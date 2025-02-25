package com.pubscale.basicvideoplayer.di

import com.pubscale.basicvideoplayer.data.VideoRepository

object VideoRepositoryProvider {

    val instance by lazy {
        val apiInterface = APIInterfaceProvider.instance
        VideoRepository(apiInterface)
    }


}