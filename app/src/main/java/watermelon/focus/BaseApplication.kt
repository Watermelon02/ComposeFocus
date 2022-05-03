package watermelon.focus

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import dagger.hilt.android.HiltAndroidApp

/**
 * description ： 用于初始化Arouter的Application子类
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/3/29 08:54
 */

@HiltAndroidApp
class BaseApplication : Application() {
    companion object{
        //app生命周期context，用于创建数据库
        @SuppressLint("StaticFieldLeak", "CI_StaticFieldLeak")
        lateinit var context: Context
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = base
    }

    override fun onCreate() {
        super.onCreate()
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this)
    }
}
