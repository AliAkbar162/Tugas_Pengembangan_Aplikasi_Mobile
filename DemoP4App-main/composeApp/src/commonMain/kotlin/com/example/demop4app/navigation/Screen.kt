package com.example.demop4app.navigation

// Sealed class untuk semua route dalam aplikasi
// Best practice: terpusat di satu file
sealed class Screen(val route: String) {

    // ── Bottom Navigation Tabs ──────────────────────────────
    object NoteList : Screen("note_list")
    object Favorites : Screen("favorites")
    object Profile   : Screen("profile")

    // ── Detail / Action Screens ─────────────────────────────
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }

    object AddNote : Screen("add_note")

    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
}