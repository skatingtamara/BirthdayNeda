/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.birthdayNeda.model

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.birthdayNeda.data.GiftSource
import com.example.cupcake.R
import java.text.SimpleDateFormat
import java.util.*


/** Price for a single cupcake */
private const val PRICE_PER_CUPCAKE = 0.00

/** Additional cost for same day pickup of an order */
private const val PRICE_FOR_SAME_DAY_PICKUP = 0.00  // @neda

/**
 * [OrderViewModel] holds information about a cupcake order in terms of quantity, flavor, and
 * pickup date. It also knows how to calculate the total price based on these order details.
 */
class OrderViewModel : ViewModel() {


    ////////// Testing new datamodel
    // Map of menu items
    val giftItems = GiftSource.giftItems

    // Default values for gift hours
    private var previousGiftHours = 0.0

    // Gifts selected
    private val _selectedGifts: MutableList<GiftItem?> = ArrayList()
    val selectedGifts: List<GiftItem?> = _selectedGifts

    private var _listOfGiftDescriptions: MutableList<String> = mutableListOf()
    var listOfGiftDescriptions: List<String> = emptyList()

    // Subtotal of hours
    private val _subtotal = MutableLiveData(0.0)
    val subtotal: LiveData<Double> = _subtotal




    //////////////////////////////////


    //////////////// Original working model   vvvvvvvvvvv

    // Quantity of cupcakes in this order
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    // Cupcake flavor for this order
    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor


    // Possible date options
    val dateOptions: List<String> = getPickupOptions()

    // Pickup date
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    // Price of the order so far
    private val _price = MutableLiveData<Double>()
    //val price: LiveData<String> = Transformations.map(_price) {
        // Format the price into the local currency and return this as LiveData<String>
    //    NumberFormat.getCurrencyInstance().format(it)
    //}

    val price: MutableLiveData<Double> = _price

    //////////////// Original working model ^^^^^^^^^


    init {
        // Set initial values for the order
        resetOrder()
    }

    /**
     * Set the quantity of cupcakes for this order.
     *
     * @param numberCupcakes to order
     */
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes

    }

    /**
     * Set the flavor of cupcakes for this order. Only 1 flavor can be selected for the whole order.
     *
     * @param desiredFlavor is the cupcake flavor as a string
     */
    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    //    updatePrice()
    }

    fun setDefaults() {
        _quantity.value = 1
        listOfGiftDescriptions = giftItems["app"]?.let { listOf(it.description) } as MutableList<String>

        Log.d("Default check", "Selected gifts = ${listOfGiftDescriptions}")
        Log.d( "Default check", "Count of gifts = ${_quantity.value}")

    }

    // choose gifts
    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            when (view.id) {
                R.id.checkbox_app -> {
                    if (checked) {
                        _selectedGifts.add(giftItems["app"])

                    } else {
                        _selectedGifts.remove(giftItems["app"])
                    }
                }
                R.id.checkbox_lemon -> {
                    if (checked) {
                        _selectedGifts.add(giftItems["lemon"])
                    } else {
                        _selectedGifts.remove(giftItems["lemon"])
                    }
                }
                R.id.checkbox_skatin -> {
                    if (checked) {
                        _selectedGifts.add(giftItems["skatin"])
                    } else {
                        _selectedGifts.remove(giftItems["skatin"])
                    }
                }
                R.id.checkbox_bod -> {
                    if (checked) {
                        _selectedGifts.add(giftItems["bod"])
                    } else {
                        _selectedGifts.remove(giftItems["bod"])
                    }
                }

            }
        }
        updateHours()
        Log.d("Checkbox logic", "Selected gifts = ${_selectedGifts.toString()}")
        Log.d( "Checkbox logic", "Count of gifts = ${_selectedGifts.count()}")


        // Setting quanity in order to report the summary
        _quantity.value = _selectedGifts.count()
        Log.d( "Checkbox logic", "Quantity = ${quantity.value.toString()}")


        updateDescriptionList()
        Log.d("Checkbox logic", "List of gifts: @{fullList}")   // DOESN"T WORK!!! @Tamara

    }

    /**
     * Set the pickup date for this order.
     *
     * @param pickupDate is the date for pickup as a string
     */
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
    }

    /**
     * Returns true if a flavor has not been selected for the order yet. Returns false otherwise.
     */
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    fun noGiftsSet(): Boolean {
        return _selectedGifts.isNullOrEmpty()
    }

    /**
     * Reset the order by using initial default values for the quantity, flavor, date, and price.
     */
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
        _selectedGifts.clear()
        _subtotal.value = 0.0
    }


    private fun updateHours() {
        var addingHours = 0.0
        for (gift in _selectedGifts) {
            if (gift != null) {
                addingHours += gift.hours
            }
        }
        _subtotal.value = addingHours
    }

    private fun updateDescriptionList() {
        //_listOfGiftDescriptions = emptyList()
        var fullList: List<String> = emptyList()
        for (gift in _selectedGifts) {
            if (gift != null) {
                _listOfGiftDescriptions = listOf(gift.description) as MutableList<String>
                fullList = fullList + _listOfGiftDescriptions
            }
        }
        println("full list is " + fullList)
        listOfGiftDescriptions = fullList
        println("list of Gift Descriptions is " + listOfGiftDescriptions)
    }




    /**
     * Returns a list of date options starting with the current date and the following 3 dates.
     */
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            calendar.add(Calendar.DATE, 7)
            options.add(formatter.format(calendar.time))
        }
        return options
    }






}