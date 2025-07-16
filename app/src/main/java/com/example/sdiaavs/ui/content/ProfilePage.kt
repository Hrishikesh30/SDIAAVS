package com.example.sdiaavs.ui.content

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sdiaavs.dataModel.UserData
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.ProfileViewModel
import com.example.sdiaavs.viewModel.UserViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun ProfilePage(
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val user = userViewModel.userData
    val context = LocalContext.current
    val uid = authViewModel.uid

    if (user != null) {
        val profileViewModel = remember { ProfileViewModel(userViewModel) }
        LaunchedEffect(Unit) {
            profileViewModel.initializeFromUser(user)
        }
        ProfileContent(
            user = user,
            viewModel = profileViewModel,
            onSave = {
                if (uid != null) {
                    profileViewModel.saveChanges(uid, user) {
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onCancel = {
                profileViewModel.cancelEditing(user)
            }
        )
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun ProfileContent(
    user: UserData,
    viewModel: ProfileViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val isEditing by viewModel.isEditing
    val firmAddress by viewModel.firmAddress
    val region by viewModel.region
    val phone by viewModel.phone
    val dob by viewModel.dob
    val anniversary by viewModel.anniversary

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("👤 User Profile", style = MaterialTheme.typography.headlineSmall)

            if (isEditing) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onSave) { Text("Save") }
                    Button(onClick = onCancel) { Text("Cancel") }
                }
            } else {
                Button(onClick = { viewModel.toggleEditing() }) { Text("Edit") }
            }
        }

        ProfileField("Name", user.name ?: "-")
        ProfileField("User ID", user.email ?: "-")
        ProfileField("Firm Name", user.firmName ?: "-")

        if (isEditing) {
            EditableProfileField("Firm Address", firmAddress) { viewModel.updateField("firmAddress", it) }
            EditableProfileField("Region", region) { viewModel.updateField("region", it) }
            EditableProfileField("Phone", phone) { viewModel.updateField("phone", it) }
            EditableDatePicker("Date of Birth", dob) { viewModel.updateField("dob", it) }
            EditableDatePicker("Anniversary", anniversary) { viewModel.updateField("anniversary", it) }
        } else {
            ProfileField("Firm Address", firmAddress)
            ProfileField("Region", region)
            ProfileField("Phone", phone)
            ProfileField("Date of Birth", dob)
            ProfileField("Anniversary", anniversary)
        }

        ProfileField("Type of Party", user.typeOfParty ?: "-")
        if (!user.authDOC.isNullOrEmpty()) {
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("🏭 Authorized Dealer Of:")
                    user.authDOC.forEach { Text("• $it") }
                    println(user.authDOC)
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
fun EditableDatePicker(
    label: String,
    value: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // Open date picker dialog on click
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate = formatter.format(calendar.time)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
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
                text = value.ifBlank { "Select date" },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    val mockUser = UserData(
        name = "श्री आशीष जी दुबे",
        email = "sdiaavs_2@sdiaavs.com",
        firmName = "भाग्यश्री मेडिकोज",
        firmAddress = "9B-xyz block",
        region = "Jawahar Marg",
        phone = "9876543210",
        dob = Timestamp(Date()),
        anniversary = Timestamp(Date()),
        typeOfParty = "retailer",
        authDOC = listOf("Patanjali", "Dabur")
    )

    val mockViewModel = remember {
        ProfileViewModel(UserViewModel()).apply {
            initializeFromUser(mockUser)
        }
    }

    ProfileContent(
        user = mockUser,
        viewModel = mockViewModel,
        onSave = {},
        onCancel = {}
    )
}
