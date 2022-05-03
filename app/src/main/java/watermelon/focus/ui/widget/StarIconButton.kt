package watermelon.focus.ui.widget

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import watermelon.focus.R


/**
 * description ： 带有动画的IconButton,在StarListPage和NewStarPage中使用
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/1 16:40
 */
@Composable
fun StarIconButton(onSelectStar: () -> Unit,modifier: Modifier){
    val isClicked = remember { mutableStateOf(false) }
    val offsetAnimation = animateDpAsState(
        targetValue = if (isClicked.value) 10.dp else 0.dp,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        finishedListener = {
            isClicked.value = false
        })
    val scaleAnimation = animateFloatAsState(
        targetValue = if (isClicked.value) 1.3f else 1f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        finishedListener = {
            isClicked.value = false
        })
    MyIconButton(
        onClick = {
            onSelectStar()
            isClicked.value = true
        },
        modifier = modifier.alpha(0.5f)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.button_choose_star),
            contentDescription = "",
            modifier = Modifier.offset(x = -offsetAnimation.value, y = offsetAnimation.value)
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.button_choose_line),
            contentDescription = "",
            modifier = Modifier
                .offset(
                    x = -offsetAnimation.value / 2,
                    y = offsetAnimation.value / 2
                )
                .scale(scaleAnimation.value)
        )
    }
}