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

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData MarsApiStatus that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status MarsApiStatus
    // This will return the status, whether it is done, loading or having an error
    val status: LiveData<MarsApiStatus>
        get() = _status

    // a list of MarsProperty
    private val _properties = MutableLiveData<List<MarsProperty>>()

    val property: LiveData<List<MarsProperty>>
        get() = _properties

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {// we pass MarsApiFilter.SHOW_ALL as the default parameter to getMarsRealEstateProperties():
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }


    /**
     * Sets the value of the status LiveData to the Mars API status.
     * Gets Mars real estate property information from the Mars API Retrofit service and updates the
     * [MarsProperty] [List] [LiveData]. The Retrofit service returns a coroutine Deferred, which we
     * await to get the result of the transaction.
     */
    //add a MarsApiFilter parameter to getMarsRealEstateProperties():
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try { //Pass the filter.value to retrofitService.getProperties():
                _properties.value = MarsApi.retrofitService.getProperties(filter.value)
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }


    //Now create an updateFilter() method to requery the data by calling
    // getMarsRealEstateProperties with the new filter:
    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }


   // In OverviewViewModel, add an encapsulated LiveData variable for navigating to
   // the selectedProperty detail screen:
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty

    //Add a function to set _navigateToSelectedProperty to marsProperty and initiate navigation
    // to the detail screen on button click:
    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }

    //and you'll need to add displayPropertyDetailsComplete() to set _navigateToSelectedProperty
    // to false once navigation is completed to prevent unwanted extra navigations:
    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

}


enum class MarsApiStatus{LOADING, ERROR, DONE}