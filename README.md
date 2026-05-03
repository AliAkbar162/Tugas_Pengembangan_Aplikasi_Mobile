## 🤖 Integrasi Gemini AI: Fitur Auto-Summary

Aplikasi ini telah diintegrasikan dengan Google Gemini AI untuk memberikan ringkasan otomatis pada catatan pengguna.

### 🌟 Fitur Utama
* **Auto-Summary**: Meringkas teks catatan yang panjang menjadi 2 kalimat singkat dalam Bahasa Indonesia.
* **Loading States**: UI yang responsif dengan indikator pemrosesan (ProgressBar).
* **Graceful Error Handling**: Pesan error yang informatif jika terjadi gangguan koneksi atau masalah API.

### 🛠️ Detail Teknis
* **Model**: `gemini-1.5-flash` (via Google AI Studio).
* **Service Layer**: Menggunakan `Ktor Client` dengan konfigurasi `v1beta` dan `encodedPath` untuk stabilitas URL API.
* **Architecture**: Mengikuti pola MVVM (ViewModel -> Repository -> Service).
* **DI**: Menggunakan `Koin` untuk manajemen dependensi HttpClient dan Service.

### 🧠 Prompt Engineering
Sistem prompt dirancang agar AI bertindak sebagai asisten penulis yang objektif:
> *"Sebagai asisten cerdas, ringkaslah teks berikut dalam maksimal 2 kalimat Bahasa Indonesia yang sangat jelas: [konten_catatan]"*

### ⚠️ Penanganan Error
Aplikasi menangani berbagai skenario error sesuai rubrik:
1. **Invalid API Key**: Mendeteksi error 401/403 dan meminta pengguna mengecek konfigurasi.
2. **Rate Limit**: Menangani error 429 jika kuota gratis telah mencapai batas.
3. **Model Not Found (404)**: Menggunakan sistem *fallback* yang akan mencoba model alternatif jika model utama tidak tersedia.
4. **Empty State**: Validasi input agar tidak mengirim teks kosong ke AI.

### ⚙️ Cara Konfigurasi
1. Dapatkan API Key dari [Google AI Studio](https://aistudio.google.com/).
2. Masukkan key tersebut ke file `composeApp/src/commonMain/kotlin/com/example/demop4app/data/ApiConfig.kt`:
   
