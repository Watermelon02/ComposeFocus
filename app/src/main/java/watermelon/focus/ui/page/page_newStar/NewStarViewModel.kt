package watermelon.focus.ui.page.page_newStar

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import watermelon.focus.model.bean.TodoStar
import watermelon.focus.model.database.StarDao
import java.util.*
import javax.inject.Inject

/**
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/1 15:59
 */
@HiltViewModel
class NewStarViewModel @Inject constructor() : ViewModel() {
    var starListPageStates by mutableStateOf(StarListPageViewStates())
        private set
    private val calendar1 = Calendar.getInstance()
    private val calendar2 = Calendar.getInstance()
    var year by mutableStateOf(calendar2[Calendar.YEAR])
    var month by mutableStateOf(calendar2[Calendar.MONTH])
    var day by mutableStateOf(calendar2[Calendar.DATE])
    var days by mutableStateOf(getMonthDays(calculateDiffMonth(year, month)))

    @Inject
    lateinit var starDao: StarDao

    fun dispatch(action: NewStarPageAction) {
        when (action) {
            is NewStarPageAction.SelectColor -> {
                starListPageStates.newStar.value =
                    starListPageStates.newStar.value.copy(color = action.color)
                starListPageStates.starColorChanged.value = true
            }
            is NewStarPageAction.ColorChanged -> starListPageStates.starColorChanged.value = false

            is NewStarPageAction.AddStar -> addStar(action.date)
        }
    }

    private fun addStar(date: String) {
        viewModelScope.launch {
            starListPageStates.newStar.value =
                starListPageStates.newStar.value.copy(reminderDate = date)
            withContext(Dispatchers.IO) {
                starDao.insertStar(starListPageStates.newStar.value)
            }
        }
    }

    data class StarListPageViewStates(
        val newStar: MutableState<TodoStar> = mutableStateOf(
            TodoStar(
                id = 0,
                name = "Name", color = 0xFF5B0FA5,
                reminderTime = 0L,
                reminderDate = "Today", focusTime = 0L,
                description = "description"
            )
        ),
        //NewStarPage改变颜色后的动画标志位
        val starColorChanged: MutableState<Boolean> = mutableStateOf(false)
    )

    sealed class NewStarPageAction {
        //NewStarPage中选择颜色
        class SelectColor(val color: Long) : NewStarPageAction()
        class AddStar(val date:String) : NewStarPageAction()

        //星球颜色改变动画播放完毕
        object ColorChanged : NewStarPageAction()
    }

    @SuppressLint("SimpleDateFormat")
    private fun calculateDiffMonth(
        year: Int,
        month: Int
    ): Int {
        return if (this.month > month) {
            -((this.month - month) + 12 * (this.year - year))
        } else {
            -((this.month + 12 - month) + 12 * (this.year - year - 1))
        }
    }

    /**@param diff 要计算的月份与当前月份的差*/
    private fun getMonthDays(diff: Int): List<Int> {
        val mDays = ArrayList<Int>()
        var diffYear = (calendar1[Calendar.MONTH] + diff) / 12
        val diffMonth = diff % 12
        if (calendar1[Calendar.MONTH] + diff < 0) diffYear += -1
        if (calendar1[Calendar.MONTH] + diff > 12) diffYear += 1
        calendar1.roll(Calendar.YEAR, diffYear)
        calendar1.roll(Calendar.MONTH, diffMonth)
        calendar1[Calendar.DATE] = 1 //把日期设置为当月第一天
        calendar1.roll(Calendar.DATE, -1) //日期回滚一天，也就是最后一天
        for (i in 1..calendar1[Calendar.DATE]) {
            mDays.add(i)
        }
        return mDays.toList()
    }
}