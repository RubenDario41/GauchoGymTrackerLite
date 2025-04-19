package com.example.gimnasio

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null

    private lateinit var tvEjercicio: TextView
    private lateinit var tvProgreso: TextView
    private lateinit var tvEstado: TextView

    private var nombreEjercicio = ""
    private var repeticionesPorSerie = 0
    private var totalSeries = 0

    private var repeticionesActuales = 0
    private var seriesActuales = 1

    private var gravedadZ = 0f
    private var alpha = 0.8f
    private var bufferZ = FloatArray(10)
    private var index = 0
    private var bufferInitialized = false
    private var ultimoZ = 0f
    private var tiempoUltimoMovimiento = 0L
    private val delayMovimiento = 500 // milisegundos
    private val umbralMovimiento = 1.5f
    private var direccionAnterior = 0

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nombreEjercicio = intent.getStringExtra("nombre") ?: ""
        repeticionesPorSerie = intent.getIntExtra("repeticiones", 0)
        totalSeries = intent.getIntExtra("series", 0)

        tvEjercicio = findViewById(R.id.tvEjercicio)
        tvProgreso = findViewById(R.id.tvProgreso)
        tvEstado = findViewById(R.id.tvEstado)

        tvEjercicio.text = "Ejercicio: $nombreEjercicio"
        actualizarContador()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        acelerometro?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val rawZ = event.values[2]
            gravedadZ = alpha * gravedadZ + (1 - alpha) * rawZ
            val z = rawZ - gravedadZ

            bufferZ[index % bufferZ.size] = z
            index++

            if (index >= bufferZ.size && !bufferInitialized) {
                bufferInitialized = true
            }

            if (bufferInitialized) {
                val zPromedio = bufferZ.average().toFloat()
                val deltaZ = zPromedio - ultimoZ

                val direccionActual = when {
                    deltaZ > umbralMovimiento -> 1
                    deltaZ < -umbralMovimiento -> -1
                    else -> 0
                }

                if (direccionAnterior == -1 && direccionActual == 1) {
                    val tiempoActual = System.currentTimeMillis()
                    if (tiempoActual - tiempoUltimoMovimiento > delayMovimiento) {
                        repeticionesActuales++
                        tiempoUltimoMovimiento = tiempoActual
                        if (repeticionesActuales > repeticionesPorSerie) {
                            repeticionesActuales = 1
                            seriesActuales++
                            emitirAlerta()
                        }
                        if (seriesActuales > totalSeries) {
                            seriesActuales = totalSeries
                            repeticionesActuales = repeticionesPorSerie
                            tvEstado.text = "¡Entrenamiento terminado!"
                            sensorManager.unregisterListener(this)
                        }
                        actualizarContador()
                    }
                }

                if (direccionActual != 0) {
                    direccionAnterior = direccionActual
                }

                ultimoZ = zPromedio
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun actualizarContador() {
        tvProgreso.text = "Serie $seriesActuales de $totalSeries – Repetición $repeticionesActuales de $repeticionesPorSerie"
    }

    private fun emitirAlerta() {
        repeat(3) {
            toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
            Thread.sleep(200)
        }
    }
}
