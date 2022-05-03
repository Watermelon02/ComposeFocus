package watermelon.focus.ui.page.page_main

import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import watermelon.focus.model.bean.TodoStar
import watermelon.focus.model.database.StarDao
import javax.inject.Inject

/**
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/26 09:32
 */
@OptIn(ExperimentalMaterialApi::class)
@HiltViewModel
class MainPageViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TOTAL_MINUTES: Int = 720//12*60 min
    }

    @Inject
    lateinit var starDao: StarDao
    var mainPageViewStates by mutableStateOf(MainPageViewState())
        private set

    fun dispatch(action: MainPageAction) {
        when (action) {
            is MainPageAction.DegreeUpdate -> degreeUpdate(action.degree)
            is MainPageAction.SelectStar -> selectStar(action.todoStar)
            is MainPageAction.StarChanged -> mainPageViewStates.starChanged.value = false
            is MainPageAction.NewStarPage -> mainPageViewStates.sheetPage.value =
                BottomSheetPage.NewStarPage
            is MainPageAction.BackToStarList -> mainPageViewStates.sheetPage.value =
                BottomSheetPage.StarListPage
            is MainPageAction.AddStar -> mainPageViewStates.addStar.value = true
            is MainPageAction.RefreshStar -> mainPageViewStates.refreshStarList.value++
            is MainPageAction.CountdownStart->countDownStart()
            is MainPageAction.Countdown -> countDown()
            is MainPageAction.InitSelectedStar -> initSelectedStar()
        }
    }

    private fun initSelectedStar() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                starDao.queryAllStar().collect {
                    if (it.isNotEmpty()){
                        mainPageViewStates.selectedStar = it[0]
                    }
                }
            }
        }
        mainPageViewStates.starChanged.value = true
    }

    private fun countDownStart(){
        mainPageViewStates.countdownStart.value = true
    }

    private fun countDown() {
        val diffMinutes = 1f / (60 * 100)
        val diffDegree = 2f / 100
        var countdownTime = 0
        mainPageViewStates.isCountdown.value = true
        viewModelScope.launch {
            while (mainPageViewStates.settingMinutes > 0) {
                //每过一分钟保存star的focus状态
                if (countdownTime == 60000) {
                    saveStarFocusTime()
                    countdownTime = 0
                }
                mainPageViewStates.draggableDegree.value =
                    mainPageViewStates.draggableDegree.value - diffDegree
                mainPageViewStates =
                    mainPageViewStates.copy(settingMinutes = mainPageViewStates.settingMinutes - diffMinutes)
                countdownTime += 10
                delay(10)
            }
            mainPageViewStates.isCountdown.value = false
            mainPageViewStates.countdownStart.value = false
        }
    }

    private fun saveStarFocusTime() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mainPageViewStates.selectedStar =
                    mainPageViewStates.selectedStar.copy(focusTime = mainPageViewStates.selectedStar.focusTime + 1)
                starDao.updateStar(mainPageViewStates.selectedStar)
            }
        }
    }


    //更改选中的star,设置starChanged,以播放星球切换动画
    private fun selectStar(todoStar: TodoStar) {
        mainPageViewStates = mainPageViewStates.copy(selectedStar = todoStar)
        mainPageViewStates.starChanged.value = true
    }

    /**Clock的DraggableDegree变化后，更改打卡时间及显示的时间字符*/
    private fun degreeUpdate(degree: Float) {
        if (!mainPageViewStates.isCountdown.value) {
            mainPageViewStates =
                mainPageViewStates.copy(settingMinutes = degree * TOTAL_MINUTES / 360)
        }
    }

    sealed class MainPageAction {
        class DegreeUpdate(val degree: Float) : MainPageAction()
        class SelectStar(val todoStar: TodoStar) : MainPageAction()
        object CountdownStart:MainPageAction()
        object Countdown : MainPageAction()

        //星球切换动画播放完毕
        object StarChanged : MainPageAction()

        //进入NewStarPage界面
        object NewStarPage : MainPageAction()

        //新增星球
        object AddStar : MainPageAction()

        //保存新星球，或是侧滑返回StarListPage
        object BackToStarList : MainPageAction()

        object RefreshStar : MainPageAction()
        object InitSelectedStar : MainPageAction()
    }

    data class MainPageViewState(
        val sheetState: ModalBottomSheetState =
            ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
        //ModalBottomSheetPage要显示的界面
        val sheetPage: MutableState<BottomSheetPage> = mutableStateOf(BottomSheetPage.StarListPage),
        val draggableDegree: MutableState<Float> = mutableStateOf(0f),
        val draggableState: DraggableState = DraggableState(onDelta = {
            draggableDegree.value = (draggableDegree.value + it / 10).coerceIn(0f, 270f)
        }),
        //用户设置的打卡时长
        val settingMinutes: Float = 0f,
        //当前选中要Focus的star
        var selectedStar: TodoStar = TodoStar(
            id = 1,
            name = "Star", color = 0xFF000000,
            reminderTime = 0L,
            reminderDate = "Today", focusTime = 0L,
            description = "remarks"
        ),
        val starChanged: MutableState<Boolean> = mutableStateOf(false),
        //新增star
        val addStar: MutableState<Boolean> = mutableStateOf(false),
        //当新增star后，自增
        val refreshStarList: MutableState<Int> = mutableStateOf(0),
        val isCountdown: MutableState<Boolean> = mutableStateOf(false),
        val countdownStart: MutableState<Boolean> = mutableStateOf(false)
    )

    sealed class BottomSheetPage {
        object StarListPage : BottomSheetPage()
        object NewStarPage : BottomSheetPage()
    }
}