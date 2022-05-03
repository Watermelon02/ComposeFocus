package watermelon.focus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import watermelon.focus.service.StarService
import watermelon.focus.ui.page.page_countdown.CountDownPageViewModel
import watermelon.focus.ui.page.page_countdown.CountdownPage
import watermelon.focus.ui.page.page_main.MainPage
import watermelon.focus.ui.page.page_main.MainPageViewModel
import watermelon.focus.ui.page.page_main.widget.MainPageBottomBar
import watermelon.focus.ui.page.page_newStar.NewStarPage
import watermelon.focus.ui.page.page_starList.StarListPage

@AndroidEntryPoint
@Route(path = "/main/mainPageActivity")
class MainPageActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serviceIntent = Intent(this, StarService::class.java)
        this.startService(serviceIntent)
        setContent {
            Scaffold(
                content = {
                    MainPageContent()
                },
                bottomBar = {
                    MainPageBottomBar()
                }
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun MainPageContent() {
        //先获取MainPageViewModel,以获取其中的sheetState
        val mainPageViewModel: MainPageViewModel = hiltViewModel()
        val sheetState =
            mainPageViewModel.mainPageViewStates.sheetState
        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(30.dp),
            sheetState = sheetState,
            sheetContent = {
                //星球选择列表page
                if (mainPageViewModel.mainPageViewStates.sheetPage.value is MainPageViewModel.BottomSheetPage.StarListPage) {
                    StarListPage(
                        sheetState = mainPageViewModel.mainPageViewStates.sheetState,
                        selectStarListener = {
                            mainPageViewModel.dispatch(
                                MainPageViewModel.MainPageAction.SelectStar(it)
                            )
                        },
                        onAddStar = { mainPageViewModel.dispatch(MainPageViewModel.MainPageAction.NewStarPage) },
                        refreshStarList = mainPageViewModel.mainPageViewStates.refreshStarList
                    )
                } else {//新增星球page
                    NewStarPage(
                        sheetState = sheetState,
                        onCreateStar = { MainPageViewModel.MainPageAction.AddStar })
                }
            }
        ) {
            val navController = rememberNavController()
            val countdownViewModel = remember { CountDownPageViewModel() }
            //Navigation
            NavHost(navController = navController, startDestination = "page_main") {
                composable("page_main") { MainPage(navController, mainPageViewModel) }
                composable(
                    route = "page_countdown/{settingMinutes}",
                    arguments = listOf(navArgument("settingMinutes") {
                        type = NavType.FloatType
                        defaultValue = 0f
                    }
                    )
                ) {
                    val settingSeconds = it.arguments?.getFloat("settingMinutes")?.times(60)
                    //倒计时时间不为空，导航到启动CountdownPage
                    settingSeconds?.let { settingSeconds ->
                        CountdownPage(
                            countdownSeconds = settingSeconds.toInt(),
                            viewModel = countdownViewModel
                        )
                    }
                }
            }
        }
        //modalBottomBar不会自动在侧滑后退出，所以用BackHandler处理
        val scope = rememberCoroutineScope()
        BackHandler(
            enabled = (sheetState.currentValue == ModalBottomSheetValue.HalfExpanded
                    || sheetState.currentValue == ModalBottomSheetValue.Expanded),
            onBack = {
                //如果当前为StarListPage，则隐藏BottomSheet
                if (mainPageViewModel.mainPageViewStates.sheetPage.value is MainPageViewModel.BottomSheetPage.StarListPage) {
                    scope.launch { sheetState.hide() }
                } else {
                    mainPageViewModel.dispatch(MainPageViewModel.MainPageAction.BackToStarList)
                    //退回到starListPage后刷新数据
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            mainPageViewModel.dispatch(MainPageViewModel.MainPageAction.RefreshStar)
                        }
                    }
                }
            }
        )
    }

}