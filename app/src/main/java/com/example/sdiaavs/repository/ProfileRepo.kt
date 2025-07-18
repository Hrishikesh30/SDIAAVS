package com.example.sdiaavs.repository

import com.example.sdiaavs.dataModel.CompanyItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ProfileRepo {
    suspend fun resolveCompanyNamesFromIds(ids: List<String>): List<CompanyItem> {
        val db = Firebase.firestore
        val result = mutableListOf<CompanyItem>()

        try {
            ids.chunked(10).forEach { chunk ->
                val query = db.collection("companies")
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                result += query.documents.mapNotNull { doc ->
                    val name = doc.getString("companyName") ?: return@mapNotNull null
                    CompanyItem(name = name, companyId = doc.id)
                }
            }
        } catch (e: Exception) {
            println("❌ Failed to resolve company names: ${e.localizedMessage}")
        }

        return result
    }

}