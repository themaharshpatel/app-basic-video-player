package com.pubscale.basicvideoplayer.ui.videoplayer

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.pubscale.basicvideoplayer.databinding.ActivityVideoPlayerBinding
import kotlinx.coroutines.launch

class VideoPlayerActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null

    private var binding: ActivityVideoPlayerBinding? = null

    private val videoPlayerViewModel by viewModels<VideoPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater).apply {
            setContentView(root)
            ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            retryBtn.setOnClickListener {
                videoPlayerViewModel.retryFetch()
            }
        }


        setupExoPLayer()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                videoPlayerViewModel.videoStates.collect { state ->
                    when (state) {
                        is VideoPlayerViewModel.ResponseState.Error -> {
                            binding?.errorGroup?.visibility = View.VISIBLE
                            binding?.errorText?.text = state.message
                            binding?.progressCircular?.hide()
                        }

                        VideoPlayerViewModel.ResponseState.Loading -> {
                            pausePlayer()
                            binding?.progressCircular?.show()
                        }

                        is VideoPlayerViewModel.ResponseState.Success -> {
                            binding?.progressCircular?.hide()
                            binding?.errorGroup?.visibility = View.GONE

                            player?.let { mPlayer ->
                                val mediaItem = MediaItem.fromUri(Uri.parse(state.videoUrl))
                                mPlayer.setMediaItem(mediaItem)
                                mPlayer.prepare()
                                mPlayer.playWhenReady = true
                                binding?.playerView?.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun pausePlayer() {
        binding?.let {
            it.playerView.player?.stop()
            it.playerView.visibility = View.GONE
        }
    }

    private fun setupExoPLayer() {
        player = ExoPlayer.Builder(this).build()
        binding?.playerView?.player = player
        player?.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                videoPlayerViewModel.playBackError(error.message)
            }
        })


    }

    override fun onRestart() {
        super.onRestart()
        player?.play()
    }

    @OptIn(UnstableApi::class)
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding?.playerView?.apply {
                hideController()
                val aspectRatio = Rational(width, height)
                val pipBuilder = PictureInPictureParams.Builder()
                pipBuilder.setAspectRatio(aspectRatio)
                enterPictureInPictureMode(pipBuilder.build())
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        binding?.playerView?.useController = !isInPictureInPictureMode
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        binding = null
        player = null
    }

}