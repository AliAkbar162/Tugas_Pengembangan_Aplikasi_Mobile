package com.example.myprofileapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.data.ProfileUiState

private val Purple = Color(0xFF6200EE)
private val BackgroundLight = Color(0xFFF0F2F5)
private val BackgroundDark = Color(0xFF121212)
private val SurfaceLight = Color.White
private val SurfaceDark = Color(0xFF1E1E1E)
private val TextPrimaryDark = Color(0xFFE0E0E0)


@Composable
fun EditProfileScreen(
    uiState: ProfileUiState,
    onSave: (name: String, bio: String) -> Unit,
    onCancel: () -> Unit
) {
    var tempName by remember { mutableStateOf(uiState.name) }
    var tempBio  by remember { mutableStateOf(uiState.bio)  }

    val bgColor      = if (uiState.isDarkMode) BackgroundDark else BackgroundLight
    val surfaceColor = if (uiState.isDarkMode) SurfaceDark    else SurfaceLight
    val textColor    = if (uiState.isDarkMode) TextPrimaryDark else Color(0xFF222222)
    val labelColor   = if (uiState.isDarkMode) Color(0xFFAAAAAA) else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "✏️  Edit Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )

        Text(
            text = "Ubah nama dan bio profilmu",
            fontSize = 13.sp,
            color = labelColor,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                LabeledTextField(
                    label      = "Nama Lengkap",
                    value      = tempName,
                    onValueChange = { tempName = it },
                    placeholder   = "Masukkan nama lengkap",
                    isDarkMode    = uiState.isDarkMode
                )

                Spacer(modifier = Modifier.height(16.dp))

                LabeledTextField(
                    label         = "Bio",
                    value         = tempBio,
                    onValueChange = { tempBio = it },
                    placeholder   = "Ceritakan sedikit tentang dirimu...",
                    singleLine    = false,
                    minLines      = 3,
                    isDarkMode    = uiState.isDarkMode
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (tempName.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Purple.copy(alpha = 0.08f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text  = "Preview:",
                        fontSize = 11.sp,
                        color = Purple,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text  = "Halo, saya ${tempName}! 👋",
                        fontSize = 14.sp,
                        color = textColor,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSave(tempName, tempBio) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape  = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple),
            enabled = tempName.isNotBlank()
        ) {
            Text(
                text       = "💾  Simpan Perubahan",
                fontSize   = 15.sp,
                color      = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape  = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Purple)
        ) {
            Text(
                text       = "Batal",
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun LabeledTextField(
    label         : String,
    value         : String,
    onValueChange : (String) -> Unit,
    placeholder   : String = "",
    singleLine    : Boolean = true,
    minLines      : Int = 1,
    isDarkMode    : Boolean = false
) {
    val textColor       = if (isDarkMode) Color(0xFFE0E0E0) else Color(0xFF222222)
    val labelColor      = if (isDarkMode) Color(0xFFAAAAAA) else Color.Gray
    val containerColor  = if (isDarkMode) Color(0xFF2A2A2A) else Color(0xFFF9F9F9)

    Column {
        Text(
            text       = label,
            fontSize   = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color      = labelColor,
            modifier   = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = {
                Text(text = placeholder, color = Color.Gray, fontSize = 13.sp)
            },
            modifier   = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines   = minLines,
            shape      = RoundedCornerShape(10.dp),
            colors     = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Purple,
                unfocusedBorderColor = Color.LightGray,
                focusedTextColor     = textColor,
                unfocusedTextColor   = textColor,
                focusedContainerColor   = containerColor,
                unfocusedContainerColor = containerColor
            )
        )
    }
}