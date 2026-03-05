# 📱 SMKN 1 GEMPOL — Android LMS App v1.1

Aplikasi Android kiosk mode untuk **SMK Negeri 1 Gempol** yang menjalankan LMS.

---

## ✨ Fitur Baru v1.1
- 🔄 **Tombol Refresh** di header bar (animasi rotasi)
- 📲 **Pull-to-Refresh** — tarik layar ke bawah untuk memuat ulang
- Spinner warna biru sesuai tema sekolah

---

## 🚀 Cara Build APK — 3 Metode

### ✅ Metode 1: Android Studio (Termudah)
1. Install [Android Studio](https://developer.android.com/studio)
2. Buka folder `SMKN1Gempol_v2/` → File → Open
3. Tunggu Gradle sync selesai
4. Edit `local.properties` → sesuaikan `sdk.dir` ke lokasi Android SDK Anda
5. Klik **Build → Build Bundle(s)/APK(s) → Build APK(s)**
6. APK ada di: `app/build/outputs/apk/debug/app-debug.apk`

---

### ✅ Metode 2: GitHub Actions (Otomatis, Gratis)
1. Buat akun [GitHub](https://github.com) (gratis)
2. Buat repository baru → upload semua file project ini
3. Masuk ke tab **Actions** → klik **Build APK** → **Run workflow**
4. Tunggu ~5 menit → klik hasil build → download `SMKN1Gempol-debug.zip`
5. Ekstrak → dapatkan `app-debug.apk` ✅

> 💡 File `.github/workflows/build_apk.yml` sudah ada di project ini!

---

### ✅ Metode 3: Build via Terminal (Linux/Mac)
```bash
# Pastikan sudah install Android Studio dan set ANDROID_HOME
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools

cd SMKN1Gempol_v2
chmod +x gradlew
./gradlew assembleDebug

# APK output:
ls app/build/outputs/apk/debug/app-debug.apk
```

---

## 📦 Struktur Project
```
SMKN1Gempol_v2/
├── .github/workflows/build_apk.yml  ← GitHub Actions auto-build
├── app/src/main/
│   ├── AndroidManifest.xml
│   ├── java/.../
│   │   ├── SplashActivity.java    ← Landing page animasi
│   │   ├── MainActivity.java      ← WebView + refresh + kiosk
│   │   └── BootReceiver.java      ← Auto-start saat boot
│   └── res/
│       ├── layout/activity_main.xml   ← SwipeRefreshLayout + tombol refresh
│       ├── anim/                      ← 9 animasi XML
│       └── drawable/                  ← Tema warna navy-biru
├── gradle/wrapper/
├── gradlew
├── local.properties               ← Edit sdk.dir sesuai komputer Anda
└── README.md
```

---

## 🌐 URL Konfigurasi
| Prioritas | URL |
|-----------|-----|
| 1 (Utama) | `https://lms.semakinpol.my.id` |
| 2 (Fallback) | `http://192.168.1.100` |

Edit di `MainActivity.java` baris `PRIMARY_URL` dan `FALLBACK_URL`.

---

## 🔒 Kiosk Mode
- Tombol **Home** & **Recent** diblokir
- **Back** hanya navigasi web (tidak keluar)
- **Auto-start** saat device dinyalakan
- Tombol **Exit** selalu tersedia dengan konfirmasi

---

**Versi**: 1.1 | **Min SDK**: Android 5.0 | **Target**: Android 14
