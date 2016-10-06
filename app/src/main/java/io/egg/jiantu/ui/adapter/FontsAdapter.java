package io.egg.jiantu.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.Executors;

import io.egg.jiantu.FontManager;
import io.egg.jiantu.JiantuApplication;
import io.egg.jiantu.R;
import io.egg.jiantu.ui.CircleProgressBar;

import static java.lang.Integer.parseInt;

/**
 * Created by jiajixin on 15/6/26.
 */
public class FontsAdapter extends BaseAdapter {
    ArrayList<Font> mResources;
    Context mContext;

    private Downloader downloader;

    public FontDownloadAsyncTask fontDownload;

    public FontsAdapter(ArrayList resources, Context context) {
        mResources = resources;
        mContext = context;
        downloader = Downloader.getInstance();
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public Font getItem(int position) {
        return mResources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final Font font = getItem(position);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.item_font, parent, false);

        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.imageview);
        ImageLoader imageLoader = JiantuApplication.getInstance().getImageLoader();
        imageView.setImageUrl(font.thumbnail_url.x3, imageLoader);

        final CircleProgressBar circleProgressBar = (CircleProgressBar) view.findViewById(R.id.status_dl);

        final ImageView mDownloadImageView = (ImageView) view.findViewById(R.id.download);

        final ImageView downloadSuccess = (ImageView)view.findViewById(R.id.download_success);

        if (FontManager.isExist(mContext, font.id, font.display_name)) {
            downloader.setProgressListener(null);
            mDownloadImageView.setImageResource(R.drawable.font_more);

        } else {
            mDownloadImageView.setImageResource(R.drawable.font_cloud);

        }
        downloader.setProgressListener(new Downloader.ProgressListener() {
            @Override
            public void onProgress(int id, String url, int progress) {
                if (id == position) {
                    circleProgressBar.setVisibility(View.VISIBLE);
                    mDownloadImageView.setVisibility(View.GONE);
                    circleProgressBar.setProgress(progress);
                    if (progress == 100) {
                        circleProgressBar.setVisibility(View.GONE);
//                        mDownloadImageView.setVisibility(View.VISIBLE);
                        downloadSuccess.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, font.display_name + "下载完成", Toast.LENGTH_SHORT).show();
                        downloader.removeProgressListener(this);
                        downloader.mTaskMap.remove(url);
                    }
                }

            }
        });

        mDownloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FontManager.isExist(mContext, font.id, font.display_name)) {
//                    downloader.setProgressListener(null);
                    downloader.mTaskMap.remove(font.resource_url);

                    new AlertDialog.Builder(mContext)
                            .setMessage("移除字体？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            FontManager.deleteFont(mContext, font.id, font.display_name);
                                            mDownloadImageView.setImageResource(R.drawable.font_cloud);
                                        }
                                    }).setNegativeButton("取消", null).create()
                            .show();

                } else {
                    mDownloadImageView.setVisibility(View.GONE);
                    circleProgressBar.setVisibility(View.VISIBLE);

                    AsyncTask<String, String, String> task = downloader.download(mContext, position, font.resource_url);

//                    fontDownload = new FontDownloadAsyncTask(mContext, circleProgressBar, mDownloadImageView, font.display_name);
//                    fontDownload = new FontDownloadAsyncTask(mContext, new FontDownloadCallback() {
//                        @Override
//                        public void taskProgress(String... progress) {
//                            circleProgressBar.setProgress(Integer.parseInt(progress[0]));
//                        }
//
//                        @Override
//                        public void Result(String result) {
//                            if (TextUtils.isEmpty(result)) {
//                                circleProgressBar.setVisibility(View.GONE);
//                                mDownloadImageView.setVisibility(View.VISIBLE);
//                                mDownloadImageView.setOnClickListener(null);
//                                mDownloadImageView.setImageResource(R.drawable.font_complete);
//                                Toast.makeText(mContext, font.display_name + "下载完成", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                    fontDownload.executeOnExecutor(Executors.newCachedThreadPool(),font.resource_url,font.id+"i"+font.display_name);
                    task.executeOnExecutor(Executors.newCachedThreadPool(), font.resource_url, font.id + "-" + font.display_name);
                }
            }
        });
        return view;
    }
}
