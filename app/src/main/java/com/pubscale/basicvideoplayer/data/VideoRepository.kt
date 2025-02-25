package com.pubscale.basicvideoplayer.data

import com.pubscale.basicvideoplayer.data.retrofit.APIInterface

class VideoRepository (
    private val apiInterface: APIInterface
) {


    suspend fun getVideoUrl():Result<String> {
        return runCatching {
            apiInterface.getVideoUrl().let {
                if(it.url == null){
                    throw NullPointerException("Video Url is null")
                }
                it.url
            }
        }
    }


}