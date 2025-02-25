package com.pubscale.basicvideoplayer.data.retrofit

import com.pubscale.basicvideoplayer.data.model.response.VideoUrl
import retrofit2.http.GET

interface APIInterface {

    @GET("greedyraagava/test/refs/heads/main/video_url.json")
    suspend fun getVideoUrl(): VideoUrl


}