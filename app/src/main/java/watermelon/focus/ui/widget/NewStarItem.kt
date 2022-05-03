package watermelon.focus.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import watermelon.focus.R

/**
 * description ： 创建星球
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/1 10:48
 */
@Composable
fun NewStarItem(clickable: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp, end = 10.dp)
            .height(200.dp)
            .width(175.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { clickable() },
        shape = RoundedCornerShape(20.dp)
    ) {
        NewStar()
        //StarName
        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(top = 150.dp, start = 70.dp, end = 70.dp)
                .width(20.dp)
                .wrapContentHeight(), color = Color(0xFFF8F8F8)
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.h6,
                fontFamily = FontFamily(Font(R.font.store_my_stamp_number)),
                textAlign = TextAlign.Center, modifier = Modifier.alpha(0.5f)
            )
        }
    }
}