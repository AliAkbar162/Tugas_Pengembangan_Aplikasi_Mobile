package com.example.myprofileapp.data

data class ProfileUiState(
    // --- Data Profil ---
    val name: String = "Ali Akbar",
    val nim: String = "123140162",
    val title: String = "Mobile Developer",
    val bio: String = "Mahasiswa Teknik Informatika ITERA yang passionate " +
            "dalam pengembangan aplikasi mobile menggunakan " +
            "Kotlin Multiplatform & Compose.",
    val email: String = "ali.123140162@itera.ac.id",
    val phone: String = "+62 857 7183 7911",
    val location: String = "Lampung, Indonesia",
    val skills: List<String> = listOf("Kotlin", "Compose", "Android", "iOS", "KMP"),

    val isDarkMode: Boolean = false,
    val isEditMode: Boolean = false
)