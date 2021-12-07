package com.example.android;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.example.android.http.AsyncResponse;
import com.example.android.http.HttpCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NFCReadFragment extends DialogFragment {

    public static final String TAG = NFCReadFragment.class.getSimpleName();
    private final String filename2 = "address.txt";
    String file;

    public static NFCReadFragment newInstance() {

        return new NFCReadFragment();
    }

    private TextView mTvMessage;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Nfc)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef){

        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "Place your ID Card Here: "+message);
            mTvMessage.setText("ID :  "+message);
            ndef.close();
            readData();
            insertData(message);

        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
    }

    private void readData() {
        try {
            FileInputStream fin = getActivity().openFileInput(filename2);
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }

            // setting text from the file.
            file = temp.toString();
            fin.close();
            // UserLoginFunction(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertData(final String m) {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("m", m);

        HttpCall.makeFormRequest(getActivity(), file+"/Android/nfc_data.php", requestBodyMap, new AsyncResponse() {
            @Override
            public void postExecute(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                   // JSONObject bodyObject = jsonObject.getJSONObject("response");

                    Intent intent = new Intent(getActivity(), mark_attendance_dashboard.class);
                   // intent.setFlags(Intent.FLAG_ACTIVITY_NgetApplicationContext()O_HISTORY);
                    startActivity(intent);
                    //finish();

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Attendance Marked Out Succesfully", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void postError(VolleyError error) {
                Toast.makeText(getActivity(), "Something gone Wrong", Toast.LENGTH_SHORT).show();


            }
        });
    }


}
