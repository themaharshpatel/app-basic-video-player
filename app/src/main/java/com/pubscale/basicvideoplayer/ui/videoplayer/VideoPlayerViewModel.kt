package com.pubscale.basicvideoplayer.ui.videoplayer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pubscale.basicvideoplayer.di.VideoRepositoryProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoPlayerViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {


    private val videoRepository get() = VideoRepositoryProvider.instance

    private val _videoStates = MutableStateFlow<ResponseState>(ResponseState.Loading)
    val videoStates = _videoStates.asStateFlow()

    private var videoUrl: String? = null

    private var videoFetchingJob: Job? = null

    init {
        fetchVideo()
    }


    fun playBackError(error: String?) {
        viewModelScope.launch {
            _videoStates.update {
                ResponseState.Error(error ?: "Something went wrong")
            }
        }
    }

    fun retryFetch() {
        val mViewUrl = videoUrl
        if (mViewUrl != null) {
            //This will be error state when player has shown the error
            _videoStates.update {
                ResponseState.Success(mViewUrl)
            }
        } else {
            fetchVideo()
        }
    }


    private fun fetchVideo() {
        if (videoFetchingJob?.isActive == true) return
        videoFetchingJob = viewModelScope.launch {
            videoRepository.getVideoUrl().onSuccess { videoUrl ->
                _videoStates.update {
                    this@VideoPlayerViewModel.videoUrl = videoUrl
                    ResponseState.Success(videoUrl)
                }
            }.onFailure { error ->
                _videoStates.update {
                    ResponseState.Error(error.message ?: "Something went wrong")
                }
            }
        }
    }


    sealed interface ResponseState {

        data class Success(val videoUrl: String) : ResponseState

        data object Loading : ResponseState

        data class Error(val message: String) : ResponseState


    }

}