package io.egg.jiantu.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import io.egg.jiantu.R;

/**
 * @author AirZcm on 20151222.
 */
public class EventDialog extends Dialog {

    private String url = "http://7jppmi.com1.z0.glb.clouddn.com/weibo/postcard.jpg";
    private String eTag = null;

    public EventDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_event);

        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(0));


        ImageView eventImage = (ImageView) findViewById(R.id.event_image);
        Picasso.with(getContext())
                .load(url)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.event_error)
                .into(eventImage);

        ImageView dismiss = (ImageView) findViewById(R.id.dialog_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDialog.this.dismiss();
            }
        });
    }
}
