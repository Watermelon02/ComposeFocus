package watermelon.focus.ui.page.page_starList

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import watermelon.focus.R
import watermelon.focus.model.bean.TodoStar
import watermelon.focus.ui.widget.DeleteButton
import watermelon.focus.ui.widget.NewStarItem
import watermelon.focus.ui.widget.StarIconButton
import watermelon.focus.ui.widget.StarItem

/**
 * description ： MainPage中ModalBottomSheetLayout中的todoList界面
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/27 22:59
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StarListPage(
    sheetState: ModalBottomSheetState,
    selectStarListener: (TodoStar) -> Unit,
    onAddStar: () -> Unit,
    viewModel: StarListPageViewModel = hiltViewModel(),
    refreshStarList: MutableState<Int>
) {
    val viewPageStates = viewModel.starListPageStates
    Column(
        modifier = Modifier
            .fillMaxHeight(0.7f)
            .background(Color(0xFFF8F8F8))
    ) {
        //存储最开始的starList,在AnimatedVisibility处作判断是否播放删除动画
        var lastTodoList = remember { viewPageStates.starList.value }
        //当refreshStarList=0时，lastTodoList中为默认空List，在首次访问数据库后，才获取到真正的全部stars
        if (refreshStarList.value == 1) lastTodoList = viewPageStates.starList.value
        //侧滑星球栏
        LazyRow(modifier = Modifier
            .fillMaxHeight(0.35f)
            .fillMaxWidth(), content = {
            for (todo in lastTodoList) {
                //已有星球列表
                item {
                    AnimatedVisibility(
                        visible = viewPageStates.starList.value.contains(todo),
                        exit = fadeOut(
                            targetAlpha = 0f,
                            animationSpec = tween(durationMillis = 600)
                        )
                    ) {
                        Box(modifier = Modifier.wrapContentSize()) {//将删除按钮和StarItem重叠
                            DeleteButton(color = Color(todo.color), onClick = {
                                viewModel.dispatch(
                                    StarListPageViewModel.StarListPageAction.DeleteStar(todo)
                                )
                            })
                            StarItem(
                                todoStar = todo,
                                clickable = {
                                    viewModel.dispatch(
                                        StarListPageViewModel.StarListPageAction.SelectStar(it)
                                    )
                                })
                        }
                    }
                }
            }
            //新增StarItem,点击进入NewStarPage
            item {
                NewStarItem { onAddStar() }
            }
        })
        //selectStar的信息
        SelectStarInformation(viewPageStates.selectStar, selectStar = selectStarListener)
        //selectStar的备注,开始隐藏在下面，当modalBottomSheet展开
        AnimatedVisibility(visible = sheetState.currentValue == ModalBottomSheetValue.Expanded) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                backgroundColor = Color.White
            ) {
                Text(
                    text = viewPageStates.selectStar.description,
                    fontFamily = FontFamily(
                        Font(R.font.store_my_stamp_number)
                    ), modifier = Modifier
                        .alpha(0.5f)
                        .padding(20.dp)
                )
            }
        }
    }
    //每次打开先进行一次刷新
    LaunchedEffect(null) { refreshStarList.value++ }
    //当refreshStarList变化时，刷新
    LaunchedEffect(key1 = refreshStarList.value, block = {
        viewModel.dispatch(StarListPageViewModel.StarListPageAction.RefreshStarList)
    })
}

@Composable
fun SelectStarInformation(todoStar: TodoStar, selectStar: (TodoStar) -> Unit) {
    Row(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxHeight(0.35f)
                .width(280.dp)
                .padding(10.dp),
            backgroundColor = Color.White
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(start = 20.dp)
            ) {
                Text(
                    text = "StarName   :  ${todoStar.name}",
                    fontFamily = FontFamily(
                        Font(R.font.store_my_stamp_number)
                    )
                )
                Text(
                    text = "DateTime  :  ${todoStar.reminderDate}",
                    fontFamily = FontFamily(
                        Font(R.font.store_my_stamp_number)
                    )
                )
                Text(
                    text = "Focused     :  ${todoStar.focusTime} minutes",
                    fontFamily = FontFamily(
                        Font(R.font.store_my_stamp_number)
                    )
                )
            }
        }
        Card(
            shape = RoundedCornerShape(20.dp), modifier = Modifier
                .fillMaxHeight(0.35f)
                .width(100.dp)
                .padding( top = 10.dp, bottom = 10.dp), backgroundColor = Color.White
        ) {
            //点击该IconButton选择当前Star为要Focus的Star，播放动画
            StarIconButton(
                modifier = Modifier, onSelectStar = { selectStar(todoStar) })
        }
    }
}
