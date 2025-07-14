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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sdiaavs.viewModel.AuthViewModel
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
fun ProfilePage(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit
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
            certificate = user.certificate ?: "-",
            dob = formatTimestamp(user.dob),
            anniversary = formatTimestamp(user.anniversary),
            typeOfParty = user.typeOfParty ?: "-",
            authDOC = user.authDOC ?: emptyList(),
            onLogoutClick = {
                authViewModel.signOut()
                onLogoutClick()
            },

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
    certificate: String,
    dob: String,
    anniversary: String,
    typeOfParty: String,
    authDOC: List<String>,
    onLogoutClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    // Make outermost container scrollable and ensure full height is allowed
    Column(
        modifier = Modifier
            .fillMaxSize() // Make sure it takes full screen height
            .verticalScroll(scrollState) // Enable scrolling
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Header Row with logout button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "👤 User Profile",
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = onLogoutClick) {
                Text("Logout")
            }
        }

        // Profile fields
        ProfileField(label = "Name", value = name)
        ProfileField(label = "Email", value = email)
        ProfileField(label = "Firm Name", value = firmName)
        ProfileField(label = "Firm Address", value = firmAddress)
        ProfileField(label = "Region", value = region)
        ProfileField(label = "Phone", value = phone)
        ProfileField(label = "Certificate", value = certificate)
        ProfileField(label = "Date of Birth", value = dob)
        ProfileField(label = "Anniversary", value = anniversary)
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
        certificate = "Yes",
        dob = "13 Nov 1970",
        anniversary = "05 Feb 1995",
        typeOfParty = "Retailer",
        authDOC = listOf("Patanjali", "Dabur"),
        onLogoutClick = {},
    )
}
