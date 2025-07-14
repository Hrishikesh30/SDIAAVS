package com.example.sdiaavs.ui.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.sdiaavs.dataModel.NavItem
import com.example.sdiaavs.viewModel.AuthViewModel
import com.example.sdiaavs.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Profile", Icons.Default.Person),
        NavItem("Search", Icons.Default.Search),
        NavItem("Help", Icons.Default.Info)
    )
    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid

    LaunchedEffect(uid) {
        if (uid != null && userViewModel.userData == null) {
            userViewModel.loadUserData(uid)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = "Icon"
                            )
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        },

        ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedItem,
            authViewModel,
            userViewModel,
            onLogout
        )
    }

}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedItem: Int,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onLogout: () -> Unit
) {
    when (selectedItem) {
        0 -> HomePage(
            userViewModel = userViewModel,
        )

        1 -> ProfilePage(
            authViewModel= authViewModel,
            modifier = Modifier,
            userViewModel = userViewModel,
            onLogoutClick = onLogout
        )

        2 -> SearchPage()
        3 -> HelpPage()
    }
}