package com.pubscale.basicvideoplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private var playerView: PlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupExoPLayer()
    }

    private fun setupExoPLayer() {
        playerView = findViewById(R.id.player_view)
        player = ExoPlayer.Builder(this).build()
        playerView?.player = player
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.sample_video)
        val mediaItem = MediaItem.fromUri(videoUri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
    }

    override fun onRestart() {
        super.onRestart()
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }
}