package com.example.roomtest

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Word::class), version = 1)
public abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDataBase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "MyKotlinDb"
                ).addCallback(WordRoomDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class WordRoomDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { it ->
                scope.launch {
                    populateDatabase(it.wordDao())
                }
            }

        }

        suspend fun populateDatabase(wordDao: Dao) {
            wordDao.deleteAll()
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World")
            wordDao.insert(word)
            word = Word("Come")
            wordDao.insert(word)
            word = Word("Get")
            wordDao.insert(word)
            word = Word("Me")
            wordDao.insert(word)
            word = Word("If")
            wordDao.insert(word)
            word = Word("You")
            wordDao.insert(word)
            word = Word("Can")
            wordDao.insert(word)
        }
    }
}