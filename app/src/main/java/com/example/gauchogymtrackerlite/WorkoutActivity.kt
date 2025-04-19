class WorkoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val startWorkoutButton = findViewById<Button>(R.id.startWorkoutButton)
        startWorkoutButton.setOnClickListener {
            // Lógica para iniciar el entrenamiento
        }
    }
}