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
 * date : 2022/4/30 15:15
 */
@Composable
fun BoxScope.Signature(text:String){
    Box(
        modifier = Modifier
            .fillMaxHeight(0.80f)
            .align(Alignment.BottomCenter)
            .alpha(0.5f)
    ) {
        Text(
            text = "$text",
            style = MaterialTheme.typography.h4,
            fontFamily = FontFamily(Font(R.font.store_my_stamp_number))
        )
    }
}