package com.example.users.data.di

import android.content.Context
import androidx.room.Room
import com.example.users.data.local.dao.UserDao
import com.example.users.data.local.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): UserDatabase = Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "users_db"
    )
        .build()

    @Provides
    fun provideUserDao(db: UserDatabase): UserDao =
        db.userDao()

}
