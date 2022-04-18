package com.example.birthdayNeda.data

import com.example.birthdayNeda.model.GiftItem

object GiftSource {
    val giftItems = mapOf(
        "app" to
                GiftItem(
                    name = "App only",
                    description = "Just this homemade app, thanks",
                    hours = 0.1
                ),
        "lemon" to
                GiftItem(
                    name = "40 Lemon Trees",
                    description = "40 lemon trees :)",
                    hours = 3.14
                ),
        "skatin" to
                GiftItem(
                    name = "Roller skatin kit",
                    description = "Roller skatin starter kit",
                    hours = 10.0
                ),
        "bod" to
                GiftItem(
                    name = "Summer bod",
                    description = "Summer ready bod (includes virtual Tamara!)",
                    hours = 60.18
                ),
        "all" to
                GiftItem(
                    name = "All the gifts",
                    description = "Let's do this: I WANT IT ALL",
                    hours = 91.34
                )
    )
}