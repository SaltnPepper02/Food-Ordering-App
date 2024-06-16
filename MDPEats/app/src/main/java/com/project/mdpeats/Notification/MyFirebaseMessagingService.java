package com.project.mdpeats.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.MainActivity;
import com.project.mdpeats.Models.Token;
import com.project.mdpeats.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {// attempt at FIrebase Messaging
//    @Override
//    public void onNewToken(String token) {
//        super.onNewToken(token);
//        token = String.valueOf(FirebaseMessaging.getInstance().getToken());
//        updateTokenToFirebase(token);
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        sendNotification(remoteMessage);
//    }
//
//    private void sendNotification(RemoteMessage remoteMessage) {
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//
//        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentText(notification.getTitle())
//                .setContentText(notification.getBody())
//                .setAutoCancel(true)
//                .setSound(defaultSound)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager)  getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, builder.build());
//
//    }
//
//    private void updateTokenToFirebase(String tokenRefreshed) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference tokens = database.getReference("Tokens");
//        Token token = new Token(tokenRefreshed, false);
//        tokens.child(Common.currentStudent.getStudentID()).setValue(token);
//    }
}