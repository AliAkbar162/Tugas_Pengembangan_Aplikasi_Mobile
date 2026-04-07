package com.example.demop4app.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.demop4app.components.BottomNavBar
import com.example.demop4app.components.bottomNavItems
import com.example.demop4app.screens.*
import com.example.demop4app.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val noteViewModel: NoteViewModel = viewModel { NoteViewModel() }
    val uiState by noteViewModel.uiState.collectAsState()

    val bottomNavRoutes = bottomNavItems.map { it.route }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBars = currentRoute in bottomNavRoutes

    // Drawer state for bonus
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBars,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Notes App Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                bottomNavItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                popToStart(navController)
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showBars) {
                    CenterAlignedTopAppBar(
                        title = { Text("Notes App") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            },
            bottomBar = {
                if (showBars) {
                    BottomNavBar(navController = navController)
                }
            }
        ) { paddingValues ->
            NavHost(
                navController    = navController,
                startDestination = Screen.NoteList.route,
                modifier         = Modifier.padding(paddingValues)
            ) {
                composable(Screen.NoteList.route) {
                    NoteListScreenContent(
                        notes = uiState.notes,
                        onNoteClick = { id -> navController.navigate(Screen.NoteDetail.createRoute(id)) },
                        onAddClick = { navController.navigate(Screen.AddNote.route) },
                        onToggleFavorite = { id -> noteViewModel.toggleFavorite(id) }
                    )
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        notes = uiState.notes.filter { it.isFavorite },
                        onNoteClick = { id -> navController.navigate(Screen.NoteDetail.createRoute(id)) },
                        onToggleFavorite = { id -> noteViewModel.toggleFavorite(id) }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreen()
                }

                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                    val note = noteViewModel.getNoteById(noteId)
                    NoteDetailScreen(
                        note = note,
                        onBack = { navController.popBackStack() },
                        onEdit = { navController.navigate(Screen.EditNote.createRoute(noteId)) },
                        onDelete = {
                            noteViewModel.deleteNote(noteId)
                            navController.popBackStack()
                        },
                        onToggleFav = { noteViewModel.toggleFavorite(noteId) }
                    )
                }

                composable(Screen.AddNote.route) {
                    AddNoteScreen(
                        onSave = { title, content ->
                            noteViewModel.addNote(title, content)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                    val note = noteViewModel.getNoteById(noteId)
                    EditNoteScreen(
                        note = note,
                        onSave = { title, content ->
                            noteViewModel.updateNote(noteId, title, content)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

private fun androidx.navigation.NavOptionsBuilder.popToStart(navController: androidx.navigation.NavController) {
    popUpTo(navController.graph.startDestinationId) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreenContent(
    notes: List<com.example.demop4app.data.model.Note>,
    onNoteClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onToggleFavorite: (Int) -> Unit
) {
    NoteListScreen(
        notes = notes,
        onNoteClick = onNoteClick,
        onAddClick = onAddClick,
        onToggleFavorite = onToggleFavorite
    )
}
