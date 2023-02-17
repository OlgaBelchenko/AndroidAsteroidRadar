package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroid: List<AsteroidEntity>)

    @Query("SELECT * FROM asteroids ORDER BY close_approach_date")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date = :date")
    fun getTodayAsteroids(date: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date >= :date ORDER BY close_approach_date")
    fun getWeekAsteroids(date: String): LiveData<List<AsteroidEntity>>
}