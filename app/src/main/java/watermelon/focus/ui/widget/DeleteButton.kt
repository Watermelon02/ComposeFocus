package watermelon.focus.ui.widget

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * description ： TODO:类的作用
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/2 12:38
 */
@Composable
fun DeleteButton(color: Color, onClick: () -> Unit) {
    val colorAnimation1 by rememberInfiniteTransition().animateColor(
        initialValue = color.copy(alpha = 0.35f),
        targetValue = color.copy(alpha = 0.75f),
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
        initialValue = color.copy(alpha = 0.9f),
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
    Card(
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp, end = 10.dp)
            .height(200.dp)
            .width(175.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        IconButton(onClick = onClick) {
            Canvas(modifier = Modifier
                .padding(start = 10.dp, top = 40.dp, end = 10.dp)
                .height(170.dp)
                .width(175.dp), onDraw = {
                //渐变色块
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorAnimation2,
                            colorAnimation1
                        ),
                        start = Offset(0f,0f),end = Offset(400.dp.value,400.dp.value),
                    ),
                    radius = 400.dp.value,
                    center = Offset(x = size.width / 2, y = size.height / 2)
                )
            })
        }
    }
}