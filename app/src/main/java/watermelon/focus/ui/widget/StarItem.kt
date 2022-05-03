package watermelon.focus.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import watermelon.focus.R
import watermelon.focus.model.bean.TodoStar

/**
 * description ： TODO:类的作用
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/28 14:15
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StarItem(todoStar: TodoStar, clickable: (TodoStar) -> Unit) {

    val swipeState = rememberSwipeableState(initialValue = StarItemSwipeStatus.Close)
    Card(
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp, end = 10.dp)
            .height(200.dp)
            .width(175.dp)
            .offset(
                y = swipeState.offset.value.dp
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { clickable(todoStar) }
            .swipeable(
                state = swipeState, anchors = mapOf(
                    0f to StarItemSwipeStatus.Close,
                    -40.dp.value to StarItemSwipeStatus.Open
                ), thresholds = { from, to ->
                    if (from == StarItemSwipeStatus.Close) {
                        FractionalThreshold(0.3f)
                    } else FractionalThreshold(0.1f)
                }, orientation = Orientation.Vertical
            ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Star(Color(todoStar.color))
        //StarName
        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(top = 150.dp, start = 50.dp, end = 50.dp)
                .width(20.dp)
                .wrapContentHeight(), color = Color(0xFFF8F8F8)
        ) {
            Text(
                text = todoStar.name,
                style = MaterialTheme.typography.body2,
                fontFamily = FontFamily(Font(R.font.store_my_stamp_number)),
                textAlign = TextAlign.Center, modifier = Modifier.alpha(0.5f)
            )
        }
    }
}

enum class StarItemSwipeStatus {
    Close, Open
}