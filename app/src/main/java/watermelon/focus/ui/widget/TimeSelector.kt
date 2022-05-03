package watermelon.focus.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import watermelon.focus.ui.page.page_newStar.NewStarViewModel
import java.util.*

/**
 * description ： NewStarPage中的时间选择器
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/2 10:59
 */

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TimeSelector(viewModel: NewStarViewModel, onCreateStar: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxHeight(0.35f)
            .fillMaxWidth()
            .padding(10.dp), shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF8F8F8)
    ) {
        val calendar = remember { Calendar.getInstance() }
        val years =
            remember { (calendar[Calendar.YEAR]..(calendar[Calendar.YEAR] + 3)).toList() }
        val months = remember { (1..12).toList() }
        val days = viewModel.days
        val yearState = rememberPagerState()
        val monthState = rememberPagerState(initialPage = calendar[Calendar.MONTH])
        //因为calendar中的月份，年份为序数，日期为基数，所以此处-1
        val dayState = rememberPagerState(initialPage = calendar[Calendar.DATE] - 1)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //year
            VerticalPager(count = years.size, state = yearState) { page ->
                Card(
                    shape = RoundedCornerShape(20.dp), modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                        .padding(bottom = 15.dp), backgroundColor = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        TimeNode(time = years[page])
                    }
                }
            }
            //month
            VerticalPager(count = months.size, state = monthState) { page ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                        .padding(bottom = 15.dp), backgroundColor = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        TimeNode(time = months[page])
                    }
                }
            }
            //day
            VerticalPager(count = days.size, state = dayState) { page ->
                Card(
                    shape = RoundedCornerShape(20.dp), modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                        .padding(bottom = 15.dp), backgroundColor = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        TimeNode(time = days[page])
                    }
                }
            }
            //确认添加按钮
            Card(
                shape = RoundedCornerShape(20.dp), modifier = Modifier
                    .fillMaxHeight()
                    .width(85.dp)
                    .padding(bottom = 15.dp), backgroundColor = Color.White
            ) {
                StarIconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onSelectStar = {
                        onCreateStar("${yearState.currentPage + calendar[Calendar.YEAR]}-${monthState.currentPage + 1}-${dayState.currentPage+1}")
                    })
            }
        }
    }
}