package com.example.sdiaavs.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdiaavs.dataModel.CompanyItem
import com.example.sdiaavs.dataModel.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    var query = mutableStateOf("")
        private set
    var suggestions = mutableStateOf<List<CompanyItem>>(emptyList())
        private set
    var selectedCompany = mutableStateOf<CompanyItem?>(null)
        private set
    var searchResults = mutableStateOf<List<UserData>>(emptyList())
        private set
    var isLoading = mutableStateOf(false)
        private set

    fun updateQuery(newQuery: String) {
        query.value = newQuery
        searchCompaniesByPrefix(newQuery)
    }

    private fun searchCompaniesByPrefix(prefix: String) {
        val db = Firebase.firestore
        db.collection("companies")
            .orderBy("companyName")
            .startAt(prefix)
            .limit(10)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val results = querySnapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("companyName")
                    val id = doc.id
                    if (name != null) CompanyItem(name, id) else null
                }
                suggestions.value = results
            }
            .addOnFailureListener { e ->
                println("❌ Error fetching companies: ${e.localizedMessage}")
            }
    }

    fun selectCompany(company: CompanyItem) {
        selectedCompany.value = company
        query.value = company.name
        suggestions.value = emptyList()
    }

    fun searchUsersByCompany() {
        val companyId = selectedCompany.value?.companyId ?: return
        isLoading.value = true
        val db = Firebase.firestore
        db.collection("users")
            .whereArrayContains("authDOC", companyId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val users = querySnapshot.documents.mapNotNull { it.toObject(UserData::class.java) }
                searchResults.value = users
                isLoading.value = false
            }
            .addOnFailureListener { e ->
                println("❌ Error fetching users: ${e.localizedMessage}")
                isLoading.value = false
            }
    }
}