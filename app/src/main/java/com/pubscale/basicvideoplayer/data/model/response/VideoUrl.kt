package com.pubscale.basicvideoplayer.data.model.response

import com.google.gson.annotations.SerializedName

data class VideoUrl(
    @SerializedName("url") val url: String?
)
