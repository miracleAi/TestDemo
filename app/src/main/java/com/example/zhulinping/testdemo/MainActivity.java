package com.example.zhulinping.testdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private TextView clickTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clickTv = (TextView) findViewById(R.id.click);
        clickTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            saveImageToGallery(getApplicationContext(),bitmap);
        }
    }

    /*public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(android.os.Environment.getExternalStorageDirectory(), "jiantu/简图/");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String path = appDir.getAbsoluteFile()+"/";
        String fileName = path + System.currentTimeMillis() + ".jpg";
        OutputStream fileOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(fileName);
            byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // bm is the bitmap object
            byte[] bsResized = byteArrayOutputStream.toByteArray();
            fileOutputStream.write(bsResized);
        } catch (IOException e) {
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
            }
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(fileName))));
        MediaScannerConnection.scanFile(context, new String[]{fileName}, null, null);

    }*/
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "jiantu/简图/");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "123.jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
