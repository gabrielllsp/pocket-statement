package com.gabriel.pocketstatement.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ReceiptEntity::class],
    version = 2, // AUMENTE A VERSÃO DE 1 PARA 2
    exportSchema = false
)
// --- FIM DA CORREÇÃO ---
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun receiptDao(): ReceiptDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pocket_statement_db"
                )
                    // INÍCIO DA SEGUNDA PARTE DA CORREÇÃO
                    // Em um app de produção, você criaria uma 'Migration'.
                    // Para desenvolvimento, podemos simplesmente destruir e recriar o banco.
                    .fallbackToDestructiveMigration()
                    // FIM DA SEGUNDA PARTE
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}