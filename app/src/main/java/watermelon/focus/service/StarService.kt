package watermelon.focus.service

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import watermelon.focus.MainPageActivity
import watermelon.focus.R
import watermelon.focus.model.bean.TodoStar
import watermelon.focus.model.database.StarDao
import java.util.*
import javax.inject.Inject

/**
 * description ： 前台服务，如果当天有star则发送通知
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/5/2 16:22
 */
@AndroidEntryPoint
class StarService : Service() {
    private lateinit var job: Job
    private val calendar = Calendar.getInstance()

    @Inject
    lateinit var starDao: StarDao
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(DelicateCoroutinesApi::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        job = GlobalScope.launch {
            while (true) {
                withContext(Dispatchers.IO) {
                    val nowDate =
                        "${calendar[Calendar.YEAR]}-${calendar[Calendar.MONTH] + 1}-${calendar[Calendar.DATE]}"
                    starDao.queryStarListByDate(nowDate).collect {
                        for (star in it) {
                            val notification = createNotification(star)
                            startForeground(1, notification)
                        }
                    }
                    delay(3000000)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotification(todoStar: TodoStar): Notification {
        //传入营业进度，生成相应的Notification对象
        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel("42", "Focus", NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
        val intent = Intent(this, MainPageActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        return Notification.Builder(this, channel.id)
            .setContentTitle("Star coming")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))
            .setSmallIcon(R.drawable.logo).setContentIntent(pendingIntent)
            .setContentText("the star of ${todoStar.name} is coming,come to focus on it").setAutoCancel(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}