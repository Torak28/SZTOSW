package com.pwr.sailapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.sail.Centre
import com.pwr.sailapp.data.sail.Equipment
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.network.sail.SailNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// https://developer.android.com/jetpack/docs/guide
// https://medium.com/androiddevelopers/coroutines-on-android-part-i-getting-the-background-3e0e54d20bb

class MainRepositoryImpl(
    private val sailNetworkDataSource: SailNetworkDataSource
) : MainRepository {

    private val centres = MutableLiveData<ArrayList<Centre>>()
    private val allCentreGear = MutableLiveData<ArrayList<Equipment>>()
    private val allUserRentals = MutableLiveData<ArrayList<Rental>>()

    init {
        // observe forever (repos don't have lifecycle) changes in live data (responses)
        sailNetworkDataSource.apply {
            downloadedCentres.observeForever {
                centres.postValue(it.centres)
            }
            downloadedAllCentreGear.observeForever {
                allCentreGear.postValue(it.gear)
            }
            downloadedAllUserRentals.observeForever {
                allUserRentals.postValue(it.rentals)
            }
        }
    }

    override suspend fun getAllUserRentals(userID: Int): LiveData<ArrayList<Rental>> {
        // create a block that will run on the IO dispatcher
        return withContext(Dispatchers.IO) {
            fetchAllUserRentals(userID)
            allUserRentals
        }

    }

    override suspend fun getAllCentreGear(centreID: Int): LiveData<ArrayList<Equipment>> {
        return withContext(Dispatchers.IO) {
            fetchAllCentreGear(centreID)
            allCentreGear
        }
    }

    override suspend fun getCentres(): LiveData<ArrayList<Centre>> {
        return withContext(Dispatchers.IO) {
            fetchCentres()
            centres
        }
    }

    private suspend fun fetchCentres(){ sailNetworkDataSource.fetchCentres() }
    private suspend fun fetchAllCentreGear(centreID: Int){ sailNetworkDataSource.fetchAllCentreGear(centreID) }
    private suspend fun fetchAllUserRentals(rentalID: Int){ sailNetworkDataSource.fetchAllUserRentals(rentalID) }
}