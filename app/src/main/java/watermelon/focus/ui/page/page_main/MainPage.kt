package watermelon.focus.ui.page.page_main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import watermelon.focus.ui.widget.*

/**
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/26 22:39
 */
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainPage(navController: NavController, viewModel: MainPageViewModel) {
    val mainPageViewStates = viewModel.mainPageViewStates
    //用于启动modalBottomSheet
    val scope = rememberCoroutineScope()
    //切换星球时的渐变动画
    val alphaTransition = animateFloatAsState(
        targetValue = if (mainPageViewStates.starChanged.value) 1f else 0f,
        finishedListener = { viewModel.dispatch(MainPageViewModel.MainPageAction.StarChanged) },
        animationSpec = tween(1000)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.Center
    ) {
        //开始倒计时时让卫星消失再浮现，以掩盖突然地位移
        var flag by remember { mutableStateOf(false) }
        val countdownAlpha1 by animateFloatAsState(
            targetValue = if (mainPageViewStates.countdownStart.value) 0f else 1f, animationSpec = tween(1000),
            finishedListener = {
                flag = true
                viewModel.dispatch(MainPageViewModel.MainPageAction.Countdown)
            })
        val countdownAlpha2 by animateFloatAsState(
            targetValue = if (flag) 1f else 0f,
            animationSpec = tween(1000),
        )
        Clock(
            draggableDegree = mainPageViewStates.draggableDegree,
            draggableState = mainPageViewStates.draggableState,
            dragStoppedListener = {
                viewModel.dispatch(
                    MainPageViewModel.MainPageAction.DegreeUpdate(
                        mainPageViewStates.draggableDegree.value
                    )
                )
            },
            settingMinutes = viewModel.mainPageViewStates.settingMinutes,
            mainPageViewStates.isCountdown.value, if (flag)countdownAlpha2 else countdownAlpha1
        )
        TimeText(
            settingMinutes = mainPageViewStates.settingMinutes,
            draggableDegree = mainPageViewStates.draggableDegree.value,
            isCountdown = mainPageViewStates.isCountdown.value
        )
        Signature(text = "Focus")
        //为star增加点击事件和取消点击波纹,点击后通过协程展开ModalBottomSheet
        Box(
            modifier = Modifier
                .size(200.dp)
                .alpha(if (mainPageViewStates.starChanged.value) alphaTransition.value else 1f)
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = {
                        //弹出ModalBottomSheet
                        scope.launch { viewModel.mainPageViewStates.sheetState.show() }
                    }, onLongPress = {
                        //开始倒计时
                        viewModel.dispatch(MainPageViewModel.MainPageAction.CountdownStart)
                    })
                },
            contentAlignment = Alignment.Center
        ) {
            Star(Color(viewModel.mainPageViewStates.selectedStar.color))
        }
        //StarName
        StarName(viewModel.mainPageViewStates.selectedStar.name)
    }
    //默认展示列表中的第一课星球
    LaunchedEffect(Unit, block = {
        viewModel.dispatch(MainPageViewModel.MainPageAction.InitSelectedStar)
    })
}



