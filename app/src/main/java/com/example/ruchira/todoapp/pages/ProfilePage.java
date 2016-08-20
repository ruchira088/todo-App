package com.example.ruchira.todoapp.pages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ruchira.todoapp.Function;
import com.example.ruchira.todoapp.R;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class ProfilePage extends AppCompatActivity
{
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private static final int REQUEST_PERMISSION_FOR_EXTERNAL_STORAGE = 200;

    private Button m_takePhotoButton;

    private Uri m_fileUri;

    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        m_takePhotoButton = (Button) findViewById(R.id.takePhotoButton);

        m_takePhotoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                Function<Void, File> getOutputMediaFile = new Function<Void, File>()
                {
                    @Override
                    public File apply(Void p_input)
                    {
                        String fileName = String.valueOf(new Date().getTime()) + ".jpg";
                        File file = new File(getExternalStoragePublicDirectory(DIRECTORY_PICTURES), fileName);

                        return file;
                    }
                };

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                m_fileUri = Uri.fromFile(getOutputMediaFile.apply(null));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, m_fileUri);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        Button skipButton = (Button) findViewById(R.id.skipButton);

        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("title", "Hello World").build();
                Request request = new Request.Builder().url("http://httpbin.org/post").post(multipartBody).build();

                OkHttpClient httpClient = new OkHttpClient();

                httpClient.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onFailure(Call p_call, IOException p_e)
                    {

                    }

                    @Override
                    public void onResponse(Call p_call, Response p_response) throws IOException
                    {
                        System.out.println(p_response.body().string());
                    }
                });


            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            m_takePhotoButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_FOR_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION_FOR_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            m_takePhotoButton.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int p_requestCode, int p_resultCode, Intent p_data)
    {
        super.onActivityResult(p_requestCode, p_resultCode, p_data);

        if(p_requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && p_resultCode == RESULT_OK)
        {
            ImageView profileImageView = (ImageView) findViewById(R.id.profileImageView);
            profileImageView.setImageURI(m_fileUri);

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, m_fileUri);
            sendBroadcast(intent);
        }
    }
}
