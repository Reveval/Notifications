package ru.startandroid.develop.p0184notificationwithprogressbar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val max = 100

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
            Сначала отображаем бесконечный прогрессбар и текст Preparing. Т.е. делаем вид, что идет
                подготовка к выполнению операции
         */
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("1", "MyChannel",
            NotificationManager.IMPORTANCE_HIGH)
        channel.apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(false)
        }

        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(this, "1")

        builder.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Some operation")
            setContentText("Preparing")
            setProgress(max,0, true);
        }

        notificationManager.notify(1, builder.build())

        Thread {
            try {
                TimeUnit.SECONDS.sleep(3)
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }

            var progress = 0

            /*
                Затем в отдельном потоке имитируем выполнение операции. Каждые 300 мсек увеличиваем
                    значение progress и обновляем уведомление, чтобы прогрессбар показал текущий
                    прогресс. А также в тексте показываем значение прогресса и максимума.
             */
            while (progress < max) {
                try {
                    TimeUnit.MILLISECONDS.sleep(300)
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                }

                progress += 10

                //показываем уведомление с текущим прогрессом
                builder.setProgress(max, progress, false)
                    .setContentText("$progress of $max")
                notificationManager.notify(1, builder.build())
            }

            //После выполнения операции скрываем прогрессбар и показываем текст Completed.
            builder.setProgress(0, 10, false)
                .setContentText("Completed")
            notificationManager.notify(1, builder.build())
        }.start()
    }
}