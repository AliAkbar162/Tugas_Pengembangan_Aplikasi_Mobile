package com.example.demop4app.navigation

sealed class Screen(val route: String) {
    // Note App
    object NoteList : Screen("note_list")
    object Favorites : Screen("favorites")
    object Profile   : Screen("profile")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
    object AddNote : Screen("add_note")
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
    
    // AI Feature
    object AISummary : Screen("ai_summary/{noteId}") {
        fun createRoute(noteId: Int) = "ai_summary/$noteId"
    }

    // News App (Week 6)
    object NewsList : Screen("news_list")
    object NewsDetail : Screen("news_detail")

    // Settings (Week 7)
    object Settings : Screen("settings")
}
