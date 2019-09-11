package com.example.roomtest

import androidx.lifecycle.LiveData

class WordRepository(private val wordDao: Dao) {

    val allWords : LiveData<List<Word>> =
        wordDao.getAllWords()

    suspend fun insert(word: Word){
        wordDao.insert(word)
    }
}