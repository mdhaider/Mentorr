package com.geckosoftlabs.mentorr.model

import com.google.gson.annotations.SerializedName


data class UserRegResponse (
	@SerializedName("created") val created : Int,
	@SerializedName("___class") val ___class : String,
	@SerializedName("ownerId") val ownerId : String,
	@SerializedName("updated") val updated : String,
	@SerializedName("objectId") val objectId : String,
	@SerializedName("phoneNo") val phoneNo : Int
)