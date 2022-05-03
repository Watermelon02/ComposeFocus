package watermelon.focus.ui.page.page_countdown

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/**
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/4/26 23:15
 */
class CountDownPageViewModel: ViewModel() {
    val lastSecond = mutableStateOf(0)
}