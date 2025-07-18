package com.example.sdiaavs.ui.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sdiaavs.dataModel.UserData
import com.example.sdiaavs.viewModel.SearchViewModel
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment


@Composable
fun SearchPage() {
    val searchViewModel: SearchViewModel = viewModel()
    val query by searchViewModel.query
    val suggestions by searchViewModel.suggestions
    val selectedCompany by searchViewModel.selectedCompany
    val searchResults by searchViewModel.searchResults
    val isLoading by searchViewModel.isLoading

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { searchViewModel.updateQuery(it) },
            label = { Text("Search Company") },
            modifier = Modifier.fillMaxWidth()
        )
        // Suggestions
        suggestions.forEach { company ->
            Text(
                text = company.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { searchViewModel.selectCompany(company) }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { searchViewModel.searchUsersByCompany() },
            enabled = selectedCompany != null && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search Users")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (searchResults.isNotEmpty()) {
            Text("Users found: ${searchResults.size}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(searchResults) { user ->
                    UserCard(user)
                }
            }
        } else if (selectedCompany != null) {
            Text("No users found for this company.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun UserCard(user: UserData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = user.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Email: ${user.email}")
            Text(text = "Firm: ${user.firmName}")
            Text(text = "Phone: ${user.phone}")
            Text(text = "Region: ${user.region}")
            // Add more fields as needed
        }
    }
}