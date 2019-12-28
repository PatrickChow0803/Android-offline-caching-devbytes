/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.repository

import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*
 * Repository for fetching devbyte videos from the network and storing them on disk.
 */
class VideosRepository (private val database: VideosDatabase){

    suspend fun refreshVideos(){
        // Reading and writing from a disk is called disk IO. Therefore use Dispatchers.IO
        // withContext forces a Kotlin Coroutine to switch to the dispatchers specified.
        withContext(Dispatchers.IO){

            // await() to tell the coroutine to suspend until it's available
            val playlist = Network.devbytes.getPlaylist().await()
            database.videoDao.insertAll(*playlist.asDatabaseModel())

        }
    }

}






















