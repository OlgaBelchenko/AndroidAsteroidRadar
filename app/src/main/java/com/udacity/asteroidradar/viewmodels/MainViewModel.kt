package com.udacity.asteroidradar.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {

    /**
     * Repository.
     */
    private val asteroidRepository = AsteroidRepository(AsteroidDatabase.getInstance(application))

    /**
     * List of all Asteroids from database.
     */
    private val allAsteroids = asteroidRepository.allAsteroids

    /**
     * List of today's asteroids.
     */
    private val todayAsteroids = asteroidRepository.todayAsteroids

    /**
     * List of asteroids for week
     */
    private val weekAsteroids = asteroidRepository.weekAsteroids

    /**
     * Mediator for switching between all, today and week asteroids
     */
    val asteroids = MediatorLiveData<List<Asteroid>>()

    /**
     * Picture of the day.
     */
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    /**
     * Navigation to Asteroid detail.
     */
    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment: MutableLiveData<Asteroid?>
        get() = _navigateToDetailFragment

    init {
        refreshAsteroidsFromRepository()
        getPictureOfDay()
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

    /**
     * Refresh data from the repository in a background thread.
     */
    private fun refreshAsteroidsFromRepository() {
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
                getWeekAsteroids()
            } catch (e: Exception) {
                Log.e(TAG, "Couldn't refresh data from repository: ${e.message}")
                getAllAsteroids()
            }
        }
    }

    /**
     * Get picture of the day.
     */
    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = asteroidRepository.getPictureOfDay()
            } catch (e: Exception) {
                Log.e(TAG, "Couldn't get picture of the day: ${e.message}")
            }
        }
    }

    /**
     * Navigate to Asteroid
     */
    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    /**
     * Resets the network error flag.
     */
    fun displayPropertyDetailsComplete() {
        _navigateToDetailFragment.value = null
    }

    /**
     * Gets all asteroids from repository.
     */
    fun getAllAsteroids() {
       removeAllSources()
        asteroids.addSource(allAsteroids) {
            asteroids.value = it
        }
    }

    /**
     * Get today asteroids.
     */
    fun getTodayAsteroids() {
        removeAllSources()
        asteroids.addSource(todayAsteroids) {
            asteroids.value = it
        }
    }

    /**
     * Get week asteroids.
     */
    fun getWeekAsteroids() {
        removeAllSources()
        asteroids.addSource(weekAsteroids) {
            asteroids.value = it
        }
    }

    /**
     * Remove all sources for asteroid mediator livedata.
     */
    private fun removeAllSources() {
        asteroids.removeSource(allAsteroids)
        asteroids.removeSource(todayAsteroids)
        asteroids.removeSource(weekAsteroids)
    }

}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}