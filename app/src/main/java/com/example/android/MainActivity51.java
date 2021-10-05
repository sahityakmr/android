package com.example.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

//import static com.example.fcm.R.id.txt;

public class MainActivity51 extends AppCompatActivity implements View.OnClickListener{
	private static final String AUTH_KEY = "key=YOUR-SERVER-KEY";
	private TextView mTextView;
	private String token;
	private Button buttonRegister;
	private ProgressDialog progressDialog;
	private EditText editTextEmail;
	private static final String URL_REGISTER_DEVICE = "http://192.168.1.106:80/Android/RegisterDevice.php";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main51);
		mTextView = findViewById(R.id.txt);
		buttonRegister = (Button) findViewById(R.id.buttonRegister);
		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		buttonRegister.setOnClickListener(this);


		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String tmp = "";
			for (String key : bundle.keySet()) {
				Object value = bundle.get(key);
				tmp += key + ": " + value + "\n\n";
			}
			mTextView.setText(tmp);
		}

		FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
			@Override
			public void onComplete(@NonNull Task<String> task) {
				if (!task.isSuccessful()) {
					token = task.getException().getMessage();
					Log.w("FCM TOKEN Failed", task.getException());
				} else {
					token = task.getResult();
					Log.i("FCM TOKEN", token);
				}

//				String msg = getString(R.string.msg_token_fmt, token);
//				Log.d(TAG, msg);
//				Toast.makeText(MainActivity51.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}


	public void showToken(View view) {
		mTextView.setText(token);
	}

	public void subscribe(View view) {
		FirebaseMessaging.getInstance().subscribeToTopic("news");
		mTextView.setText(R.string.subscribed);
	}

	public void unsubscribe(View view) {
		FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
		mTextView.setText(R.string.unsubscribed);
	}

	public void sendToken(View view) {
		sendWithOtherThread("token");
	}

	public void sendTokens(View view) {
		sendWithOtherThread("tokens");
	}

	public void sendTopic(View view) {
		sendWithOtherThread("topic");
	}

	private void sendWithOtherThread(final String type) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				pushNotification(type);
			}
		}).start();
	}

	private void pushNotification(String type) {
		JSONObject jPayload = new JSONObject();
		JSONObject jNotification = new JSONObject();
		JSONObject jData = new JSONObject();
		try {
			jNotification.put("title", "Google I/O 2016");
			jNotification.put("body", "Firebase Cloud Messaging (App)");
			jNotification.put("sound", "default");
			jNotification.put("badge", "1");
			jNotification.put("click_action", "OPEN_ACTIVITY_1");
			jNotification.put("icon", "ic_notification");

			jData.put("picture", "https://miro.medium.com/max/1400/1*QyVPcBbT_jENl8TGblk52w.png");

			switch (type) {
				case "tokens":
					JSONArray ja = new JSONArray();
					ja.put("c5pBXXsuCN0:APA91bH8nLMt084KpzMrmSWRS2SnKZudyNjtFVxLRG7VFEFk_RgOm-Q5EQr_oOcLbVcCjFH6vIXIyWhST1jdhR8WMatujccY5uy1TE0hkppW_TSnSBiUsH_tRReutEgsmIMmq8fexTmL");
					ja.put(token);
					jPayload.put("registration_ids", ja);
					break;
				case "topic":
					jPayload.put("to", "/topics/news");
					break;
				case "condition":
					jPayload.put("condition", "'sport' in topics || 'news' in topics");
					break;
				default:
					jPayload.put("to", token);
			}

			jPayload.put("priority", "high");
			jPayload.put("notification", jNotification);
			jPayload.put("data", jData);

			URL url = new URL("https://fcm.googleapis.com/fcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", AUTH_KEY);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			// Send FCM message content.
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(jPayload.toString().getBytes());

			// Read FCM response.
			InputStream inputStream = conn.getInputStream();
			final String resp = convertStreamToString(inputStream);

			Handler h = new Handler(Looper.getMainLooper());
			h.post(new Runnable() {
				@Override
				public void run() {
					mTextView.setText(resp);
				}
			});
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	private String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next().replace(",", ",\n") : "";
	}

	@Override
	public void onClick(View view) {

		if (view == buttonRegister) {
			sendTokenToServer();
		}


	}

	private void sendTokenToServer() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Registering Device...");
		progressDialog.show();

		//final String token = SharedPrefManager.getInstance(this).getDeviceToken();
		final String email = editTextEmail.getText().toString();

		if (token == null) {
			progressDialog.dismiss();
			Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
			return;
		}

		StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						progressDialog.dismiss();
						try {
							JSONObject obj = new JSONObject(response);
							Toast.makeText(MainActivity51.this, obj.getString("message"), Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						progressDialog.dismiss();
						Toast.makeText(MainActivity51.this, error.getMessage(), Toast.LENGTH_LONG).show();
					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("email", email);

				params.put("token", token);
				return params;
			}
		};
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}


}