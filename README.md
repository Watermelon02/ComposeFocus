# Focus


> Focus是一款帮助你集中的app——为自己的目标建立星球，将时间投入在上面。花在星球上的每一分钟都会被记录，每颗星球可以定制颜色与外观。为了贯彻简单干净不让人分心的设计理念，app采用白灰为主色调。为了不使界面显得单调，所以增加了不少的动画

UI使用`Compose`实现，采用了MVI架构，涉及框架包括Navigation,Hilt,Room,Flow



## 预览
先看看目前的所有功能总的预览吧，使用流程主要就是：

1. `创建星球界面`设置星球名字，打卡时间和详细描述
2. `星球列表界面`选中要打卡的星球
3. `主界面`设置要打卡的时间，开始打卡
<div align=center> <img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/51a625381aea455c90e71259c3df2875~tplv-k3u1fbpfcp-watermark.image?"/> </div>

## 功能
### 主界面
- **左右滑动** 设置倒计时时间
- **长按星球** 当卫星消失再出现并开始逆时针转动的时候开始计时。打卡的时长会被记录进对应的星球中
<div align=center> <img src="https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/c4160270ba364b50b6153d2a34fa2b2d~tplv-k3u1fbpfcp-watermark.image?"/> </div>




### 星球列表界面
> **双击星球** 进入星球列表界面
- **上拉星球** 显示星球颜色的渐变背景，再点击可以删除星球
- **左右滑动** 查看已有星球
- **上滑界面** 查看星球详细信息
- **点击✨**   选中当前星球为要打卡的星球
<div align=center> <img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/094d5f94931a4551aa9371471cd5600b~tplv-k3u1fbpfcp-watermark.image?"/> </div>


### 新建星球界面
> **点击** 星球列表界面的最后一个带加号的星球进入新建星球界面
- **颜色选择** 选中新星球的颜色，这些颜色按钮也做了渐变的动画
- **时间选择** 没啥好说的
- **点击✨**   创建星球
<div align=center> <img src="https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/92af8dd73d8546f0a19fab34ac4b6b2e~tplv-k3u1fbpfcp-watermark.image?"/> </div>


## 主要实现
### 架构
- MVI
>MVI相比MVVM更加强调`数据的单向流动`和`唯一数据源`，项目中将用户所有的操作包装为Action,传入到界面对应的ViewModel中进行处理，在ViewModel中对界面的状态进行统一集中管理。而UI层则订阅ViewState,当界面状态变化时，Compose函数会自动进行更新
<div align=center> <img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bb3fe9361e244430bd2f69b70c7b0e75~tplv-k3u1fbpfcp-watermark.awebp"/> </div>

```kotlin
//将state的setter设置为私有，使状态只能在dispatch()中修改，保证数据只能单向修改
var mainPageViewStates by mutableStateOf(MainPageViewState())
    private set

//主界面ViewModel中统一对事件进行处理
fun dispatch(action: MainPageAction) {
    when (action) {
        is MainPageAction.DegreeUpdate -> degreeUpdate(action.degree)
        is MainPageAction.SelectStar -> selectStar(action.todoStar)
        is MainPageAction.StarChanged -> mainPageViewStates.starChanged.value = false
        is MainPageAction.NewStarPage -> mainPageViewStates.sheetPage.value =
            BottomSheetPage.NewStarPage
        is MainPageAction.BackToStarList -> mainPageViewStates.sheetPage.value =
            BottomSheetPage.StarListPage
        is MainPageAction.AddStar -> mainPageViewStates.addStar.value = true
        is MainPageAction.RefreshStar -> mainPageViewStates.refreshStarList.value++
        is MainPageAction.CountdownStart->countDownStart()
        is MainPageAction.Countdown -> countDown()
        is MainPageAction.InitSelectedStar -> initSelectedStar()
    }

//界面事件的密封类
sealed class MainPageAction {
    class DegreeUpdate(val degree: Float) : MainPageAction()
    .......
    }

}

```

- 依赖注入
通过使用`Hilt`来对ViewModel和Room数据库的Dao进行依赖注入，可以非常简单地实现解耦
```kotlin
//为ViewModel加上@HiltViewMode注解
@HiltViewModel
class NewStarViewModel constructor() : ViewModel() {...}
//然后直接在Composable函数的参数中使用hiltViewModel()进行依赖注入
@Composable
fun NewStarPage(
    ...
    viewModel: NewStarViewModel = hiltViewModel()
) {...}


```

- 数据存储
直接使用`Room`数据库来进行存储，同时，Room数据库支持直接返回`Flow`，所以也可以使用协程配合Flow来获取查询结果
```kotlin
//刷新星球列表数据，使用Flow来获取返回结果
private fun refreshStarList() {
    viewModelScope.launch {
        starDao.queryAllStar().collect {
            if (it.isNotEmpty()){
                starListPageStates.starList.value = it
            }
        }
    }
}
```

### 界面

- `声明式`和`手势api`

Compose的`声明式`写法和一些`手势api`让许多控件实现起来更为简单。
比如项目主界面中的星球倒计时时钟，这个时钟既需要能够处理用户的手指滑动来设置倒计时时间，还需要能够在用户长按之后开始倒计时。

在原先使用自定义view实现的时候，需要重写其`onTouchEvent()`,手动计算前后两次手指移动距离，然后旋转view，并回调给时钟View设置的接口来更新倒计时的时间，然后再将更新后的时间传递给上方的TextView。长按事件处理起来同样需要经过类似的步骤。

而使用compose则只需要一个记录滑动度数的state，然后将这个state传入`手势(Gesture)api`中。这样compose就会自动更新state的数值，而其它使用该state作为参数的compose函数也能自动重组。

- `LazyRow&LazyPage`

`LazyRow`和`LazyPage`类似于RecyclerView,但是不需要再去写adapter，layoutManager等，而且可以方便的将不同类型的item拼接在一起，不需要实现RecyclerViewConcatAdapter或是设置ViewHolder中不同的viewType。(但是好像目前性能不如RV
```kotlin
LazyRow(modifier = Modifier
    .fillMaxHeight(0.35f)
    .fillMaxWidth(), content = {
    //已创建星球列表
    for (star in starList) {item {...}}
    //新增星球Item,点击进入新增星球界面
    item {
        NewStarItem (...)
    }
})
```

- 动画
compose中自带不少强大的、可扩展的动画 API，可以轻松的实现一些效果。比如————
`AnimatedVisibility()`配合`ModalBottomSheetLayout()`实现伸缩列表：
```kotlin
//star的详细资料,开始隐藏，当modalBottomSheet展开时出现
AnimatedVisibility(visible = sheetState.currentValue == ModalBottomSheetValue.Expanded) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(),
    ) {...}
}
```
<div align=center> <img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4e8d4fd63bc241e282782c3459826655~tplv-k3u1fbpfcp-watermark.image?"/> </div>

再比如`InfiniteTransition`实现的动态渐变Button (删除星球按钮：
```kotlin
@Composable
fun DeleteButton(color: Color, onClick: () -> Unit) {
    val colorAnimation1 by rememberInfiniteTransition().animateColor(
        initialValue = color.copy(alpha = 0.35f),
        targetValue = color.copy(alpha = 0.75f),
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 4750 + 500 * color.alpha.toInt(),
                easing = FastOutLinearInEasing,
                delayMillis = 2730 * color.alpha.toInt()
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val colorAnimation2 by rememberInfiniteTransition().animateColor(类似上面的实现)
    Card(
        modifier = Modifier
            .padding(start = 10.dp, top = 20.dp, end = 10.dp)
            .height(200.dp)
            .width(175.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        IconButton(onClick = onClick) {
            Canvas(modifier = Modifier
                .padding(start = 10.dp, top = 40.dp, end = 10.dp)
                .height(170.dp)
                .width(175.dp), onDraw = {
                //渐变色块
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorAnimation2,
                            colorAnimation1
                        ),
                        start = Offset(0f,0f),end = Offset(400.dp.value,400.dp.value),
                    ),
                    radius = 300.dp.value,
                    center = Offset(x = size.width / 2, y = size.height / 2)
                )
            })
        }
    }
}
```
<div align=center> <img src="https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/404b45a782544d97aed8ff32bcd804e1~tplv-k3u1fbpfcp-watermark.image?"/> </div>

- 自定义绘制
这方面感觉和原生的写法大同小异，而自定义布局还没来得及了解，这里就不赘述了

## 总结
首次上手Compose和MVI,项目中的实现可能有不小的问题。Compose在实现许多界面元素的时候感觉比View要更加简单高效，但是用到的许多api都带有实验性注解。而且目前compose的教程不是很多，在遇到问题的时候不太好解决。

这里非常推荐想要上手的同学们参考`Jetpack compose博物馆` https://jetpackcompose.cn/docs/ 进行学习，不仅介绍了许多api的使用，带有实战例子，而且还有compose原理的解析。感恩

因为时间限制所以还有很多想要实现的功能没来得及做，不出意外的话之后会继续修改bug和增加功能。最后再次希望您能够给个star😭