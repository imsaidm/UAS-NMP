package com.example.uasnmp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Healing(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val shortDesc: String,
    val longDesc: String,
    val category: String
) : Parcelable

