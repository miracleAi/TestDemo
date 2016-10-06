package io.egg.jiantu.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import io.egg.jiantu.R;

public class WaitProgressDialog extends ProgressDialog {
	private Activity mActivity = null;
	

	public WaitProgressDialog(Activity a) {
		super(a);
		
		mActivity = a;
	}
	
	public void showWaitBox() {
		if (!mActivity.isFinishing()) {
			show();
		}
	}
	
	public void closeWaitBox() {
		if (!mActivity.isFinishing()) {
			dismiss();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setCancelable(true);

		setContentView(R.layout.layout_progress_diag);
	}

	public void show() {
		try {
			super.show();
		} catch (Throwable e) {
		}
	}

	public void dismiss() {
		try {
			super.dismiss();
		} catch (Exception e) {
		}
	}
}
