package com.creatorflow.ai.di

import android.app.NotificationManager
import android.content.Context
import com.creatorflow.ai.data.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides @Singleton
    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides @Singleton
    fun provideMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides @Singleton
    fun provideAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Provides @Singleton
    fun provideCrashlytics(): FirebaseCrashlytics = Firebase.crashlytics

    @Provides @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository = AuthRepositoryImpl(auth)

    @Provides @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository = UserRepositoryImpl(firestore)

    @Provides @Singleton
    fun provideScriptRepository(firestore: FirebaseFirestore): ScriptRepository = ScriptRepositoryImpl(firestore)

    @Provides @Singleton
    fun providePaymentRepository(firestore: FirebaseFirestore): PaymentRepository = PaymentRepositoryImpl(firestore)

    @Provides @Singleton
    fun provideSubscriptionRepository(): SubscriptionRepository = SubscriptionRepositoryImpl()

    @Provides @Singleton
    fun provideNotificationRepository(firestore: FirebaseFirestore): NotificationRepository = NotificationRepositoryImpl(firestore)

    @Provides @Singleton
    fun provideNotificationManager(app: android.app.Application): NotificationManager =
        app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides @Singleton
    fun provideAnalyticsRepository(analytics: FirebaseAnalytics): AnalyticsRepository = AnalyticsRepositoryImpl(analytics)
}