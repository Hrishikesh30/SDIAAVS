package com.example.sdiaavs.ui.content

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
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
    val user by userViewModel.userData
    val context = LocalContext.current
    val uid = authViewModel.uid


    user?.let { u ->
        val profileViewModel = remember { ProfileViewModel(userViewModel) }
        LaunchedEffect(Unit) {
                profileViewModel.initializeFromUser(u)
        }
        ProfileContent(
            user = u,
            viewModel = profileViewModel,

            onSave = {
                if (uid != null) {
                    profileViewModel.saveChanges(uid, u) {
                        userViewModel.loadUserData(uid)
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onCancel = {
                profileViewModel.cancelEditing(u)
            },
            onPasswordUpdate = {
                uid?.let {
                    authViewModel.updatePassword(it, newPassword = "") {
                        Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    } ?: run {
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
    onCancel: () -> Unit,
    onPasswordUpdate: () -> Unit
) {
    val isEditing by viewModel.isEditing
    val firmAddress by viewModel.firmAddress
    val region by viewModel.region
    val phone by viewModel.phone
    val dob by viewModel.dob
    val anniversary by viewModel.anniversary
    Column(modifier = Modifier.fillMaxSize()) {
        // Row 1: Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "\uD83D\uDC64 Profile",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.width(16.dp))
            ProfileActionButtons(
                onSave = onSave,
                onCancel = onCancel,
                onEdit = { viewModel.toggleEditing() },
                onChangePassword = onPasswordUpdate,
                isEditing = isEditing,
                modifier = Modifier.alignByBaseline()
            )
        }


        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ProfileField("Name", user.name ?: "-")
            ProfileField("User ID", user.email ?: "-")
            ProfileField("Firm Name", user.firmName ?: "-")

            if (isEditing) {
                EditableProfileField(
                    "Firm Address",
                    firmAddress
                ) { viewModel.updateField("firmAddress", it) }
                EditableProfileField("Region", region) { viewModel.updateField("region", it) }
                EditableProfileField("Phone", phone) { viewModel.updateField("phone", it) }
                EditableDatePicker("Date of Birth", dob) { viewModel.updateField("dob", it) }
                EditableDatePicker(
                    "Anniversary",
                    anniversary
                ) { viewModel.updateField("anniversary", it) }

            } else {
                ProfileField("Firm Address", firmAddress)
                ProfileField("Region", region)
                ProfileField("Phone", phone)
                ProfileField("Date of Birth", dob)
                ProfileField("Anniversary", anniversary)
            }

            ProfileField("Type of Party", user.typeOfParty ?: "-")
            if (isEditing) {
                EditableAuthDealer(profileViewModel = viewModel)
            }
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
}

@Composable
fun ProfileActionButtons(
    isEditing: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onEdit: () -> Unit,
    onChangePassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isEditing) {
            Button(onClick = onSave, shape = RoundedCornerShape(12.dp)) {
                Text("Save")
            }
            OutlinedButton(onClick = onCancel, shape = RoundedCornerShape(12.dp)) {
                Text("Cancel")
            }
        } else {
            Button(onClick = onEdit, shape = RoundedCornerShape(12.dp)) {
                Text("Edit")
            }
            Button(onClick = onChangePassword, shape = RoundedCornerShape(12.dp)) {
                Text("Change Password")
            }
        }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditableAuthDealer(profileViewModel: ProfileViewModel) {
    val query by profileViewModel.query
    val suggestions by profileViewModel.suggestions
    val selectedCompanies by profileViewModel.selectedCompanies

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = {
                profileViewModel.updateQuery(it, profileViewModel::searchCompaniesByPrefix)
            },
            label = { Text("Search Company") }
        )

        suggestions.forEach { companyName ->
            Text(
                text = companyName,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        profileViewModel.selectCompany(companyName)
                    }
                    .padding(8.dp)
            )
        }

        // Show selected companies with ❌
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            selectedCompanies.forEach { company ->
                AssistChip(
                    onClick = { profileViewModel.removeCompany(company) },
                    label = {
                        Text(
                            text = company,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = Color.Gray
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors()
                )
            }
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
        onCancel = {},
        onPasswordUpdate = {}
    )
}
