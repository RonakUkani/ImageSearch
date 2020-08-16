package com.app.imagesearchdemo.data


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageData(
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("images")
    val images: List<Image?>? = listOf(),
    val commentList: MutableList<String> = mutableListOf()
) : Parcelable {

    @Parcelize
    data class Image(
        @SerializedName("link")
        val link: String? = ""
    ) : Parcelable
}