package com.example.mediaplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var playBtn:ImageView
    private lateinit var skipBtn:ImageView
    private lateinit var tvMusicTitle:TextView

    private var isPlaying = false
    private var isBound = false
    private lateinit var musicPlayerService: MusicPlayerService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playBtn=findViewById(R.id.btnPlayPause)
        skipBtn=findViewById(R.id.btnSkip)
        tvMusicTitle=findViewById(R.id.tvMusicTitle)


        playBtn.setOnClickListener {
            if(!isPlaying){
                musicPlayerService.play()
                tvMusicTitle.text=musicPlayerService.nowPlaying
                isPlaying=true
                playBtn.setImageResource(R.drawable.pause_circle_24)
            }else{
                musicPlayerService.pauseTrack()
                isPlaying=false
                playBtn.setImageResource(R.drawable.play_circle_24)
            }
        }

        skipBtn.setOnClickListener {
            musicPlayerService.skipTrack()
            tvMusicTitle.text=musicPlayerService.nowPlaying
        }



    }



    private val serviceConnection = object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicPlayerService.LocalBinder
            musicPlayerService = binder.getService()
            isBound=true

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound=false
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this,MusicPlayerService::class.java)
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE)
        Log.d("MainActivity","Service Started")
    }

    override fun onStop() {
        super.onStop()
        if(isBound){
            //unbindService(serviceConnection)
            isBound=false
        }

    }


}