package com.example.sdiaavs.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sdiaavs.dataModel.UserData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.getValue


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
    var authDOC = mutableStateOf<List<String>>(emptyList()) // List of company IDs
        private set

    var query = mutableStateOf("")
        private set

    var suggestions = mutableStateOf<List<String>>(emptyList())
        private set

    var selectedCompanies = mutableStateOf<List<String>>(emptyList())
        private set

    fun updateQuery(newQuery: String, searchFunction: (String, (List<String>) -> Unit) -> Unit) {
        query.value = newQuery
        searchFunction(newQuery) { results ->
            suggestions.value = results
        }
    }

    fun selectCompany(company: String) {
        val updated = selectedCompanies.value.toMutableList()
        if (!updated.contains(company)) {
            updated.add(company)
            selectedCompanies.value = updated
        }
        query.value = ""
        suggestions.value = emptyList()
    }

    fun removeCompany(company: String) {
        val updated = selectedCompanies.value.toMutableList()
        updated.remove(company)
        selectedCompanies.value = updated
    }

    fun initializeFromUser(user: UserData) {
        firmAddress.value = user.firmAddress ?: ""
        region.value = user.region ?: ""
        phone.value = user.phone ?: ""
        dob.value = formatTimestamp(user.dob)
        anniversary.value = formatTimestamp(user.anniversary)
        authDOC.value = user.authDOC ?: emptyList()
        selectedCompanies.value = user.authDOC ?: emptyList()
    }

    fun updateField(field: String, value: String) {
        when (field) {
            "firmAddress" -> firmAddress.value = value
            "region" -> region.value = value
            "phone" -> phone.value = value
            "dob" -> dob.value = value
            "anniversary" -> anniversary.value = value
            "authDOC" -> authDOC.value = listOf(value)
        }
    }

    fun updateAuthDealerList(newList: List<String>) {
        authDOC.value = newList
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
            anniversary = parseToTimestamp(anniversary.value),
            authDOC = selectedCompanies.value
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

    fun searchCompaniesByPrefix(prefix: String, onResult: (List<String>) -> Unit) {
        val db = Firebase.firestore
        db.collection("companies")
            .orderBy("companyName")
            .startAt(prefix)
            //.endAt(prefix + "\uf8ff")
            .limit(10)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val names = querySnapshot.documents.mapNotNull { it.getString("companyName") }
                println("db valuew:$db")
                println("🔍 Found companies: $names")
                onResult(names)
            }
            .addOnFailureListener { e ->
                println("❌ Error fetching companies: ${e.localizedMessage}")
            }

    }



}
