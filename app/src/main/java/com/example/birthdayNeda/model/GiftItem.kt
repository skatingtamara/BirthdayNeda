package com.example.birthdayNeda.model


import java.text.NumberFormat


/**
 * Data class for gift items
 */

data class GiftItem (
    val name: String,
    val description: String,
    val hours: Double
        )
{
    /**
     * Getter method for hours.
     */
    //fun getHours(): String = NumberFormat.format(hours)
}
