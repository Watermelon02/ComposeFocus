package watermelon.focus.ui.widget

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * description ： NewStarPage中选择颜色的Button
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/1 14:38
 */
@Composable
fun ColorButton(color: Color, onClick: () -> Unit) {
    //两个颜色渐变动画
    val colorAnimation1 by rememberInfiniteTransition().animateColor(
        initialValue = color.copy(alpha = 0.3f),
        targetValue = color.copy(alpha = 0.65f),
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 4750 + 500 * color.alpha.toInt(),
                easing = FastOutLinearInEasing,
                delayMillis = 2730 * color.alpha.toInt()
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val colorAnimation2 by rememberInfiniteTransition().animateColor(
        initialValue = color.copy(alpha = 0.7f),
        targetValue = color.copy(alpha = 0.3f),
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 3371 + 500 * color.alpha.toInt(),
                easing = FastOutSlowInEasing,
                delayMillis = 2730 * color.alpha.toInt()
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    IconButton(onClick = { onClick() }) {
        Canvas(modifier = Modifier, onDraw = {
            //渐变色块
            drawCircle(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorAnimation2,
                        colorAnimation1
                    ), startY = size.height / 2 - 15.dp.value, endY = size.height / 2 + 15.dp.value
                ),
                radius = 30.dp.value,
                center = Offset(x = size.width / 2, y = size.height / 2)
            )
        })
    }
}