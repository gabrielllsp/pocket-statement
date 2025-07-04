package com.gabriel.pocketstatement.di

import android.app.Application
import com.gabriel.pocketstatement.data.local.AppDatabase
import com.gabriel.pocketstatement.data.repository.ReceiptRepositoryImpl
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository
import com.gabriel.pocketstatement.domain.usecase.DeleteReceiptUseCase
import com.gabriel.pocketstatement.domain.usecase.GetReceiptByIdUseCase
import com.gabriel.pocketstatement.domain.usecase.GetReceiptsUseCase
import com.gabriel.pocketstatement.domain.usecase.ReceiptUseCases
import com.gabriel.pocketstatement.domain.usecase.SaveReceiptUseCase
import com.gabriel.pocketstatement.domain.usecase.TextRecognitionUseCase
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
    fun provideReceiptUseCases(repository: ReceiptRepository): ReceiptUseCases {
        return ReceiptUseCases(
            getReceiptsUseCase = GetReceiptsUseCase(repository),
            deleteReceiptUseCase = DeleteReceiptUseCase(repository),
            saveReceiptUseCase = SaveReceiptUseCase(repository),
            getReceiptByIdUseCase = GetReceiptByIdUseCase(repository),
            textRecognitionUseCase = TextRecognitionUseCase()
        )
    }
}