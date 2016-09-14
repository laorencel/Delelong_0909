package com.delelong.diandian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.delelong.diandian.listener.MyOrientationListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.jpush.android.api.JPushInterface;


public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BAIDUMAPFORTEST";
//    public static final String URL_LOGIN = "http://121.40.142.141:8090/Jfinal/api/login";
    public static final String APPTYPE_CLIENT = "2";
    public static final String DEVICE_TYPE = "1";
    private String registrationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initJPush();
    }

    //////////////////////////////////////////////////////////////////////////定位
    /**
     * 定位到我的位置
     */
    /**
     * 定位到我的位置
     */
    public void centerToMyLocation(AMap aMap, AMapLocationClient mLocationClient, MyOrientationListener myOrientationListener,double myLatitude, double myLongitude) {
        if (myOrientationListener != null){
            if(!myOrientationListener.isStarted()){
                myOrientationListener.start();
            }
        }
        if (mLocationClient != null){
            if (!mLocationClient.isStarted()){
                mLocationClient.startLocation();
            }
        }
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(
                myLatitude, myLongitude)));
    }

    public <T> void intentActivityForResult(Context context, Class<T> tClass, String key, String value, String city, int requestCode) {
        Intent intent = new Intent(context, tClass);
        intent.putExtra(key, value);
        intent.putExtra("city", city);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 带bundle的界面跳转
     *
     * @param context
     * @param tClass
     * @param bundle
     * @param <T>
     */
    public <T> void intentActivityWithBundle(Context context, Class<T> tClass, Bundle bundle) {
        Intent intent = new Intent(context, tClass);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    /**
     * @return 获取手机序列号
     */
    public String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }
    ///////////////////////////////////////////图片获取与存储
    /**
     * 把数据写入文件
     * @param filePath
     * @param fileName
     * @param bitmap
     * @return 文件路径
     */
    public String createImage(String filePath, String fileName, Bitmap bitmap) {
        String path = filePath+fileName;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);// 把数据写入文件
            Toast.makeText(this, "保存图片"+fileName, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
    /**
     * 获取相机返回的图片
     * @param data
     * @param bitmap
     * @return Bitmap
     */
    public Bitmap getCamera(Intent data, Bitmap bitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i(TAG, "SD card is not avaiable/writeable right now.");
            return null;
        }
        Bundle bundle = data.getExtras();
        bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        return bitmap;
    }

    /**
     * 获取相册返回的图片
     * @param data Intent
     * @param bitmap
     * @return Bitmap
     */
    public Bitmap getAlbum(Intent data, Bitmap bitmap) {
        Uri selectedImage = data.getData();
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
        String imagePath = cursor.getString(columnIndex);
        bitmap = BitmapFactory.decodeFile(imagePath);
        cursor.close();
        return bitmap;
    }
    public static class MyHeadTask extends AsyncTask<String, Void, Bitmap> {
        ImageView img_head;
        public MyHeadTask(ImageView img_head){
            this.img_head = img_head;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            URL url_head_portrait = null;
            Bitmap head = null;
            try {
                url_head_portrait = new URL(params[0] + params[1]);
                HttpURLConnection conn = (HttpURLConnection) url_head_portrait.openConnection();
                conn.setConnectTimeout(8000);
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                head = BitmapFactory.decodeStream(is);
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return head;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                img_head.setImageBitmap(bitmap);
            }
        }
    }
    /////////////////////////////////////////////////////////////////极光推送
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }

    private void initJPush() {
        JPushInterface.init(this);
        registerMessageReceiver();
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//                if (!ExampleUtil.isEmpty(extras)) {
                showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//                }
                Log.i(TAG, "onReceive: showMsg:" + showMsg.toString());
            }
        }
    }
}
