package watermelon.focus.ui.widget

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

/**
 * description ： TODO:类的作用
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/25 21:43
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Clock(
    draggableDegree: MutableState<Float>,
    draggableState: DraggableState,
    dragStoppedListener: suspend CoroutineScope.(velocity: Float) -> Unit,
    settingMinutes: Float,
    isCountdown: Boolean,alphaTransition:Float
) {
    val rotateTransition by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            //左右滑动设置打卡时间
            .draggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                enabled = true,
                onDragStopped = dragStoppedListener
            ),
            onDraw = {
                val height = size.height
                val width = size.width
                val radius = 0.4f * width - 15.dp.value
                rotate(
                    draggableDegree.value,
                    pivot = Offset(width / 2, height / 2)
                ) {
                    //外层环
                    drawCircle(
                        center = Offset(x = width / 2, y = height / 2),
                        radius = radius,
                        color = Color(0x80000000)
                    )
                    //内层圈
                    drawCircle(radius = radius - 10.dp.value, color = Color(0xFFF8F8F8))
                    //旋转卫星
                    rotate(
                        degrees = if (isCountdown) settingMinutes * 60 else rotateTransition,
                        pivot = Offset(x = width / 2, y = height / 2)
                    ) {
                        drawLine(
                            color = Color.White,
                            start = Offset(x = width / 2, y = height / 2 - radius + 15.dp.value),
                            end = Offset(x = width / 2, y = height / 2 - radius),
                            strokeWidth = 80.dp.value,alpha = alphaTransition
                        )
                        //卫星
                        drawCircle(
                            center = Offset(
                                x = width / 2,
                                y = height / 2 - radius
                            ),
                            radius = 30.dp.value,
                            color = Color.Gray, alpha = alphaTransition
                        )
                        drawCircle(
                            center = Offset(
                                x = width / 2,
                                y = height / 2 - radius
                            ),
                            radius = 20.dp.value,
                            color = Color.White, alpha = alphaTransition
                        )
                    }
                }
            })
    }
}
