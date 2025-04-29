package com.example.pay

import androidx.annotation.DrawableRes

data class OnboardingItem(
    val title: String,
    val description: String,
    @DrawableRes val imageResId: Int
)