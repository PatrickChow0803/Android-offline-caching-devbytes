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

package com.example.android.devbyteviewer.database

import android.arch.lifecycle.LiveData
import android.content.Context
import androidx.room.*

// An offline cache requires 2 methods
// One to load the values from the cache and another to store values
@Dao
interface VideoDao{

    @Query("SELECT * FROM databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    // vararg is how a function can take in an unknown number of arguments
    // OnConflictStrategy.REPLACE because we want to overwrite the last saved value with the new one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg video: DatabaseVideo)

}

@Database(entities = [DatabaseVideo::class], version = 1, exportSchema = false)
abstract class VideosDatabase:RoomDatabase(){

    // Used to get access to the video dao
    abstract val videoDao: VideoDao
}

private lateinit var INSTANCE: VideosDatabase

fun getDatabase(context: Context): VideosDatabase{
    // INSTANCE.isInitialized can be used on lateinit variables to check to see if they've been assigned to something
    // This if statement checks to see if it isn't initialized. If it isn't, then build and initialize it.
    // synchronized makes it thread safe.
    // When a thread invokes a synchronized method, it automatically acquires the lock for that object and releases it when the thread completes its task.
    synchronized(VideosDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideosDatabase::class.java,
                    "videos").build()
        }
    }

    return INSTANCE
}