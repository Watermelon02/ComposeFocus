package watermelon.focus.model.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import watermelon.focus.model.bean.TodoStar

/**
 * description ： TODO:类的作用
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/28 13:02
 */
@Dao
interface StarDao {
    @Query("SELECT * FROM todo_star WHERE id = :id")
    fun queryStar(id: Long): Flow<TodoStar>

    @Query("SELECT * FROM todo_star")
    fun queryAllStar(): Flow<List<TodoStar>>

    @Delete
    fun deleteStar(todo: TodoStar)

    @Update
    fun updateStar(todo: TodoStar)

    @Insert
    fun insertStar(todo: TodoStar)

    @Query("SELECT * FROM todo_star WHERE reminder_date = :reminderDate")
    fun queryStarListByDate(reminderDate: String): Flow<List<TodoStar>>
}