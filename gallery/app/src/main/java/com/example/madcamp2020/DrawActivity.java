package com.example.madcamp2020;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class DrawActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    // 그림을 그릴 CustomView
    private DrawingView drawingView;

    // 색상 선택 버튼
    private ImageButton[] colorImageButtons;

    // 초기화 버튼, 저장 버튼
    private Button resetButton, saveButton;

    ImageView sticker;

    float oldXvalue;
    float oldYvalue;

    float getXvalue;
    float getYvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        tedPermission();

        sticker = (ImageView) findViewById(R.id.sticker);
        // 변수 초기화
        drawingView = (DrawingView) findViewById(R.id.drawingView);
/*
        Intent intent = getIntent();
        byte[] arr = getIntent().getByteArrayExtra("image");
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        Drawable d = new BitmapDrawable(getResources(), image);

        drawingView.setBackground(d);
*/
        Intent intent = getIntent();
        byte[] b = intent.getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

        Drawable d = new BitmapDrawable(getResources(), bitmap);

        drawingView.setBackground(d);

        colorImageButtons = new ImageButton[3];
        colorImageButtons[0] = (ImageButton) findViewById(R.id.blackColorBtn);
        colorImageButtons[1] = (ImageButton) findViewById(R.id.redColorBtn);
        colorImageButtons[2] = (ImageButton) findViewById(R.id.blueColorBtn);
        for (ImageButton colorImageButton : colorImageButtons) {
            colorImageButton.setOnClickListener(this);
        }

        resetButton = (Button) findViewById(R.id.resetBtn);
        resetButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(this);

        sticker.setOnTouchListener(this);

    }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //권한 요청 성공 : 여기 코드 작성
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int width = ((ViewGroup) v.getParent()).getWidth() - v.getWidth();
        int height = ((ViewGroup) v.getParent()).getHeight() - v.getHeight();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            oldXvalue = event.getX();
            oldYvalue = event.getY();
            //  Log.i("Tag1", "Action Down X" + event.getX() + "," + event.getY());
            Log.i("Tag1", "Action Down rX " + event.getRawX() + "," + event.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            v.setX(event.getRawX() - oldXvalue);
            v.setY(event.getRawY() - (oldYvalue + v.getHeight()));
            //  Log.i("Tag2", "Action Down " + me.getRawX() + "," + me.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (v.getX() > width && v.getY() > height) {
                v.setX(width);
                v.setY(height);
            } else if (v.getX() < 0 && v.getY() > height) {
                v.setX(0);
                v.setY(height);
            } else if (v.getX() > width && v.getY() < 0) {
                v.setX(width);
                v.setY(0);
            } else if (v.getX() < 0 && v.getY() < 0) {
                v.setX(0);
                v.setY(0);
            } else if (v.getX() < 0 || v.getX() > width) {
                if (v.getX() < 0) {
                    v.setX(0);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                } else {
                    v.setX(width);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                }
            } else if (v.getY() < 0 || v.getY() > height) {
                if (v.getY() < 0) {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(0);
                } else {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(height);
                }
            }

            getXvalue = v.getX();
            getYvalue = v.getY();

        }
        return true;

}
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blackColorBtn:
                drawingView.setColor(Color.BLACK);
                break;
            case R.id.redColorBtn:
                drawingView.setColor(Color.RED);
                break;
            case R.id.blueColorBtn:
                drawingView.setColor(Color.BLUE);
                break;
            case R.id.resetBtn:
                drawingView.reset();
                break;
            case R.id.saveBtn:
                Bitmap draw = drawingView.save(DrawActivity.this);
                Log.d("fragment4", draw.toString());

                Bitmap stickerdraw = BitmapFactory.decodeResource(this.getResources(), R.drawable.sticker);
                Bitmap mergedraw =createSingleImageFromMultipleImages(draw, stickerdraw);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mergedraw.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byte[] bytes = stream.toByteArray();

                MediaStore.Images.Media.insertImage(this.getContentResolver(),draw, "Title", null);

                Intent intent = new Intent();
                intent.putExtra("draw", bytes);
                setResult(RESULT_OK, intent);

                finish();
                break;
        }
    }


    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage){

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, getXvalue, getYvalue, null);

        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "Title_" + timeStamp + "_";

        try {
            File storageDir = new File(Environment.getExternalStorageDirectory(), "/miniworld/");
            if (!storageDir.exists()) storageDir.mkdirs();

            // 파일 생성
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

            OutputStream outStream = new FileOutputStream(image);
            result.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

            // 갤러리에 변경을 알려줌
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 안드로이드 버전이 Kitkat 이상 일때
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(image);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            } else {
                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }

            Toast.makeText(this.getApplicationContext(), "저장완료", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
