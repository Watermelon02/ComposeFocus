package watermelon.focus.model.database

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import watermelon.focus.model.bean.TodoStar

/**
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/28 12:59
 */
@Database(entities = [TodoStar::class],version = 1,exportSchema = false)
abstract class StarDatabase : RoomDatabase() {
    abstract fun starDao(): StarDao

    companion object {
        @Volatile
        private var noteDatabase: StarDatabase? = null

        fun getInstance(context: Context): StarDatabase {
            if (noteDatabase == null) {
                synchronized(this) {
                    if (noteDatabase == null) noteDatabase =
                        Room.databaseBuilder(context, StarDatabase::class.java, "todo_database")
                            .build()
                }
            }
            return noteDatabase!!
        }
    }
}