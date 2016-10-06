package io.egg.jiantu.ui.fragment;//package io.egg.jiantu.ui.fragment;
//
//import android.app.DownloadManager;
//import android.app.Fragment;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.google.gson.Gson;
//
//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EFragment;
//import org.androidannotations.annotations.ViewById;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import io.egg.jiantu.JiantuApplication;
//import io.egg.jiantu.R;
//import io.egg.jiantu.ui.CircleProgressBar;
//import io.egg.jiantu.ui.adapter.Downloader;
//import io.egg.jiantu.ui.adapter.Font;
//import io.egg.jiantu.ui.adapter.Fonts;
//import io.egg.jiantu.ui.adapter.FontsAdapter;
//
///**
// * Created by AirZcm on 15720.
// */
//
//@EFragment(R.layout.fragment_font_manager)
//public class FontManagerFragment extends Fragment {
//
//    @ViewById(R.id.ll)
//    LinearLayout ll;
//
//
////    public FontDownloadAsyncTask fontDownload;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @AfterViews
//    void afterViews() {
//        Log.i("TEST", "afterViews");
//        setRetainInstance(true);
//        String url = "http://7xj62a.com1.z0.glb.clouddn.com/v1/fontlist-jt-android.json";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Gson gson = new Gson();
//                final Fonts fonts = gson.fromJson(response.toString(), Fonts.class);
//                FontsAdapter fontsAdapter = new FontsAdapter(fonts.resources, getActivity());
//                for (int i = 0; i < fonts.resources.size(); i++) {
////                for (int i = 0; i < 1; i++) {
//                    View fv = fontsAdapter.getView(i, null, ll);
//                    ll.addView(fv);
////                    View divider = getActivity().getLayoutInflater().inflate(R.layout.divider, ll, false);
////                    if (i != fonts.resources.size() - 1) {
////                        ll.addView(divider);
////                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        JiantuApplication.getInstance().addToRequestQueue(jsonObjectRequest);
//    }
//
//    @Click(R.id.banner)
//    public void onBannerClicked() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addCategory(Intent.CATEGORY_BROWSABLE);
//        intent.setData(Uri.parse("http://www.nowapp.org"));
//        startActivity(intent);
//    }
//}
