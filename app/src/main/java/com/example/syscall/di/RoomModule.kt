package com.example.syscall.di

import android.content.Context
import androidx.room.Room
import com.example.syscall.data.database.SyscallDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    const val CALL_DATABASE_NAME = "syscall_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, SyscallDataBase::class.java, CALL_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideSyscallDao(db: SyscallDataBase) = db.getSyscallDao()

}