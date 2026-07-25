# CreatorFlow AI — Native Android (Kotlin + Jetpack Compose + Firebase)

<div align="center">
  <h3>🚀 AI-Powered Content Creation Platform</h3>
  <p><strong>Create. Automate. Grow.</strong></p>
  <p>
    <strong>Kotlin</strong> • <strong>Jetpack Compose</strong> • <strong>Firebase</strong> • <strong>MVVM</strong> • <strong>Hilt</strong> • <strong>Razorpay</strong>
  </p>
</div>

---

## 🏗️ Architecture — Clean MVVM

```
app/src/main/java/com/creatorflow/ai/
├── CreatorFlowApp.kt          # Application (Hilt)
├── MainActivity.kt            # Single Activity
├── di/                        # Hilt DI Modules
├── data/
│   ├── model/                 # Domain models
│   ├── repository/            # Repository interfaces + impls
│   ├── remote/                # Firebase / Retrofit
│   └── local/                 # Room / DataStore
├── domain/
│   └── usecase/               # Business logic
└── ui/
    ├── theme/                 # Material 3 Theme (Dark+Light)
    ├── navigation/            # Navigation routes
    ├── components/            # Shared composables
    ├── CreatorFlowApp.kt      # Root NavHost
    └── screens/
        ├── splash/
        ├── auth/              # Login, SignUp, Forgot
        ├── dashboard/
        ├── youtube/
        ├── instagram/
        ├── aiscript/
        ├── thumbnail/
        ├── scheduler/
        ├── notifications/
        ├── subscription/
        ├── settings/
        └── admin/
```

## � Modules

| # | Module | Stack |
|---|--------|-------|
| 1 | Authentication | Firebase Auth + Google Sign-In + Email |
| 2 | Dashboard | Compose + Firestore real-time |
| 3 | AI Script Generator | ViewModel + AI API |
| 4 | AI Thumbnail Generator | Compose + Coil |
| 5 | YouTube Automation | Channel stats, video list |
| 6 | Instagram Automation | Profile stats, post grid |
| 7 | Content Scheduler | Calendar + scheduled posts |
| 8 | Analytics | Crashlytics + Firebase Analytics |
| 9 | Notifications | FCM + In-app |
| 10 | Subscription | Razorpay checkout + Plans |
| 11 | Settings | Dark Mode, profile, legal |
| 12 | Admin Panel | 6-tab: Overview, Users, Payments, Settlements, Content, Analytics |
| 13 | Firestore | Real-time data sync |
| 14 | Storage | Firebase Storage |
| 15 | Push Notifications | FCM |
| 16 | Crash Reporting | Firebase Crashlytics |
| 17 | Offline Support | Room + DataStore |
| 18 | Material 3 | Full M3 with dark/light |
| 19 | Production | ProGuard, R8, signing config |

## 🚀 Build

```bash
# Debug
./gradlew assembleDebug

# Release AAB
./gradlew bundleRelease

# Fastlane
cd android && fastlane beta
```

# GitHub Repo: https://github.com/awesomedude01ad-byte/creatorflow-ai
