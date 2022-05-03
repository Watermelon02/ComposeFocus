package watermelon.focus.ui.page.page_countdown

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import watermelon.focus.R

/**
 * description ： 倒计时界面
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/26 22:42
 */
@Composable
fun CountdownPage(countdownSeconds: Int, viewModel: CountDownPageViewModel) {
    val rotateTransition by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    var trigger by remember { mutableStateOf(countdownSeconds) }

    val lastTime by animateIntAsState(
        targetValue = trigger * 1000,
        animationSpec = tween(countdownSeconds * 1000, easing = LinearEasing)
    )

    DisposableEffect(Unit) {
        trigger = 0
        onDispose { }
    }

    val (hou, min, sec) = remember(lastTime / 1000) {
        val elapsedInSec = lastTime / 1000
        val hou = elapsedInSec / 3600
        val min = elapsedInSec / 60 - hou * 60
        val sec = elapsedInSec % 60
        viewModel.lastSecond.value = elapsedInSec
        Triple(hou, min, sec)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            val radius = size.width / 4f
            val height = maxOf(size.height, size.width)
            val width = minOf(size.height, size.width)
            drawCircle(
                center = Offset(x = width / 2, y = height / 2),
                radius = 2 * radius,
                color = Color(0x80000000)
            )
            //白色圆形背景
            drawCircle(
                center = Offset(x = width / 2, y = height / 2),
                radius = 1.975f * radius,
                color = Color.White
            )
            rotate(degrees = rotateTransition, pivot = Offset(x = width / 2, y = height / 2)) {
                drawLine(
                    color = Color.White,
                    start = Offset(x = width / 2, y = height / 2),
                    end = Offset(x = width / 2, y = height / 2 - 2f * radius),
                    strokeWidth = 12.dp.value
                )
            }
        })
        //剩余时间
        Box(
            modifier = Modifier
                .wrapContentSize()
                .alpha(0.5f)
        ) {
            Text(
                text = "$hou : $min",
                style = MaterialTheme.typography.h3,
                fontFamily = FontFamily(Font(R.font.store_my_stamp_number))
            )
        }
    }
}
