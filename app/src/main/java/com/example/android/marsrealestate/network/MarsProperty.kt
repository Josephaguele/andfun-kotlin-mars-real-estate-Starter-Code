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

package com.example.android.marsrealestate.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

//In MarsProperty.kt, convert the class to a Kotlin data class with properties that match the JSON response fields:
//We make MarsProperty parcelable so it can be passed as an argument in navigation :
//In MarsProperty, we make the class parcelable by extending it from Parcelable and adding the @Parcelize annotation:
@Parcelize
data class MarsProperty(
    val id: String,
    @Json(name = "img_src") val imgSrcUrl: String, /*Rename the img_src class property to imgSrcUrl,
     and add a @Json annotation to remap the img_src JSON field to it:*/
    val type: String,
    val price: Double): Parcelable{

    //Inside the MarsProperty class, create an isRental boolean, and set its value based on whether
    // the property type is "rent":
    val isRental
        get() = type == "rent"
    }

