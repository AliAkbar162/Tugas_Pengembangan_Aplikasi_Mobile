package com.example.demop4app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

// Data class untuk setiap item di Bottom Navigation
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

// Daftar item Bottom Nav
val bottomNavItems = listOf(
    BottomNavItem(Screen.NoteList.route,  "Notes",     Icons.Default.Home),
    BottomNavItem(Screen.Favorites.route, "Favorites", Icons.Default.Favorite),
    BottomNavItem(Screen.Profile.route,   "Profile",   Icons.Default.Person)
)

@Composable
fun BottomNavBar(navController: NavController) {
    // Observasi route yang aktif saat ini
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Kembali ke start destination agar tidak tumpuk di back stack
                        popUpTo(navController.graph.findStartDestination().route!!) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                icon  = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}