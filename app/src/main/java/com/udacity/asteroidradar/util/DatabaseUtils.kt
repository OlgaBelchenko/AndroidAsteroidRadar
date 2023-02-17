package com.udacity.asteroidradar.util

import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.util.Constants.API_QUERY_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun getToday(): String {
    val formatter = SimpleDateFormat(API_QUERY_DATE_FORMAT)
    val today = Calendar.getInstance().time
    return formatter.format(today)
}