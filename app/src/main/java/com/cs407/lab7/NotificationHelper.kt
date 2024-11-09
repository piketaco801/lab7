import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cs407.lab7.R

class NotificationHelper private constructor() {

    companion object {
        @Volatile
        private var instance: NotificationHelper? = null

        // Declare CHANNEL_ID: a unique identifier for the notification channel.
        const val CHANNEL_ID = "channel_chat"

        fun getInstance(): NotificationHelper {
            return instance ?: synchronized(this) {
                instance ?: NotificationHelper().also { instance = it }
            }
        }
    }

    // Declare a unique ID for each notification instance
    private var notificationId: Int = 0

    // Declare variables for sender name and message content
    private var sender: String? = null
    private var message: String? = null

    // The createNotificationChannel function creates a notification channel.
    // Notification channels are required for apps targeting Android O (API level 26) and above
    // to send notifications. It allows grouping of notifications by category.
    public fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // TODO Fetch the channel name and description from resources.
            // - Use context.getString(R.string.channel_name) to obtain the name of the channel.
            // - Use context.getString(R.string.channel_description) for the description.
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)

            // TODO Define the notification importance level.
            // - Use NotificationManager.IMPORTANCE_DEFAULT
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            // TODO Define and create a NotificationChannel instance with an ID, name,
            // and importance level.
            // - Use CHANNEL_ID as the unique identifier for this channel.
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                // TODO Assign the channel's description
                description = descriptionText
            }

            // TODO Register the channel with the system.
            // - Use context.getSystemService(NotificationManager::class.java) to get the
            // NotificationManager instance.
            // - Check if notificationManager is not null before calling createNotificationChannel(channel)
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Sets the content for a notification.
     *
     * @param sender The name of the sender to display in the notification.
     * @param message The message content to display in the notification.
     */
    fun setNotificationContent(sender: String, message: String) {
        // TODO: Set the sender field with the provided sender name
        // This will be used later to display who sent the message.
        this.sender = sender

        // TODO: Set the message field with the provided message content
        // This will display the message details in the notification.
        this.message = message

        // TODO: Increment notificationId to ensure each notification is unique
        // Each time a new notification is created, the ID is updated.
        notificationId++
    }

    /**
     * Displays a notification in the notification drawer.
     *
     * This function checks if the app has permission to post notifications, then builds and displays
     * a notification using the specified content title and text. The notification includes a small
     * icon and is set to be visible only on a secure lock screen to protect privacy.
     *
     * @param context The context in which the function is called, typically an Activity or Application context.
     */
    fun showNotification(context: Context) {
        // Ensure that the app has the required POST_NOTIFICATIONS permission before proceeding.
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        // Set up a NotificationCompat.Builder instance to create and customize the notification.
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)  // Set the small icon for the notification
            .setContentTitle(sender)  // Use the initialized sender variable
            .setContentText(message)  // Use the initialized message variable
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // Get a NotificationManagerCompat instance to issue the notification.
        val notificationManager = NotificationManagerCompat.from(context)

        // Send out the notification with the unique notificationId
        notificationManager.notify(notificationId, builder.build())
    }
}
