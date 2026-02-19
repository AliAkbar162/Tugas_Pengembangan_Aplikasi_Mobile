package com.example.mynewsfeeder

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository {

    private val allNews = listOf(
        NewsItem(1,  "Kotlin 2.0 Dirilis dengan Fitur Baru",       "Kotlin 2.0 hadir dengan compiler K2 yang lebih cepat.",           NewsCategory.TEKNOLOGI),
        NewsItem(2,  "Tim Nasional Lolos ke Final",                 "Tim nasional berhasil mengalahkan lawan dengan skor 3-0.",         NewsCategory.OLAHRAGA),
        NewsItem(3,  "Pemilu Daerah Berlangsung Damai",             "Pemilihan kepala daerah berjalan lancar di seluruh wilayah.",     NewsCategory.POLITIK),
        NewsItem(4,  "Film Animasi Indonesia Raih Penghargaan",     "Film animasi karya anak bangsa meraih penghargaan internasional.",NewsCategory.HIBURAN),
        NewsItem(5,  "Vaksin Baru Berhasil Uji Klinis",             "Vaksin untuk penyakit tropis berhasil melewati uji klinis.",      NewsCategory.KESEHATAN),
        NewsItem(6,  "Startup AI Indonesia Dapat Pendanaan Besar",  "Startup kecerdasan buatan asal Jakarta mendapat investasi besar.",NewsCategory.TEKNOLOGI),
        NewsItem(7,  "Atlet Lari Indonesia Pecahkan Rekor",         "Pelari muda Indonesia memecahkan rekor Asia Tenggara.",           NewsCategory.OLAHRAGA),
        NewsItem(8,  "DPR Sahkan UU Perlindungan Data Pribadi",     "Undang-undang baru memberikan perlindungan lebih bagi warga.",    NewsCategory.POLITIK),
        NewsItem(9,  "Konser Musik Terbesar Hadir di Jakarta",      "Konser internasional akan digelar di Gelora Bung Karno.",         NewsCategory.HIBURAN),
        NewsItem(10, "Studi: Tidur Siang Tingkatkan Produktivitas", "Studi terbaru membuktikan manfaat tidur siang 20 menit.",         NewsCategory.KESEHATAN),
        NewsItem(11, "Google Umumkan Android 16",                   "Sistem operasi terbaru Android membawa perubahan besar.",        NewsCategory.TEKNOLOGI),
        NewsItem(12, "Liga Sepak Bola Nasional Musim Baru Dimulai", "Kompetisi sepak bola nasional musim 2025 resmi dibuka.",          NewsCategory.OLAHRAGA),
    )

    // Flow memancarkan berita satu per satu setiap 2 detik
    fun newsFlow(): Flow<NewsItem> = flow {
        val shuffled = allNews.shuffled()
        for (news in shuffled) {
            delay(2000)
            emit(news)
        }
    }

    // Suspend function untuk ambil detail berita
    suspend fun fetchNewsDetail(newsId: Int): NewsDetail? {
        delay(800)
        val news = allNews.find { it.id == newsId } ?: return null
        val authors = listOf("Andi Wijaya", "Siti Rahayu", "Budi Santoso", "Dewi Lestari")
        return NewsDetail(
            newsItem = news,
            fullContent = "${news.title}\n\n${news.summary}\n\nBerita ini termasuk kategori ${news.category.name}. Redaksi terus memantau perkembangan dan akan memberikan update terbaru secepatnya.",
            author = authors.random(),
            readTimeMinutes = (2..8).random()
        )
    }
}