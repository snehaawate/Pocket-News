package com.example.pocketnews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pocketnews.data.local.dao.SourceDao
import com.example.pocketnews.data.local.dao.TopHeadlinesDao
import com.example.pocketnews.data.local.entity.Article
import com.example.pocketnews.data.local.entity.NewsSources

@Database(
    entities = [Article::class, NewsSources::class],
    version = 1,
    exportSchema = false
)
abstract class NewsAppDatabase : RoomDatabase() {

    abstract fun topHeadlinesDao(): TopHeadlinesDao

    abstract fun newsSourceDao(): SourceDao

}