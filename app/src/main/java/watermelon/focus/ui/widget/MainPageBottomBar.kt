package watermelon.focus.ui.page.page_main.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import watermelon.focus.R

/**
 * description ： MainPage底部的Bar,这一段是参考的
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/28 22:17
 */
@Composable
fun MainPageBottomBar() {
    BottomNavigation(
        backgroundColor = Color.White, modifier = Modifier
            .fillMaxHeight(0.1f)
    ) {
        var selectedItem by remember { mutableStateOf(0) }

        for (index in 0..2) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable(
                        onClick = {
                            selectedItem = index
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavigationIcon(index, selectedItem)
                //Icon和选中小点之间的间距
                Spacer(Modifier.padding(top = 4.dp))
                AnimatedVisibility(visible = index == selectedItem) {
                    when (index) {
                        0 -> NavigationText(text = "star")
                        1 -> NavigationText(text = "universe")
                        else -> NavigationText(text = "mine")
                    }
                }
            }
        }

    }
}


@Composable
fun NavigationIcon(
    index: Int,
    selectedItem: Int
) {
    val vectorStar = ImageVector.vectorResource(id = R.drawable.bottom_star)
    val vectorUniverse = ImageVector.vectorResource(id = R.drawable.bottom_universe)
    val vectorUser = ImageVector.vectorResource(id = R.drawable.bottom_user)
    val alpha = if (selectedItem != index) 0.2f else 0.5f

    when (index) {
        0 -> Icon(vectorStar, contentDescription = null, modifier = Modifier.alpha(alpha))
        1 -> Icon(vectorUniverse, contentDescription = null, modifier = Modifier.alpha(alpha))
        else -> Icon(vectorUser, contentDescription = null, modifier = Modifier.alpha(alpha))
    }
}

@Composable 
fun NavigationText(text:String){
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        fontFamily = FontFamily(Font(R.font.store_my_stamp_number)),
        modifier = Modifier.alpha(0.5f)
    )
}