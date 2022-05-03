package watermelon.focus.ui.page.page_newStar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import watermelon.focus.model.bean.TodoStar
import watermelon.focus.ui.widget.ColorSelector
import watermelon.focus.ui.widget.Star
import watermelon.focus.ui.widget.TimeSelector

/**
 * description : StarList选择新增Star后的page
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/1 13:37
 */
@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun NewStarPage(
    sheetState: ModalBottomSheetState,
    onCreateStar: (TodoStar) -> Unit,
    viewModel: NewStarViewModel = hiltViewModel()
) {
    val viewModelStates = viewModel.starListPageStates
    //切换星球时的渐变动画
    val alphaTransition = animateFloatAsState(
        targetValue = if (viewModelStates.starColorChanged.value) 1f else 0f,
        finishedListener = { viewModel.dispatch(NewStarViewModel.NewStarPageAction.ColorChanged) },
        animationSpec = tween(500)
    )
    Column(
        modifier = Modifier
            .fillMaxHeight(0.7f)
            .background(Color(0xFFF8F8F8))
    ) {
        Row {
            Card(
                modifier = Modifier
                    .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 10.dp)
                    .height(200.dp)
                    .width(175.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {},
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .alpha(if (viewModelStates.starColorChanged.value) alphaTransition.value else 1f),
                    contentAlignment = Alignment.Center
                ) {
                    //预览star
                    Star(Color(viewModelStates.newStar.value.color))
                }

                //NewStarName
                TextField(
                    value = viewModelStates.newStar.value.name,
                    onValueChange = {
                        viewModelStates.newStar.value =
                            viewModelStates.newStar.value.copy(name = it)
                    },
                    textStyle = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .alpha(0.5f)
                        .padding(top = 150.dp, start = 50.dp, end = 50.dp)
                        .width(20.dp), colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color(0X80000000), cursorColor = Color(0X80000000)
                    )
                )
            }
            //色彩选择器
            ColorSelector(selectColorListener = {
                viewModel.dispatch(NewStarViewModel.NewStarPageAction.SelectColor(it))
            })
        }

        //时间选择器，设置newStar的reminderTime时间
        TimeSelector(viewModel = viewModel, onCreateStar = {
            viewModel.dispatch(NewStarViewModel.NewStarPageAction.AddStar(it))
        })

        //NewStar的备注,开始隐藏在下面，当modalBottomSheet展开
        AnimatedVisibility(visible = sheetState.currentValue == ModalBottomSheetValue.Expanded) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
            ) {
                TextField(
                    value = viewModelStates.newStar.value.description,
                    onValueChange = {
                        viewModelStates.newStar.value =
                            viewModelStates.newStar.value.copy(description = it)
                    },
                    textStyle = MaterialTheme.typography.body2,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color(0X80000000), cursorColor = Color(0X80000000),
                        textColor = Color(0X80000000)
                    )
                )
            }
        }
    }
}
