package com.gabriel.pocketstatement.di

import android.app.Application
import com.gabriel.pocketstatement.data.local.AppDatabase
import com.gabriel.pocketstatement.data.remote.GeminiApiService
import com.gabriel.pocketstatement.data.repository.ReceiptRepositoryImpl
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository
import com.gabriel.pocketstatement.domain.usecase.DeleteReceiptUseCase
import com.gabriel.pocketstatement.domain.usecase.GetReceiptByIdUseCase
import com.gabriel.pocketstatement.domain.usecase.GetReceiptsUseCase
import com.gabriel.pocketstatement.domain.usecase.GetSpendingByCategoryUseCase
import com.gabriel.pocketstatement.domain.usecase.ProcessReceiptTextUseCase
import com.gabriel.pocketstatement.domain.usecase.ReceiptUseCases
import com.gabriel.pocketstatement.domain.usecase.SaveReceiptUseCase
import com.gabriel.pocketstatement.domain.usecase.TextRecognitionUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return AppDatabase.getInstance(app)
    }


    @Provides
    @Singleton
    fun provideReceiptRepository(db: AppDatabase): ReceiptRepository {
        return ReceiptRepositoryImpl(db.receiptDao())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(gson: Gson): GeminiApiService {
        return GeminiApiService(gson)
    }


    @Provides
    @Singleton
    fun provideReceiptUseCases(repository: ReceiptRepository,geminiApiService: GeminiApiService): ReceiptUseCases {
        return ReceiptUseCases(
            getReceiptsUseCase = GetReceiptsUseCase(repository),
            deleteReceiptUseCase = DeleteReceiptUseCase(repository),
            saveReceiptUseCase = SaveReceiptUseCase(repository),
            getReceiptByIdUseCase = GetReceiptByIdUseCase(repository),
            textRecognitionUseCase = TextRecognitionUseCase(),
            processReceiptTextUseCase = ProcessReceiptTextUseCase(geminiApiService),
            getSpendingByCategoryUseCase = GetSpendingByCategoryUseCase(repository)
        )
    }
}