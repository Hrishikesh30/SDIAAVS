package com.example.sdiaavs.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sdiaavs.dataModel.UserData
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileViewModel(
    private val userViewModel: UserViewModel
) : ViewModel() {

    var isEditing = mutableStateOf(false)
        private set

    var firmAddress = mutableStateOf("")
        private set
    var region = mutableStateOf("")
        private set
    var phone = mutableStateOf("")
        private set
    var dob = mutableStateOf("")
        private set
    var anniversary = mutableStateOf("")
        private set

    fun initializeFromUser(user: UserData) {
        firmAddress.value = user.firmAddress ?: ""
        region.value = user.region ?: ""
        phone.value = user.phone ?: ""
        dob.value = formatTimestamp(user.dob)
        anniversary.value = formatTimestamp(user.anniversary)
    }

    fun updateField(field: String, value: String) {
        when (field) {
            "firmAddress" -> firmAddress.value = value
            "region" -> region.value = value
            "phone" -> phone.value = value
            "dob" -> dob.value = value
            "anniversary" -> anniversary.value = value
        }
    }

    fun toggleEditing() {
        isEditing.value = !isEditing.value
    }

    fun cancelEditing(user: UserData) {
        initializeFromUser(user)
        isEditing.value = false
    }

    fun saveChanges(uid: String, user: UserData, onSuccess: () -> Unit) {
        val updatedUser = user.copy(
            firmAddress = firmAddress.value,
            region = region.value,
            phone = phone.value,
            dob = parseToTimestamp(dob.value),
            anniversary = parseToTimestamp(anniversary.value)
        )

        userViewModel.updateUserData(uid, updatedUser) {
            onSuccess()
            isEditing.value = false
        }
    }

    private fun formatTimestamp(timestamp: Timestamp?): String {
        return if (timestamp != null) {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = timestamp.toDate()
            sdf.format(date)
        } else "-"
    }

    private fun parseToTimestamp(dateStr: String): Timestamp? {
        return try {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = sdf.parse(dateStr)
            date?.let { Timestamp(it) }
        } catch (e: Exception) {
            null
        }
    }
}
