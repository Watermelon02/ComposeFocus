package watermelon.focus.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * description ： NewStarPage中的色彩选择器
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/1 16:32
 */
@Composable
fun ColorSelector(selectColorListener: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp, end = 10.dp,bottom = 10.dp)
            .height(200.dp)
            .width(175.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {},
        shape = RoundedCornerShape(20.dp)
    ) {
        //左颜色列表
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
        ) {
            for (color in listOf(
                0xFF5B0FA5,
                0xA3F80C0C,
                0xFF29B6F6,
                0x9A26A69A
            )) {
                ColorButton(Color(color)) { selectColorListener(color) }
            }
        }
        //右颜色列表
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(end = 20.dp, top = 10.dp, bottom = 10.dp)
        ) {
            for (color in listOf(
                0xDA0D9958, 0xEDFFA726, 0xFFFFCA28,
                0x80000000
            )) {
                ColorButton(Color(color)) { selectColorListener(color) }
            }
        }
    }
}