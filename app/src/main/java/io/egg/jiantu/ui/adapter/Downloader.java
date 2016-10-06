package io.egg.jiantu.ui.adapter;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AirZcm on 15730.
 */
public class Downloader {


    private static Downloader instance = null;

    public static Downloader getInstance() {
        if (null == instance) {
            instance = new Downloader();
        }
        return instance;
    }

    private ArrayList<WeakReference<ProgressListener>> listeners = new ArrayList<>();
    public Map<String, AsyncTask<String, String, String>> mTaskMap = new HashMap<>();

    public void setProgressListener(ProgressListener l) {
        listeners.add(new WeakReference<ProgressListener>(l));
    }

    public void removeProgressListener(ProgressListener l){
        listeners.remove(this);
    }




    public AsyncTask<String, String, String> download(final Context context, final int id, final String url) {
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            private int lengthOfFile = 0;

            @Override
            protected String doInBackground(String... args) {
                File tempFile = new File(context.getFilesDir(), args[1] + ".tmp");
                File file = new File(context.getFilesDir(), args[1] + ".ttf");
                try {
                    //连接地址
                    URL u = new URL(args[0]);
                    HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.connect();
                    //计算文件长度
                    lengthOfFile = connection.getContentLength();
                    FileOutputStream f = new FileOutputStream(tempFile);
                    InputStream in = connection.getInputStream();
                    //下载的代码
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    long total = 0;
                    while ((len1 = in.read(buffer)) > 0) {
                        total += len1; //total = total + len1
                        publishProgress(String.valueOf(id), url, "" + (int) ((total * 100) / lengthOfFile));
                        f.write(buffer, 0, len1);
                    }
                    tempFile.renameTo(file);
                    f.close();
                } catch (Exception e) {
                    tempFile.delete();
                    return "failure";
                } finally {
                    mTaskMap.remove(url);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (lengthOfFile != 0) {
                    for (int i = 0; i < listeners.size(); ++i) {
                        ProgressListener l = listeners.get(i).get();
                        if (l != null)
                            l.onProgress(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]));
                    }
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                if (!isCancelled()){
                    cancel(true);
                }
            }
        };
        mTaskMap.put(url, task);
        return task;
    }


    public interface ProgressListener {
        void onProgress(int id, String url, int progress);
    }
}
