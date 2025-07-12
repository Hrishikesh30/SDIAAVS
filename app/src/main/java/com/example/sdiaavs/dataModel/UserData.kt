package com.example.sdiaavs.dataModel
import com.google.firebase.Timestamp
data class UserData(
    var name: String = "",
    var email: String = "",
    var anniversary: Timestamp? = null,
    var certificate: String = "",
    var dob: Timestamp? = null,
    var firmAddress: String? = null,
    var firmName: String = "",
    var phone: String = "",
    var region: String = "",
    var userID: String = ""
)

