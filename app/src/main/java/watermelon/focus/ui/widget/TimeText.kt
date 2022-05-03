package watermelon.focus.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import watermelon.focus.R

/**
 * description ： TODO:类的作用
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/26 21:15
 */
@Composable
fun BoxScope.TimeText(draggableDegree:Float,settingMinutes:Float,isCountdown:Boolean) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .align(Alignment.BottomCenter)
            .alpha(0.5f)
    ) {
        val totalMinutes = if (isCountdown) settingMinutes else draggableDegree/ 360 * TOTAL_MINUTES
        val hourString = totalMinutes / 60
        val minuteString = totalMinutes % 60
        Text(
            text = "${hourString.toInt()} : ${minuteString.toInt()}",
            style = MaterialTheme.typography.h3,
            fontFamily = FontFamily(Font(R.font.store_my_stamp_number))
        )
    }
}

const val TOTAL_MINUTES = 720//12*60 min