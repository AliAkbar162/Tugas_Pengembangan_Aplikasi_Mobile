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
import com.example.demop4app.viewmodel.NewsViewModel
import com.example.demop4app.data.repository.NoteRepository
import com.example.demop4app.database.NoteDatabase
import com.example.demop4app.settings.SettingsRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    noteRepository: NoteRepository? = null,
    settingsRepository: SettingsRepository? = null
) {
    val navController = rememberNavController()
    
    // Proper ViewModel injection or initialization
    // For the assignment simplicity, we will assume repositories are available or handle nulls
    val noteViewModel: NoteViewModel = viewModel { 
        if (noteRepository != null) NoteViewModel(noteRepository) 
        else throw IllegalStateException("NoteRepository must be provided")
    }
    
    val newsViewModel: NewsViewModel = viewModel { NewsViewModel() }
    val uiState by noteViewModel.uiState.collectAsState()

    val bottomNavRoutes = bottomNavItems.map { it.route }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBars = currentRoute in bottomNavRoutes

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBars,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Multi-App Menu",
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
                
                // Added Settings to Drawer for Week 7
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = currentRoute == Screen.Settings.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Settings.route)
                    },
                    icon = { Icon(Icons.Default.Menu, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showBars) {
                    CenterAlignedTopAppBar(
                        title = { Text(if (currentRoute == Screen.NewsList.route) "Space News" else "Notes App") },
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
                // --- Notes App ---
                composable(Screen.NoteList.route) {
                    NoteListScreen(
                        notes = uiState.notes,
                        isLoading = uiState.isLoading,
                        searchQuery = uiState.searchQuery,
                        onSearchQueryChanged = { noteViewModel.onSearchQueryChanged(it) },
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

                // --- News App (Week 6) ---
                composable(Screen.NewsList.route) {
                    NewsScreen(
                        viewModel = newsViewModel,
                        onArticleClick = { article ->
                            newsViewModel.selectedArticle = article
                            navController.navigate(Screen.NewsDetail.route)
                        }
                    )
                }

                composable(Screen.NewsDetail.route) {
                    val article = newsViewModel.selectedArticle
                    if (article != null) {
                        NewsDetailScreen(
                            article = article,
                            onBack = { navController.popBackStack() }
                        )
                    } else {
                        LaunchedEffect(Unit) { navController.popBackStack() }
                    }
                }

                composable(Screen.Profile.route) {
                    ProfileScreen()
                }

                // --- Settings (Week 7) ---
                composable(Screen.Settings.route) {
                    if (settingsRepository != null) {
                        SettingsScreen(settingsRepository = settingsRepository)
                    }
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
