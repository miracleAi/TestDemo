package io.egg.jiantu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import io.egg.jiantu.JiantuApplication;
import io.egg.jiantu.R;
import io.egg.jiantu.ui.adapter.Fonts;
import io.egg.jiantu.ui.adapter.FontsAdapter;
//import io.egg.jiantu.ui.fragment.FontManagerFragment;
//import io.egg.jiantu.ui.fragment.FontManagerFragment_;


@EActivity(R.layout.activity_font_manager)
public class FontManagerActivity extends Activity {
//    private FontManagerFragment mFontManagerFragment;

//    @AfterViews
//    void afterViews(){
//        if (mFontManagerFragment == null) {
//            mFontManagerFragment = FontManagerFragment_.builder().build();
//            getFragmentManager().beginTransaction().add(R.id.font_list_fragment, mFontManagerFragment).commit();
//        }
//    }

    @ViewById(R.id.ll)
    LinearLayout ll;

    @ViewById(R.id.network_error)
    LinearLayout networkError;

    @AfterViews
    void afterViews() {
        fontRequest();
    }

//    @Click(R.id.banner)
//    public void onBannerClicked() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addCategory(Intent.CATEGORY_BROWSABLE);
//        intent.setData(Uri.parse("http://www.nowapp.org"));
//        startActivity(intent);
//    }

    @Click(R.id.retry_button)
    public void retryFontListRequest(){
        fontRequest();
    }

    void fontRequest(){
        networkError.setVisibility(View.GONE);
        String url = "http://7xj62a.com1.z0.glb.clouddn.com/v1/fontlist-jt-android.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Fonts fonts = gson.fromJson(response.toString(), Fonts.class);
                FontsAdapter fontsAdapter = new FontsAdapter(fonts.resources, FontManagerActivity.this);
                for (int i = 0; i < fonts.resources.size(); i++) {
                    View fv = fontsAdapter.getView(i, null, ll);
                    ll.addView(fv);
                    View divider = getLayoutInflater().inflate(R.layout.divider, ll, false);
                    if (i != fonts.resources.size() - 1) {
                        ll.addView(divider);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkError.setVisibility(View.VISIBLE);
            }
        });

        JiantuApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
