package com.example.aboutdogs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aboutdogs.model.DogBreed

/**
 * Created by Mayokun Adeniyi on 12/01/2020.
 */

@Database(entities = [DogBreed::class], version = 1, exportSchema = false)
abstract class DogDatabase : RoomDatabase() {

    abstract val dogDao: DogDao

    companion object {
        @Volatile
        private var instance: DogDatabase? = null

        fun getInstance(context: Context): DogDatabase {
            synchronized(this) {
                var _instance = instance
                if (_instance == null) {
                    _instance = Room.databaseBuilder(
                        context.applicationContext,
                        DogDatabase::class.java,
                        "dog_breed_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    instance = _instance
                }
                return _instance
            }
        }
    }
}