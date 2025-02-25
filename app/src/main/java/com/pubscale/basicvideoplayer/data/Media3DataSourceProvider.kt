package com.pubscale.basicvideoplayer.data

import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.pubscale.basicvideoplayer.BasicVideoPlayerApp
import java.io.File


@UnstableApi
object Media3DataSourceProvider {

    val cacheDataSource by lazy {
        val databaseProvider = StandaloneDatabaseProvider(BasicVideoPlayerApp.instance)

        val cacheDir = File(BasicVideoPlayerApp.instance.cacheDir, "VideoSolutionCache")
        SimpleCache(
            cacheDir,
            LeastRecentlyUsedCacheEvictor(200 * 1024 * 1024),
            databaseProvider
        )
    }


    val cachedHttpDataSource by lazy {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        CacheDataSource.Factory()
            .setCache(cacheDataSource)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }


}