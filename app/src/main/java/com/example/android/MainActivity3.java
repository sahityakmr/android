package com.example.android;
import android.Manifest;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ImageView;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.UnsupportedEncodingException;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import java.util.List;
import java.io.File;
import java.util.Objects;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.android.R;

import static androidx.core.content.FileProvider.getUriForFile;

public class MainActivity3 extends AppCompatActivity {
    //   private static final String TAG = MainActivity5.class.getSimpleName();

    Bitmap bitmap, photo;

    boolean check = true;

    Button SelectImageGallery, UploadImageServer, cam;

    ImageView imageView;

    TextView imageName;
    private final String filename = "cache.txt";

    ProgressDialog progressDialog ;

    String GetImageNameEditText;

    String ImageName = "image_name" ;
    String Temp;
    String filePath;
    String a;

    String ImagePath = "image_path" ;

    private static final int pic_id = 123;
    String fileName;

    // String ServerUploadPath ="http://192.168.1.106:80/android/img_upload_to_server.php" ;
    private long pressedTime;
    private final String filename2 = "address.txt";
    String file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        imageView = (ImageView)findViewById(R.id.imageView);

        imageName = (TextView) findViewById(R.id.editTextImageName);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        imageName.setText(str);

        SelectImageGallery = (Button)findViewById(R.id.buttonSelect);
        cam = (Button)findViewById(R.id.buttonSelect2);

        UploadImageServer = (Button)findViewById(R.id.buttonUpload);

        readData();

        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="check1";

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });


        UploadImageServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a="check2";

                GetImageNameEditText = imageName.getText().toString();

                ImageUploadToServerFunction();

            }
        });


        cam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                takeCameraImage();

                // Create the camera_intent ACTION_IMAGE_CAPTURE
                // it will open the camera for capture the image
//                Intent camera_intent
//                        = new Intent(MediaStore
//                        .ACTION_IMAGE_CAPTURE);
//
//                // Start the activity with camera_intent,
//                // and request pic id
//                startActivityForResult(camera_intent, pic_id);
            }
        });


    }

    private void takeCameraImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            fileName = System.currentTimeMillis() + ".jpeg";

                            //   String fileName = createFileName();
                            filePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + fileName;
                            File cameraFile = new File(filePath);
//                            Uri fileURI = FileProvider.getUriForFile(MainActivity5.this,
//                                    "edu.usna.mobileos.cameraexamples.fileprovider",
//                                    cameraFile);

                            Uri fileURI = FileProvider.getUriForFile(MainActivity3.this,
                                    BuildConfig.APPLICATION_ID + ".provider", cameraFile);



                            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileURI);
                            // startActivityForResult(takePictureIntent, pic_id);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, pic_id);
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imageView.setImageBitmap(photo);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if (RC == pic_id) {


            // BitMap is data structure of image file
            // which store the image in memory
            photo = BitmapFactory.decodeFile(filePath);

            //photo = (Bitmap)I.getExtras()
//                    .get("data");

            imageView.setImageBitmap(photo);


            // Set the image in imageview for display


        }else{
            Toast.makeText(MainActivity3.this, "Nothing Received", Toast.LENGTH_LONG).show();
        }

    }

//    private Uri getCacheImagePath(String fileName) {
//        File path = new File(Temp);
//        if (!path.exists()) path.mkdirs();
//        File image = new File(path, fileName);
//        return getUriForFile(MainActivity5.this, getPackageName() + ".provider", image);
//    }






//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//
//        // Match the request 'pic id with requestCode
//        if (requestCode == pic_id) {
//
//            // BitMap is data structure of image file
//            // which stor the image in memory
//            Bitmap photo = (Bitmap)data.getExtras()
//                    .get("data");
//
//            // Set the image in imageview for display
//            imageView.setImageBitmap(photo);
//        }
//    }






    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG,50, byteArrayOutputStreamObject);

        photo.compress(Bitmap.CompressFormat.JPEG,50, byteArrayOutputStreamObject);




        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity3.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(MainActivity3.this,string1,Toast.LENGTH_LONG).show();

                // SettingNavigation image as transparent after done uploading.
                imageView.setImageResource(android.R.color.transparent);
                //  Intent intent = new Intent(MainActivity3.this,MainActivity1.class);
                Intent intent = new Intent(MainActivity3.this,MainActivity1.class);
                startActivity(intent);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);




            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageName, GetImageNameEditText);

                HashMapParams.put(ImagePath, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(file+"/Android/Payroll_and_Attendance_system/image-gallery/photos/ID/img_upload_to_server.php", HashMapParams);


                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, StandardCharsets.UTF_8));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }
    public void printMessage(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    private void readData() {
        try {
            FileInputStream fin = openFileInput(filename2);
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
        // printMessage("reading to file " + filename2 + " completed..");
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(MainActivity3.this, MainActivity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity3.this, FingerPrintStoreActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }



}