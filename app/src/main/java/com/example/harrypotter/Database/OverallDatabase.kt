package com.example.harrypotter.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [FavoriteData::class, FavoriteDataSpell::class, FavoriteCreatureData::class, AnalyticsData::class, LoginData::class],
    version = 7
)
abstract class OverallDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDAO
    abstract fun favoriteDaoSpell(): FavoriteSpellDAO
    abstract fun favoriteDaoCreature(): FavoriteCreatureDAO

    abstract fun analyticsDao(): AnalyticsDAO

    abstract fun loginDao(): LoginDAO

    companion object {

        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE favoriteCreature (" +
                            "id TEXT PRIMARY KEY NOT NULL," +
                            "name TEXT," +
                            "house TEXT," +
                            "ancestry TEXT," +
                            "patronus TEXT," +
                            "image TEXT," +
                            "wizard INTEGER," +
                            "hogwartsStudent INTEGER," +
                            "hogwartsStaff INTEGER," +
                            "species TEXT)"
                )
                db.execSQL(
                    "CREATE TABLE favoriteSpell (" +
                            "id TEXT PRIMARY KEY NOT NULL," +
                            "name TEXT," +
                            "description TEXT)"
                )
            }
        }

        val migration_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE analyticsTable (" +
                            "id INTEGER PRIMARY KEY NOT NULL," +
                            "APIname TEXT," +
                            "isSuccess INTEGER," +
                            "responseTime INTEGER," +
                            "timeStamp INTEGER)"
                )
            }
        }

        val migration_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE loginTable (" +
                            "id INTEGER PRIMARY KEY NOT NULL," +
                            "name TEXT," +
                            "house TEXT," +
                            "wand TEXT," +
                            "patronus TEXT)"
                )
            }
        }

        val migration_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE loginTable ADD COLUMN role TEXT"
                )
            }
        }

        val migration_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE character (" +
                            "id TEXT PRIMARY KEY NOT NULL," +
                            "name TEXT," +
                            "house TEXT," +
                            "ancestry TEXT," +
                            "patronus TEXT," +
                            "image TEXT," +
                            "wizard INTEGER," +
                            "hogwartsStudent INTEGER," +
                            "hogwartsStaff INTEGER," +
                            "species TEXT)"
                )
            }
        }

        val migration_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "DROP TABLE character"
                )
            }
        }

        @Volatile
        private var INSTANCE: OverallDatabase? = null

        fun getDatabase(context: Context): OverallDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        OverallDatabase::class.java,
                        "overallDB"
                    ).addMigrations(
                        migration_1_2,
                        migration_2_3,
                        migration_3_4,
                        migration_4_5,
                        migration_5_6,
                        migration_6_7
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}