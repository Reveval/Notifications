package ru.startandroid.develop.p0184notificationsintro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "1"
const val FIRST_NOTIFICATION_ID = 1
const val SECOND_NOTIFICATION_ID = 2

//Введение в уведомления
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Создаем экземпляр NotificationManager для конфигурирования уведомлений
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        /*
            Создаем канал для уведомлений - создавая канал, разработчик дает пользователю
                возможность настроить поведение определенной группы уведомлений. В конструкторе
                NotificationChannel указываем ID, имя и важность
         */
        val channel = NotificationChannel(CHANNEL_ID, "MyChannel",
            NotificationManager.IMPORTANCE_HIGH)

        channel.apply {
            //указываем прочие данные и настройки
            description = "My channel description"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(false)
        }

        //создаем канал
        notificationManager.createNotificationChannel(channel)

        //для построения простого уведомления используем билдер
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)

        //Методом build получаем готовое уведомление.
        val notification = builder.apply {
            //указываем иконку, заголовок и текст для уведомления.
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Title")
            setContentText("NotificationText")
        }.build()

        /*
            Далее используем NotificationManager и его метод notify, чтобы показать созданное
                уведомление. Кроме notification, требуется передать id. Это необходимо, чтобы в
                дальнейшем мы могли использовать этот id для обновления или удаления уведомления.
         */
        notificationManager.notify(FIRST_NOTIFICATION_ID, notification)

        //запускаем обновление нашего уведомления по нажатию на кнопку
        findViewById<Button>(R.id.button_update).setOnClickListener {
            updateFirstNotification()
        }

        //создаем второе уведомление
        val secondBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        secondBuilder.apply {
            setSmallIcon(android.R.drawable.ic_dialog_email)
            setContentTitle("Title 2")
            setContentText("Notification text 2")
        }
        createNotification(SECOND_NOTIFICATION_ID, secondBuilder)

        //Удалим второе уведомление по нажатию на кнопку
        findViewById<Button>(R.id.button_delete).setOnClickListener {
            deleteNotification(SECOND_NOTIFICATION_ID)
        }

        /*
            сделаем обработку нажатий на уведомление. Чтобы выполнить какое-либо действие по
                нажатию на уведомление, необходимо использовать PendingIntent. PendingIntent - это
                контейнер для Intent. Этот контейнер может быть использован для последующего
                запуска вложенного в него Intent. Созданный PendingIntent нам надо будет передать в
                билдер уведомления.
         */
        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        //создадим новое уведомление с обработчиком нажатий
        val thirdBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        thirdBuilder.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Title 3")
            setContentText("Notification text 3")
            //вешаем intent
            setContentIntent(resultPendingIntent)
            //Уведомление, созданное с этим флагом будет закрываться после нажатия на него.
            setAutoCancel(true)
        }

        createNotification(3, thirdBuilder)
    }

    //Метод для обновления уведомления
    private fun updateFirstNotification() {
        /*
            Код полностью аналогичен коду, что мы использовали при отображении уведомления. Только
                в билдере используем другие тексты и иконку. Самое главное, что в методе notify мы
                снова используем id = 1. NotificationManager по этому id найдет уведомление,
                которое мы отобразили чуть раньше и заменит его новым
         */
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        val notification = builder.apply {
            setSmallIcon(android.R.drawable.ic_dialog_email)
            setContentTitle("Title change")
            setContentText("Notification text change")
        }.build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(FIRST_NOTIFICATION_ID, notification)
    }

    //Напишем код для создания нескольких уведомлений
    private fun createNotification(notificationID: Int, builder: NotificationCompat.Builder) {
        val notification = builder.build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationID, notification)
    }

    /*
        метод для удаления уведомлений. Чтобы удалить уведомление, используем NotificationManager и
            его метод cancel с указанием id уведомления. Либо методом cancelAll можем удалить все
            уведомления сразу
     */
    private fun deleteNotification(notificationID: Int) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationID)
        Toast.makeText(this, "Notification with ID $notificationID was deleted",
            Toast.LENGTH_SHORT).show()
    }
}