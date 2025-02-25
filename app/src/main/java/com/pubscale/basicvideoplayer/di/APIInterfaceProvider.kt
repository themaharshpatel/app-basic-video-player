package com.pubscale.basicvideoplayer.di

import com.pubscale.basicvideoplayer.data.retrofit.APIClient.client
import com.pubscale.basicvideoplayer.data.retrofit.APIInterface

object APIInterfaceProvider {

    val instance: APIInterface by lazy {
        client.create(APIInterface::class.java)
    }
}