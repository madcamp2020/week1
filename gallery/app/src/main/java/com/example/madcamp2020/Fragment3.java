package com.example.madcamp2020;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class Fragment3 extends Fragment {

    private static final String TAG = "blackjin";

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private static final int DRAW_PIC = 3;

    private File tempFile;

    ImageView imageView;

    Bitmap originalBm;


    private ArrayList<Contacts> list = ContactsList.getInstance();
    private Spinner spinner;
    private ArrayList<String> namelist;
    private ArrayAdapter<String> arrayAdapter;
    private int idx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 //       tedPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_3, container, false);

        spinner = v.findViewById(R.id.spinner2);
        namelist = new ArrayList<>();
        int count = 0;
        while (list.size() > count) {
            Contacts item = list.get(count);
            namelist.add("(" + item.nickname + ") " + item.name);
            count++;
        }
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, namelist);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), namelist.get(position) + " was chosen", Toast.LENGTH_SHORT).show();
                idx = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        imageView = v.findViewById(R.id.addimage);

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try{
                    //이부분 startactivity를 startactivityforresult로 수정
                    Intent intent = new Intent(getActivity().getApplicationContext(), DrawActivity.class);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    originalBm.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                    byte[] b = stream.toByteArray();

                    intent.putExtra("image", b);
                    startActivityForResult(intent, DRAW_PIC);
                }catch(Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), "사진을 먼저 선택해 주세요", Toast.LENGTH_LONG).show();
                }

            }
        });

        TextView send = (TextView) v.findViewById(R.id.sender);
        EditText textSMS = (EditText) v.findViewById(R.id.message);
        send.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String phoneNo = list.get(idx).phNumbers;
                String sms = textSMS.getText().toString();
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    ArrayList<Image> fileList = getfiles();

                    fileList = (ArrayList<Image>) fileList.stream().filter(t -> t.bucket_Name.equals("Pictures") && t.name.contains("Title")).collect(Collectors.toList());
                    //sendFile = new File(cursor.getString(1));
                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(fileList.get(fileList.size()-1).getUri()));

                    //cursor.close();
                    sendMMS(phoneNo, bitmap, sms);
                    Toast.makeText(getActivity().getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "SMS failed, please try again later!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        TextView textgal = v.findViewById(R.id.getphoto);
        TextView textcam = v.findViewById(R.id.takephoto);

        textgal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isPermission) goToAlbum();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_1), Toast.LENGTH_LONG).show();
            }
        });

        textcam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isPermission) takePhoto();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d( "취소 되었습니다." , "tq");

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));


            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            try {
                setImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == PICK_FROM_CAMERA) {

            try {
                setImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (requestCode == DRAW_PIC){
            //여기 자체가 실행 안됨
            byte[] arr = data.getByteArrayExtra("draw");
            Log.d("fragment3", arr.toString());
            Bitmap drawedimage = BitmapFactory.decodeByteArray(arr, 0, arr.length);
            imageView.setImageBitmap(drawedimage);
        }
    }

    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
//            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
            e.printStackTrace();
        }
        if (tempFile != null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(getContext().getApplicationContext(),
                        "com.example.madcamp2020.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }

    public void sendMMS(String phone, Bitmap bitmap, String content) {

        Log.d(TAG, "sendMMS(Method) : " + "start");

        String subject = "제목";
        String text = content;

        // 예시 (절대경로) : String imagePath = "/storage/emulated/0/Pictures/Screenshots/Screenshot_20190312-181007.png";

        //String imagePath = sendFile.getAbsolutePath();

        Log.d(TAG, "subject : " + subject);
        Log.d(TAG, "text : " + text);
        //Log.d(TAG, "imagePath : " + imagePath);

        Settings settings = new Settings();
        settings.setUseSystemSending(true);

        // TODO : 이 Transaction 클래스를 위에 링크에서 다운받아서 써야함
        Transaction transaction = new Transaction(getActivity(), settings);

        // 제목이 있을경우
        Message message = new Message(text, phone, subject);

        // 제목이 없을경우
        // Message message = new Message(text, number);

//        if (!(imagePath.equals("") || imagePath == null)) {
//            // 예시2 (앱 내부 리소스) :
//            // Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mms_test_1);
//            Bitmap mBitmap = BitmapFactory.decodeFile(imagePath);
//            // TODO : image를 여러장 넣고 싶은경우, addImage를 계속호출해서 넣으면됨
//            message.addImage(mBitmap);
//        }
        message.addImage(bitmap);

        long id = Process.getThreadPriority(Process.myTid());

        transaction.sendNewMessage(message, id);
    }



    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "blackJin_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/blackJin/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }


    /**
     *  tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        imageView.setImageBitmap(originalBm);

        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         */
        tempFile = null;

    }

    class Image {
        private final Uri uri;
        private final String name;
        private final int size;
        private final Long date;
        private final Long bucket_ID;
        private final String bucket_Name;

        public Uri getUri() {
            return uri;
        }



        public Image(Uri uri, String name, int size, Long date, Long bucket_ID, String bucket_Name) {
            this.uri = uri;
            this.name = name;
            this.size = size;
            this.date = date;
            this.bucket_ID = bucket_ID;
            this.bucket_Name = bucket_Name;
        }
    }

    public ArrayList<Image> getfiles() {
        ArrayList<Image> ImageList = new ArrayList<Image>();

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };

        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        try (Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
            int bucketIDColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            int bucketNameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            //int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);


            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int size = cursor.getInt(sizeColumn);
                Long date = cursor.getLong(dateColumn);
                String bucketname = cursor.getString(bucketNameCol);
                Long bucketID = cursor.getLong(bucketIDColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                ImageList.add(new Image(contentUri, name, size, date, bucketID, bucketname));
            }
            return ImageList;

        }

    }

}