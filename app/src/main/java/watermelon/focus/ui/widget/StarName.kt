package watermelon.focus.ui.widget

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import watermelon.focus.R

/**
 * description ： TODO:类的作用
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/30 18:29
 */
@Composable
fun BoxScope.StarName(name:String) {
    Surface(
        color = Color(0xFFF8F8F8),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .width(100.dp)
            .padding(bottom = 200.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h6,
            fontFamily = FontFamily(Font(R.font.store_my_stamp_number)),
            textAlign = TextAlign.Center,modifier = Modifier.alpha(0.5f)
        )
    }
}