package ru.startandroid.develop.extendednotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import androidx.core.app.Person
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

const val CHANNEL_ID_FOR_EXTENDED_NOTIFICATIONS = "1"
const val EXTENDED_NOTIFICATION1_ID = 1
const val EXTENDED_NOTIFICATION2_ID = 2
const val EXTENDED_NOTIFICATION3_ID = 3

//Создаем расширенные уведомления
class MainActivity : AppCompatActivity() {
    val longText = "To have a notification appear in an expanded view, " +
            "first create a NotificationCompat.Builder object " +
            "with the normal view options you want. " +
            "Next, call Builder.setStyle() with an " +
            "expanded layout object as its argument."

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
            В билдере уведомления вызываем метод setStyle, в который нам необходимо передать
                стиль. Создаем стиль BigTextStyle и передаем ему длинный текст в метод bigText.
         */
        val builder1 = NotificationCompat.Builder(this,
            CHANNEL_ID_FOR_EXTENDED_NOTIFICATIONS)
        builder1.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Title")
            setContentText("Notification Text")
            setStyle(NotificationCompat.BigTextStyle().bigText(longText))
        }.build()

        createNotification(EXTENDED_NOTIFICATION1_ID, builder1)

        //применим к уведомлению стиль InboxStyle - разместит до 5 ваших строк в виде списка
        val builder2 = NotificationCompat.Builder(this,
            CHANNEL_ID_FOR_EXTENDED_NOTIFICATIONS)
        builder2.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Title")
            setContentText("Notification text")
            setStyle(NotificationCompat.InboxStyle()
                    //добавляем новые линии
                .addLine("Line 1")
                .addLine("Line 2")
                .addLine("Line 3"))
        }

        createNotification(EXTENDED_NOTIFICATION2_ID, builder2)

        //Стиль MessagingStyle удобен для отображения последних сообщений из чата
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //Создаем несколько объектов Person для дальнейшего использования в чате
            val admin = Person.Builder()
                .setName("You")
                .build()
            val ivan = Person.Builder()
                .setName("Ivan")
                .build()
            val andrey = Person.Builder()
                .setName("Andrey")
                .build()
            /*
                В конструкторе MessagingStyle вам необходимо указать Person с именем, которое будет
                видеть пользователь на своем месте. Обычно в чатах используют слово You (или Me).
             */
            val messagingStyle = NotificationCompat.MessagingStyle(admin)
            messagingStyle.run {
                /*
                    В setConversationTitle указывается название чата. Обычно это используется,
                        когда в чате более двух собеседников.
                 */
                conversationTitle = "Android Chat"
                /*
                    Далее, методом addMessage добавляются сообщения. Сообщение состоит из трех
                        полей: текст, время, отправитель.
                 */
                addMessage("Всем привет", System.currentTimeMillis(), ivan)
                addMessage("Кто перешел на новую студию, как оно", System.currentTimeMillis(),
                    ivan)
                addMessage("Я пока не переходил, жду отзывов", System.currentTimeMillis(),
                    andrey)
                addMessage("Я перешел", System.currentTimeMillis(), admin)
                addMessage("Было несколько проблем, но все решаемо",
                    System.currentTimeMillis(), admin)
                addMessage("Ок, спасибо!", System.currentTimeMillis(), ivan)
            }

            val builder3 = NotificationCompat.Builder(this,
                CHANNEL_ID_FOR_EXTENDED_NOTIFICATIONS)
            builder3.apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle("Title")
                setContentText("Notification text")
                //сетаем новый стиль
                setStyle(messagingStyle)
            }

            createNotification(EXTENDED_NOTIFICATION3_ID, builder3)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(notificationID: Int, builder: NotificationCompat.Builder) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID_FOR_EXTENDED_NOTIFICATIONS, "MyChannel",
            NotificationManager.IMPORTANCE_HIGH)
        channel.apply {
            description = "My Main Channel Description"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(false)
        }
        notificationManager.createNotificationChannel(channel)
        val notification =  builder.build()
        notificationManager.notify(notificationID, notification)
    }
}