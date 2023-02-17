package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.util.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.util.asDomainModel
import com.udacity.asteroidradar.util.getToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(private val database: AsteroidDatabase) {

    val allAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    val todayAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getTodayAsteroids(getToday())) {
            it.asDomainModel()
        }

    val weekAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getWeekAsteroids(getToday())) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = AsteroidApi.getAsteroids()
            database.asteroidDatabaseDao.insertAll(asteroids.asDatabaseModel())
        }
    }

    suspend fun getPictureOfDay(): PictureOfDay {
        val pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfDay = AsteroidApi.getPictureOfDay()
        }
        return pictureOfDay
    }
}