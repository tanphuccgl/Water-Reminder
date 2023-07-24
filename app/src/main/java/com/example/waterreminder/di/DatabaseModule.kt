package com.example.waterreminder.di

import android.content.Context
import androidx.room.Room
import com.example.waterreminder.database.WaterReminderDao
import com.example.waterreminder.database.WaterReminderDatabase
import com.example.waterreminder.database.cup.SwitchCupDao
import com.example.waterreminder.database.cup.SwitchCupDatabase
import com.example.waterreminder.database.reminder.ReminderDao
import com.example.waterreminder.database.reminder.ReminderDatabase
import com.example.waterreminder.database.sound.SoundDao
import com.example.waterreminder.database.sound.SoundDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): WaterReminderDatabase {
        return Room.databaseBuilder(
            appContext,
            WaterReminderDatabase::class.java,
            "WaterReminders"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideWaterReminderDao(waterReminderDatabase: WaterReminderDatabase): WaterReminderDao {
        return waterReminderDatabase.waterReminderDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase2(@ApplicationContext appContext: Context): ReminderDatabase {
        return Room.databaseBuilder(
            appContext,
            ReminderDatabase::class.java,
            "Reminders"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideReminderDao(reminderDatabase: ReminderDatabase): ReminderDao {
        return reminderDatabase.reminderDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase3(@ApplicationContext appContext: Context): SwitchCupDatabase {
        return Room.databaseBuilder(
            appContext,
            SwitchCupDatabase::class.java,
            "SwitchCups"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideSwitchCupDao(switchCupDatabase: SwitchCupDatabase): SwitchCupDao {
        return switchCupDatabase.switchCupDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase4(@ApplicationContext appContext: Context): SoundDatabase {
        return Room.databaseBuilder(
            appContext,
            SoundDatabase::class.java,
            "Sounds"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideSoundDao(soundDatabase: SoundDatabase): SoundDao {
        return soundDatabase.soundDao
    }
}