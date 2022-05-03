package watermelon.focus.ui.page.page_starList

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
import watermelon.focus.ui.page.page_starList.StarListPageViewModel.StarListPageAction.SelectStar
import javax.inject.Inject

/**
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/27 23:19
 */
@HiltViewModel
class StarListPageViewModel @Inject constructor() : ViewModel() {
    var starListPageStates by mutableStateOf(StarListPageViewStates())
        private set

    @Inject
    lateinit var starDao: StarDao

    fun dispatch(action: StarListPageAction) {
        when (action) {
            is SelectStar -> starListPageStates =
                starListPageStates.copy(selectStar = action.todoStar)
            is StarListPageAction.RefreshStarList -> refreshStarList()
            is StarListPageAction.DeleteStar -> {
                deleteStar(action.todoStar)
            }
        }
    }

    private fun deleteStar(todoStar: TodoStar) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                starDao.deleteStar(todoStar)
                starDao.queryAllStar().collect {
                    it?.let { starListPageStates.starList.value = it }
                }
            }
        }
    }

    private fun refreshStarList() {
        viewModelScope.launch {
            starDao.queryAllStar().collect {
                if (it.isNotEmpty()){
                    starListPageStates.starList.value = it
                }
            }
        }
    }

    data class StarListPageViewStates(
        val starList: MutableState<List<TodoStar>> = mutableStateOf(listOf()),
        val selectStar: TodoStar = TodoStar(
            id = 1,
            name = "Start", color = 0xFF5B0FA5,
            reminderTime = 0L,
            reminderDate = "Right Now !", focusTime = 0L,
            description = "Focus ！"
        )
    )

    sealed class StarListPageAction {
        class SelectStar(val todoStar: TodoStar) : StarListPageAction()
        class DeleteStar(val todoStar: TodoStar) : StarListPageAction()
        object RefreshStarList : StarListPageAction()
    }
}