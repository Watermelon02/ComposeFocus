package watermelon.focus.ui.widget

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * description ： NewStarItem中的Star
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/1 11:14
 */
@Composable
fun NewStar() {
    val vector = ImageVector.vectorResource(id = watermelon.focus.R.drawable.ic_star)
    val painter = rememberVectorPainter(image = vector)
    val verticalShakeTransition by rememberInfiniteTransition().animateFloat(
        initialValue = -40.dp.value * Random(System.currentTimeMillis()).nextFloat(),
        targetValue = 20.dp.value * Random(System.currentTimeMillis() - 10).nextFloat(),
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val alphaTransition by rememberInfiniteTransition().animateFloat(
        initialValue = 0.3f * Random(System.currentTimeMillis()).nextFloat(),
        targetValue = 0.2f * Random(System.currentTimeMillis() - 10).nextFloat(),
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 3947, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .wrapContentSize()
    ) {
        Canvas(
            modifier = Modifier,
            onDraw = {
                val height = size.height
                val width = size.width
                //外层灰环
                translate(
                    top = verticalShakeTransition + height / 2 - 280.dp.value,
                    left = width / 2 - 280.dp.value
                ) {
                    with(painter) {
                        draw(painter.intrinsicSize, alpha = alphaTransition)
                    }
                }
            })

    }
}