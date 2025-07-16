package com.example.sdiaavs.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sdiaavs.viewModel.UserViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


fun formatTimestamp(timestamp: Timestamp?): String {
    return if (timestamp != null) {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = timestamp.toDate()
        sdf.format(date)
    } else {
        "-"
    }
}

@Composable
fun EditableProfileField(label: String, value: String, onValueChange: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Composable
fun ProfilePage(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
) {
    val user = userViewModel.userData

    if (user != null) {
        ProfileContent(
            name = user.name ?: "",
            email = user.email ?: "",
            firmName = user.firmName ?: "-",
            firmAddress = user.firmAddress ?: "-",
            region = user.region ?: "-",
            phone = user.phone ?: "-",
            dob = formatTimestamp(user.dob),
            anniversary = formatTimestamp(user.anniversary),
            typeOfParty = user.typeOfParty ?: "-",
            authDOC = user.authDOC ?: emptyList(),

        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Loading...", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
@Composable
fun ProfileContent(
    name: String,
    email: String,
    firmName: String,
    firmAddress: String,
    region: String,
    phone: String,
    dob: String,
    anniversary: String,
    typeOfParty: String,
    authDOC: List<String>
) {
    val scrollState = rememberScrollState()
    var isEditing by remember { mutableStateOf(false) }
    var firmAddressState by remember { mutableStateOf(firmAddress) }
    var regionState by remember { mutableStateOf(region) }
    var phoneState by remember { mutableStateOf(phone) }
    var dobState by remember { mutableStateOf(dob) }
    var anniversaryState by remember { mutableStateOf(anniversary) }

    // Make outermost container scrollable and ensure full height is allowed
    Column(
        modifier = Modifier
            .fillMaxSize() // Make sure it takes full screen height
            .verticalScroll(scrollState) // Enable scrolling
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "👤 User Profile",
            style = MaterialTheme.typography.headlineSmall
        )
            if (isEditing) {
                Button(onClick = {
                    // TODO: Save logic (send to ViewModel or DB)
                    isEditing = false
                }) {
                    Text("Save")
                }
            } else {
                Button(onClick = { isEditing = true }) {
                    Text("Edit")
                }
            }

    }


        // Profile fields
        ProfileField(label = "Name", value = name)
        ProfileField(label = "User ID", value = email)
        ProfileField(label = "Firm Name", value = firmName)
    if (isEditing) {
        EditableProfileField(label = "Firm Address", value = firmAddressState) {
            firmAddressState = it
        }
    } else {
        ProfileField(label = "Firm Address", value = firmAddressState)
    }
    if (isEditing) {
        EditableProfileField(label = "Region", value = regionState) {
            regionState = it
        }
    } else {
            ProfileField(label = "Region", value = regionState)
    }
    if (isEditing) {
        EditableProfileField(label = "Phone", value = phoneState) {
            phoneState = it
        }
    } else {
        ProfileField(label = "Phone", value = phoneState)
    }

    if (isEditing) {
        EditableProfileField(label = "Date of Birth", value = dobState) {
            dobState = it
        }
    } else {
        ProfileField(label = "Date of Birth", value = dobState)
    }

    if (isEditing) {
        EditableProfileField(label = "Anniversary", value = anniversaryState) {
           anniversaryState = it
        }
    } else {
        ProfileField(label = "Anniversary", value = anniversaryState)
    }
        ProfileField(label = "Type of Party", value = typeOfParty)
        if (authDOC.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "🏭 Authorized Dealer Of:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                authDOC.forEach {
                    Text("• $it", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
        Spacer(modifier = Modifier.height(100.dp))
    }
}


@Composable
fun ProfileField(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileContent(
        name = "श्री आशीष जी दुबे",
        email = "sdiaavs_2@sdiaavs.com",
        firmName = "भाग्यश्री मेडिकोज",
        firmAddress = "जवाहर मार्ग",
        region = "जवाहर मार्ग",
        phone = "9826020160",
        dob = "13 Nov 1970",
        anniversary = "05 Feb 1995",
        typeOfParty = "Retailer",
        authDOC = listOf("Patanjali", "Dabur"),
    )
}
