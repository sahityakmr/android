package com.example.android;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NfcWrite extends AppCompatActivity implements Listener{

    public static final String TAG = Nfc.class.getSimpleName();

    private EditText mEtMessage;
    private Button mBtWrite;
    private Button mBtRead;
    private Button manual;

    private NFCWriteFragment mNfcWriteFragment;
    private NFCReadFragment mNfcReadFragment;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;
    private long pressedTime;
    String str;
    private TextView id;


    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc);
        Intent intent = getIntent();
        str = intent.getStringExtra("message_key");


        initViews();
        initNFC();
    }

    private void initViews() {

        mEtMessage = (EditText) findViewById(R.id.et_message);

        mBtWrite = (Button) findViewById(R.id.btn_write);
        mBtRead = (Button) findViewById(R.id.btn_read);
        manual = (Button) findViewById(R.id.btn_manual);
        manual.setOnClickListener(view -> showManual());
        mBtWrite.setOnClickListener(view -> showWriteFragment());
        mBtRead.setVisibility(View.GONE);
       // mBtRead.setOnClickListener(view -> showReadFragment());
        mEtMessage.setText(str);
        mEtMessage.setEnabled(false);
        id = (TextView) findViewById(R.id.et_view);
    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }



    private void showManual(){
        mEtMessage.setText("");
        mEtMessage.setEnabled(true);
        Toast.makeText(NfcWrite.this, " EditText Disable Programmatically ", Toast.LENGTH_LONG).show();
    }

    private void showWriteFragment() {

        isWrite = true;

        mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);

        if (mNfcWriteFragment == null) {

            mNfcWriteFragment = NFCWriteFragment.newInstance();
        }
        mNfcWriteFragment.show(getFragmentManager(),NFCWriteFragment.TAG);

    }

//    private void showReadFragment() {
//
//        mNfcReadFragment = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
//
//        if (mNfcReadFragment == null) {
//
//            mNfcReadFragment = NFCReadFragment.newInstance();
//        }
//        mNfcReadFragment.show(getFragmentManager(),NFCReadFragment.TAG);
//
//    }

    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            id.setText("NFC Tag\n" + ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Log.d(TAG, "onNewIntent: " + intent.getAction());

            if (tag != null) {
                Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
                Ndef ndef = Ndef.get(tag);


                if (isDialogDisplayed) {

                    if (isWrite) {

                        String messageToWrite = mEtMessage.getText().toString();
                        String sourceStr = mEtMessage.getText().toString();

                        try {
                            //encrypt12(mEtMessage.getText().toString(), Integer.parseInt("1"));
                            //String encrypted = AESUtils.encrypt(sourceStr);
                            //Log.d("TEST", "encrypted:" + encrypted);
                            mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);
                            mNfcWriteFragment.onNfcDetected(ndef, messageToWrite);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //                    mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);
                        //                    mNfcWriteFragment.onNfcDetected(ndef,messageToWrite);

                    } else {

                        mNfcReadFragment = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                        mNfcReadFragment.onNfcDetected(ndef);
                        Log.d("TAG", "Empty Tag || Id Not Found On Server");
                    }
                }
            }
        }
    }


    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }



    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(NfcWrite.this, ValidateIDCard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(NfcWrite.this, MainActivity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}
