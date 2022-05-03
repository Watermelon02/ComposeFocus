package watermelon.focus.model.bean

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * description ： 代办事项类
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/27 23:27
 * @param name 标题
 * @param reminderTime 提醒时间，用于service后台提醒
 * @param reminderDate 提醒日期，用于根据日期查找显示对应日期的所有Todo
 * @param focusTime 已经专注于该星球的事件
 * @param description 备注
 */
@Entity(tableName = "todo_star")
data class TodoStar(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    var name: String,
    @SerializedName("color")
    var color: Long = Color.White.value.toLong(),
    @ColumnInfo(name = "reminder_time")
    val reminderTime: Long,
    @ColumnInfo(name = "reminder_date")
    var reminderDate: String,
    @ColumnInfo(name = "focus_time")
    val focusTime: Long,
    var description: String
) : Serializable