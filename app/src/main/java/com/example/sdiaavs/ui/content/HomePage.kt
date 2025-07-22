package com.example.sdiaavs.ui.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdiaavs.R
import com.example.sdiaavs.dataModel.UserData
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.UserViewModel

@Composable
fun HomePageContent(
    user: UserData?,
    onLogoutClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Top banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "श्री धन्वंतरी इन्दौर आयुर्वेदिक औषधि विक्रेता संघ",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )
        }

        // Logo + User Info + Logout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.sdiaavs4_1),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Black, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // User Info and Logout in Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "User ID: ${user?.userID ?: "-"}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Name: ${user?.name ?: "-"}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Logout")
                }
            }
        }

        // Center welcome message
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "आपका स्वागत है !",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = user?.name ?: "",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
@Composable
fun HomePage(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onLogoutClick: () -> Unit
) {
    val userData by userViewModel.userData
    HomePageContent(
        user = userData,
        onLogoutClick = {
            authViewModel.signOut()
            onLogoutClick()
        },
    )
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomePageContent(
        user = UserData(
            name = "श्री अखिलेश जी शर्मा",
            userID = "IAAVS_0004"
        ),
        onLogoutClick = {},
    )
}

