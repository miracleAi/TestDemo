package io.egg.jiantu.ui.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;

import javax.xml.transform.Result;

import io.egg.jiantu.R;
import io.egg.jiantu.ui.CircleProgressBar;
import io.egg.jiantu.ui.activity.FontManagerActivity;

/**
 * Created by AirZcm on 15724.
 */

public class FontDownloadAsyncTask extends AsyncTask<String, String, String> {

    Context mContext;

    Vector progressObserver = new Vector();

    private FontDownloadCallback callback;

//    public FontDownloadAsyncTask(Context context, CircleProgressBar circleProgressBar, ImageView imageView, String string){
//        this.mContext = context;
//        this.circleProgressBar = circleProgressBar;
//        this.mImageView = imageView;
//        this.fontName = string;
//    }

    public FontDownloadAsyncTask(Context mContext, FontDownloadCallback callback){
        this.mContext = mContext;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... aurl) {
        File tempFile = new File(mContext.getFilesDir(), aurl[1] + ".tmp");
        File file = new File(mContext.getFilesDir(), aurl[1] + ".ttf");
        try {
            //连接地址
            URL u = new URL(aurl[0]);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            //计算文件长度
            int lengthOfFile = c.getContentLength();


            FileOutputStream f = new FileOutputStream(tempFile);

            InputStream in = c.getInputStream();

            //下载的代码
            byte[] buffer = new byte[1024];
            int len1 = 0;
            long total = 0;

            while ((len1 = in.read(buffer)) > 0) {
                total += len1; //total = total + len1
                publishProgress("" + (int) ((total * 100) / lengthOfFile));
                f.write(buffer, 0, len1);
            }
            tempFile.renameTo(file);
            f.close();

        } catch (Exception e) {
            tempFile.delete();
            return "failure";

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
//        circleProgressBar.setProgress(Integer.parseInt(progress[0]));
        callback.taskProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String str) {
//        if (TextUtils.isEmpty(str)) {
//            circleProgressBar.setVisibility(View.GONE);
//            mImageView.setVisibility(View.VISIBLE);
//            mImageView.setOnClickListener(null);
//            mImageView.setImageResource(R.drawable.font_complete);
//            Toast.makeText(mContext, fontName + "下载完成", Toast.LENGTH_SHORT).show();
//        }
        callback.Result(str);
    }
}
