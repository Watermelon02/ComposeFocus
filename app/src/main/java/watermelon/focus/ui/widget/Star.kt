package watermelon.focus.ui.widget

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/30 15:10
 * @param color 通过设置ColorFilter的blender为SRCin模式，来展示不同颜色的星球
 */
@Composable
fun Star(color: Color = Color(0xFF000000)) {
    val vector = ImageVector.vectorResource(id = watermelon.focus.R.drawable.ic_star)
    val painter = rememberVectorPainter(image = vector)
    val verticalShakeTransition by rememberInfiniteTransition().animateFloat(
        initialValue = -40.dp.value* Random(System.currentTimeMillis()).nextFloat(),
        targetValue = 20.dp.value* Random(System.currentTimeMillis()-10).nextFloat(),
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val alphaTransition by rememberInfiniteTransition().animateFloat(
        initialValue = 0.8f * Random(System.currentTimeMillis()).nextFloat(),
        targetValue = 0.5f * Random(System.currentTimeMillis() - 10).nextFloat(),
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 7231, easing = FastOutSlowInEasing),
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
                        draw(painter.intrinsicSize, alpha = alphaTransition,colorFilter = ColorFilter.tint(color = color,blendMode = BlendMode.SrcIn))
                    }
                }
            })
    }
}