package com.example.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.android.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
	public static final String FCM_PARAM = "picture";
	private static final String CHANNEL_NAME = "FCM";
	private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
	private int numMessages = 0;
	private Button buttonDisplayToken;
	private TextView textViewToken;
	private Button buttonRegister;
	private EditText editTextEmail;
	private ProgressDialog progressDialog;
	private static final String URL_REGISTER_DEVICE = "http://192.168.73.69:80/Android/RegisterDevice.php";


	@RequiresApi(api = Build.VERSION_CODES.M)


	@Override
	public void onNewToken(String token) {
		Log.d(TAG, "Refreshed token: " + token);

		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// FCM registration token to your app server.
		sendRegistrationToServer(token);
	}




	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		RemoteMessage.Notification notification = remoteMessage.getNotification();
		Map<String, String> data = remoteMessage.getData();
		Log.d("FROM", remoteMessage.getFrom());
		sendNotification(notification, data);
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
		Bundle bundle = new Bundle();
		bundle.putString(FCM_PARAM, data.get(FCM_PARAM));

		Intent intent = new Intent(this, SecondActivity.class);
		intent.putExtras(bundle);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
				.setContentTitle(notification.getTitle())
				.setContentText(notification.getBody())
				.setAutoCancel(true)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				//.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
				.setContentIntent(pendingIntent)
				.setContentInfo("Hello")
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
				.setColor(getColor(R.color.colorAccent))
				.setLights(Color.RED, 1000, 300)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setNumber(++numMessages)
				.setSmallIcon(R.drawable.ic_notification);

		try {
			String picture = data.get(FCM_PARAM);
			if (picture != null && !"".equals(picture)) {
				URL url = new URL(picture);
				Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				notificationBuilder.setStyle(
						new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
				);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(
					getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
			);
			channel.setDescription(CHANNEL_DESC);
			channel.setShowBadge(true);
			channel.canShowBadge();
			channel.enableLights(true);
			channel.setLightColor(Color.RED);
			channel.enableVibration(true);
			channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

			assert notificationManager != null;
			notificationManager.createNotificationChannel(channel);
		}

		assert notificationManager != null;
		notificationManager.notify(0, notificationBuilder.build());
	}


	private void sendRegistrationToServer(String token) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Registering Device...");
		progressDialog.show();

		token = SharedPrefManager.getInstance(this).getDeviceToken();
		final String email = editTextEmail.getText().toString();

		if (token == null) {
			progressDialog.dismiss();
			Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
			return;
		}

		String finalToken = token;
		StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						progressDialog.dismiss();
						try {
							JSONObject obj = new JSONObject(response);
							Toast.makeText(MyFirebaseMessagingService.this, obj.getString("message"), Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						progressDialog.dismiss();
						Toast.makeText(MyFirebaseMessagingService.this, error.getMessage(), Toast.LENGTH_LONG).show();
					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("email", email);
				params.put("token", finalToken);
				return params;
			}
		};
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}




}