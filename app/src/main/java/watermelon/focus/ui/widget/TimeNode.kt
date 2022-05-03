package watermelon.focus.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import watermelon.focus.R
import kotlin.math.absoluteValue

/**
 * description ： TODO:类的作用
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/2 10:03
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerScope.TimeNode(time: Int) {
    Box(modifier = Modifier
        .graphicsLayer {
            //滑动时的缩放和渐变动画
            val pageOffset = calculateCurrentOffsetForPage(currentPage).absoluteValue
            leap(
                start = 1f,
                stop = 0.2f,
                fraction = 1f - pageOffset % 1
            ).also {
                scaleX = it
                scaleY = it
            }
            alpha = leap(
                start = 1f,
                stop = 0.1f,
                fraction = 1f - pageOffset % 1
            )
        }) {
        Text(
            text = time.toString(),
            style = MaterialTheme.typography.h4,
            color = Color(0x40000000),
            fontFamily = FontFamily(
                Font(R.font.store_my_stamp_number)
            )
        )
    }
}

//差值器，前半段和后半段执行相反动画，动画前后状态一致
fun leap(start: Float, stop: Float, fraction: Float): Float {
    return if (fraction < 0.5f) {
        (1 - fraction) * start + fraction * stop
    } else {
        (1 - fraction) * stop + fraction * start
    }
}