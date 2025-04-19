package com.example.gauchoGymTrackerLite

import android.content.Context
import android.media.MediaPlayer

class SoundPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playAlarm() {
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound).apply {
            start()
        }
    }
}