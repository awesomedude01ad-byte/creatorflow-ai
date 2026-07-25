# CreatorFlow AI — Production ProGuard

# ─── Kotlin ──────────────────────────────────────────
-keepattributes *Annotation*
-keep class kotlin.** { *; }
-dontwarn kotlin.**

# ─── Firebase ────────────────────────────────────────
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.google.android.gms.** { *; }

# ─── Razorpay ────────────────────────────────────────
-keep class com.razorpay.** { *; }
-dontwarn com.razorpay.**

# ─── Retrofit / OkHttp ───────────────────────────────
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature, Exceptions
-dontwarn okhttp3.**
-dontwarn okio.**

# ─── Moshi ───────────────────────────────────────────
-keep class com.creatorflow.ai.data.model.** { *; }

# ─── Room ────────────────────────────────────────────
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# ─── Coroutines ──────────────────────────────────────
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# ─── Hilt ────────────────────────────────────────────
-dontwarn dagger.hilt.**

# ─── Remove logging in release ───────────────────────
-assumenosideeffects class timber.log.Timber {
    public static void v(...);
    public static void d(...);
}
