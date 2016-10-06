package io.egg.jiantu.qzone;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import io.egg.jiantu.R;

public class TencentUtil {
	private static Dialog mProgressDialog;

	public static final void showResultDialog(Context context, String msg) {
		if(msg == null) return;
		String rmsg = msg.replace(",", "\n");
		Log.d("TencentUtil", rmsg);
		Toast.makeText(context, rmsg, Toast.LENGTH_SHORT).show();
	}


	public static ProgressDialog createProgressDialog(Context mContext) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		try {
			dialog.show();
		} catch (WindowManager.BadTokenException e) {

		}
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog_progress);
		dialog.setCancelable(true);
		return dialog;
	}

	public static final void dismissDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

}
