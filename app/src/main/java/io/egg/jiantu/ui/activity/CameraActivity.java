package io.egg.jiantu.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.egg.jiantu.Constants;
import io.egg.jiantu.R;
import io.egg.jiantu.analytics.GAUtil;
import io.egg.jiantu.util.CameraUtil;
import io.egg.jiantu.util.Debug;
import io.egg.jiantu.util.DiskLruImageCache;
import io.egg.jiantu.util.GAConstant;
import io.egg.jiantu.util.ImageUtil;
import io.egg.jiantu.util.ViewUtil;
import io.egg.jiantu.widget.PreviewFrameLayout;

@EActivity(R.layout.activity_camera)
public class CameraActivity extends Activity implements
        SurfaceHolder.Callback,
        Camera.PictureCallback,
        Animation.AnimationListener {

    @ViewById(R.id.preview_surface_view)
    SurfaceView mCameraView;

    @ViewById(R.id.preview_layout)
    PreviewFrameLayout mPreviewLayout;

    @ViewById(R.id.camera_operation_layout)
    View mOperationLayout;

    @ViewById(R.id.camera_focus)
    ImageView mFocusView;

    @AnimationRes(R.anim.camera_foucs_anim_out)
    Animation mFocusAnimation;

    private SurfaceHolder mSurfaceHolder;

    private Camera mCamera;

    private int currentCameraId;

    private boolean mPreviewRunning;

    private ProgressDialog mDiaDebug;

    private float mDist;

    private int mZoomFactor = 100;

    private boolean pointerCountBiggerThanOne = false;

    @AfterViews
    void afterViews() {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        int statusBarHeight = ViewUtil.getStatusBarHeight(this);

        ViewUtil.changeViewHeight(mOperationLayout, screenHeight - screenWidth - statusBarHeight);

        currentCameraId = CameraInfo.CAMERA_FACING_BACK;

        mSurfaceHolder = mCameraView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override protected void onPause() {
        super.onPause();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            Debug.e(e);

            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            mCamera.setPreviewDisplay(holder);
            updateCameraParameters();
            Parameters p = mCamera.getParameters();
            p.setFocusMode(Parameters.FOCUS_MODE_AUTO);

            mCamera.setParameters(p);
            mCamera.startPreview();

            mCamera.autoFocus(null);

        } catch (Exception e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }

        mPreviewRunning = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }

        mPreviewRunning = false;
    }

    @Touch(R.id.preview_surface_view)
    void onCameraViewTouched(MotionEvent motionEvent) {
        Parameters params = mCamera.getParameters();

        if (motionEvent.getPointerCount() > 1) {
            pointerCountBiggerThanOne = true;

            if (motionEvent.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
                mDist = getFingerSpacing(motionEvent);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && params.isZoomSupported()) {
                mCamera.cancelAutoFocus();
                handleZoom(motionEvent, params);
            }
        } else {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                pointerCountBiggerThanOne = false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (pointerCountBiggerThanOne) {
                    return;
                }

                mFocusView.setX(motionEvent.getX() - mFocusView.getWidth() / 2);
                mFocusView.setY(motionEvent.getY() - mFocusView.getHeight() / 2);

                mFocusAnimation.setAnimationListener(this);
                mFocusView.startAnimation(mFocusAnimation);

                float x = motionEvent.getX();
                float y = motionEvent.getY();
                float touchMajor = motionEvent.getTouchMajor();
                float touchMinor = motionEvent.getTouchMinor();

                Rect touchRect = new Rect(
                        (int) (x - touchMajor / 2),
                        (int) (y - touchMinor / 2),
                        (int) (x + touchMajor / 2),
                        (int) (y + touchMinor / 2));

                final Rect targetFocusRect = new Rect(
                        touchRect.left * 2000 / mCameraView.getWidth() - 1000,
                        touchRect.top * 2000 / mCameraView.getHeight() - 1000,
                        touchRect.right * 2000 / mCameraView.getWidth() - 1000,
                        touchRect.bottom * 2000 / mCameraView.getHeight() - 1000);

                final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
                Camera.Area focusArea = new Camera.Area(targetFocusRect, 1000);
                focusList.add(focusArea);

                Parameters para = mCamera.getParameters();
                para.setFocusAreas(focusList);
                para.setMeteringAreas(focusList);
                try {
                    mCamera.setParameters(para);
                    mCamera.autoFocus(null);
                } catch (Exception ex) {
                    Debug.e(ex);
                }

                if (currentCameraId == CameraInfo.CAMERA_FACING_BACK) {
                    GAUtil.trackFocusCamera(this, GAConstant.focus_camera_label_rear, (long) mZoomFactor);
                } else {
                    GAUtil.trackFocusCamera(this, GAConstant.focus_camera_label_front, (long) mZoomFactor);
                }
            }
        }
    }

    private void handleZoom(MotionEvent event, Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }

        mDist = newDist;

        params.setZoom(zoom);

        try {
            mCamera.setParameters(params);
            mZoomFactor = params.getZoomRatios().get(zoom);
        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    private float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    @Click(R.id.camera_take_photo_button)
    void onTakePhotoButtonClicked() {
        if (mCamera == null) {
            return;
        }

        mCamera.takePicture(shutterCallback, null, this);
    }

    @Click(R.id.camera_cancel_button)
    void onCancelButtonClicked() {
        GAUtil.trackTakingPictrue(this, GAConstant.pick_picture_from_taking_picture_action_cancel);

        finish();
    }

    @Click(R.id.camera_change_camera_button)
    void onChangeCameraButtonClicked() {
        switchCameraAndPreview();
    }

    @Override
    public void onPictureTaken(final byte[] data, final Camera camera) {
        mDiaDebug = ProgressDialog.show(CameraActivity.this, "", "");

        onPictureTake(data, camera);
    }

    public void onPictureTake(byte[] data, Camera camera) {
        Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        if (bitmapWidth > bitmapHeight) {
            if (currentCameraId == CameraInfo.CAMERA_FACING_BACK) {
                mBitmap = ImageUtil.rotate(mBitmap, 90);
            } else {
                mBitmap = ImageUtil.rotate(mBitmap, 270);
            }

            bitmapWidth = mBitmap.getWidth();
            bitmapHeight = mBitmap.getHeight();
        }

        int clipSize;

        if (bitmapWidth <= bitmapHeight) {
            clipSize = bitmapWidth;
        } else {
            clipSize = bitmapHeight;
        }

        if (currentCameraId == CameraInfo.CAMERA_FACING_BACK) {
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, clipSize, clipSize);
        } else {
            Matrix rotateRight = new Matrix();
            rotateRight.preRotate(90);

            float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
            rotateRight = new Matrix();
            Matrix matrixMirrorY = new Matrix();
            matrixMirrorY.setValues(mirrorY);

            rotateRight.postConcat(matrixMirrorY);

            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, clipSize, clipSize, rotateRight, true);
        }

        DiskLruImageCache cache = new DiskLruImageCache(this);
        cache.put(Constants.CAMERA_TMP_FILE_NAME, mBitmap);
        mBitmap.recycle();

        mDiaDebug.dismiss();

        finish();
    }

    private void switchCameraAndPreview() {
        if (mPreviewRunning) {
            mCamera.stopPreview();
        }

        mCamera.release();

        if (currentCameraId == CameraInfo.CAMERA_FACING_BACK) {
            currentCameraId = CameraInfo.CAMERA_FACING_FRONT;

            GAUtil.trackSwitchToCamera(this, GAConstant.switch_to_camera_label_front);
        } else {
            currentCameraId = CameraInfo.CAMERA_FACING_BACK;

            GAUtil.trackSwitchToCamera(this, GAConstant.switch_to_camera_label_rear);
        }

        mCamera = Camera.open(currentCameraId);

        try {
            //摄像头画面显示在Surface上
            mCamera.setPreviewDisplay(mSurfaceHolder);
            updateCameraParameters();
            mCamera.startPreview();
        } catch (IOException e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }

        if (mCamera == null) {
            return;
        }

        updateCameraParameters();

        mCamera.startPreview();

        mPreviewRunning = true;
    }

    private void updateCameraParameters() {
        if (mCamera != null) {
            Parameters parameters = mCamera.getParameters();

            int degrees = 0;
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_0:
                    mCamera.setDisplayOrientation(90);
                    degrees = 0;
                    break;

                case Surface.ROTATION_90:
                    degrees = 90;
                    break;

                case Surface.ROTATION_180:
                    degrees = 180;
                    break;

                case Surface.ROTATION_270:
                    mCamera.setDisplayOrientation(180);
                    degrees = 270;
                    break;

                default:
                    break;
            }

            CameraInfo mCameraInfo = new CameraInfo();
            Camera.getCameraInfo(currentCameraId, mCameraInfo);


            int rotate = (mCameraInfo.orientation - degrees + 360) % 360;
            parameters.setRotation(rotate);

            long time = new Date().getTime();
            parameters.setGpsTimestamp(time);

            Size pictureSize = CameraUtil.findBestPictureSize(this, mCamera, parameters);
            if (pictureSize != null) {
                parameters.setPictureSize(pictureSize.width, pictureSize.height);
            }

            // Set the preview frame aspect ratio according to the picture size.
            Size size = parameters.getPictureSize();
            mPreviewLayout.setAspectRatio((double) size.width / size.height);

            Size previewSize = CameraUtil.findBestPreviewSize(this, mCamera, parameters);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            try {
                mCamera.setParameters(parameters);
            } catch (Exception ex) {
                Debug.e(ex);
            }

            int supportPreviewWidth = previewSize.width;
            int supportPreviewHeight = previewSize.height;

            int srcWidth = CameraUtil.getScreenWH(this).widthPixels;
            int srcHeight = CameraUtil.getScreenWH(this).heightPixels;

            int width = Math.min(srcWidth, srcHeight);
            int height = width * supportPreviewWidth / supportPreviewHeight;

            mCameraView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == null) {
            return;
        }

        if (animation.equals(mFocusAnimation)) {
            mFocusView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == null) {
            return;
        }

        if (animation.equals(mFocusAnimation)) {
            mFocusView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public boolean onKeyUp(int pKeyCode, KeyEvent pEvent) {
        if (pKeyCode == KeyEvent.KEYCODE_BACK) {
            GAUtil.trackTakingPictrue(this, GAConstant.pick_picture_from_taking_picture_action_cancel);
        }

        return super.onKeyUp(pKeyCode, pEvent);
    }

    private final Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };

}
