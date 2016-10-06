package io.egg.jiantu.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.enrique.stackblur.StackBlurManager;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.BeforeTextChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.SeekBarTouchStart;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.regex.Pattern;

import io.egg.jiantu.Constants;
import io.egg.jiantu.FontManager;
import io.egg.jiantu.R;
import io.egg.jiantu.analytics.GAUtil;
import io.egg.jiantu.manager.ImageManager;
import io.egg.jiantu.manager.QQSharingManager;
import io.egg.jiantu.manager.SinaWeiboSharingManager;
import io.egg.jiantu.manager.WeChatSharingManager;
import io.egg.jiantu.ui.EventDialog;
import io.egg.jiantu.ui.activity.CameraActivity_;
import io.egg.jiantu.ui.activity.FontManagerActivity_;
import io.egg.jiantu.ui.activity.LogActivity;
//import io.egg.jiantu.ui.activity.QzoneAlbumActivity;
import io.egg.jiantu.util.CameraUtil;
import io.egg.jiantu.util.Debug;
import io.egg.jiantu.util.DiskLruImageCache;
import io.egg.jiantu.util.FontUtil;
import io.egg.jiantu.util.GAConstant;
import io.egg.jiantu.util.ImageUtil;
import io.egg.jiantu.util.StringUtil;
import io.egg.jiantu.util.TextColorUtil;
import io.egg.jiantu.util.TextureData;
import io.egg.jiantu.util.TextureData.Texture;
import io.egg.jiantu.util.ViewUtil;
import io.egg.jiantu.wechat.WeChatEntity;
import io.egg.jiantu.widget.ResizeLayout;
import io.egg.jiantu.widget.VerticalSeekBar;
import io.egg.jiantu.widget.WaitProgressDialog;

import static android.widget.RelativeLayout.CENTER_HORIZONTAL;

@EFragment(R.layout.fragment_home)
@OptionsMenu(R.menu.camera_menu)
public class HomeFragment extends Fragment implements ResizeLayout.OnResizeListener,
        ImageManager.ImageSaveListener, Animation.AnimationListener {

    @ViewById(R.id.express_wording)
    EditText mWording;

    @ViewById(R.id.express_wording_editing)
    EditText mEditWording;

    @ViewById(R.id.hud)
    TextView hud;

    @ViewById(R.id.edit_stage_scrollview)
    ScrollView editScrollView;

    @ViewById(R.id.main_resize_layout)
    ResizeLayout mainResizeLayout;

    @ViewById(R.id.control_layout)
    LinearLayout controlLayout;

    @ViewById(R.id.util_layout)
    LinearLayout utilLayout;

    @ViewById(R.id.edit_stage)
    RelativeLayout mStage;

    @ViewById(R.id.img_view)
    ImageView mImageView;

    @ViewById(R.id.img_lightness_mask)
    ImageView mLightMaskView;

    @ViewById(R.id.home_texture_button)
    LinearLayout textureButton;

    @ViewById(R.id.home_color_button)
    LinearLayout colorButton;

    @ViewById(R.id.home_setting)
    ImageView settingButton;

    @ViewById(R.id.home_confirm_button)
    MaterialRippleLayout confirmButton;

    @ViewById(R.id.home_font_button)
    LinearLayout fontButton;

    @ViewById(R.id.home_delete_button)
    LinearLayout deleteButton;

    @ViewById(R.id.brightness_seekbar_up_arrow)
    ImageView mBrightnessSeekBarUpArrow;

    @ViewById(R.id.brightness_seekbar_down_arrow)
    ImageView mBrightnessSeekBarDownArrow;

    @ViewById(R.id.blur_seekbar_up_arrow)
    ImageView mBlurSeekBarUpArrow;

    @ViewById(R.id.blur_seekbar_down_arrow)
    ImageView mBlurSeekBarDownArrow;

    @AnimationRes(R.anim.seekbar_anim_out)
    Animation mBlurLayoutAnimOut;

    @AnimationRes(R.anim.seekbar_anim_out)
    Animation mBrightnessLayoutAnimOut;

    @ViewById(R.id.blur_seekbar)
    VerticalSeekBar mBlurSeekBar;

    @ViewById(R.id.brightness_seekbar)
    VerticalSeekBar mBrightnessSeekBar;

    @ViewById(R.id.brightness_seekbar_invisible_layout)
    View mBrightnessLayout;

    @ViewById(R.id.blur_seekbar_invisible_layout)
    View mBlurLayout;

    @ViewById(R.id.keyboard_toolbar)
    LinearLayout mKeyboardToolbar;

    @OptionsMenuItem(R.id.action_open_camera)
    MenuItem openCamera;

    @OptionsMenuItem(R.id.action_open_gallery)
    MenuItem openGallery;

    @DimensionPixelOffsetRes(R.dimen.button_diameter)
    int mUtilButtonHeight;

    @DimensionPixelOffsetRes(R.dimen.util_gap)
    int mUtilGap;

    @DimensionPixelOffsetRes(R.dimen.font_size_36px)
    int mFontSize36;

    @ColorRes(R.color.white)
    int wordingColor;

    @ColorRes(R.color.solid_white)
    int solidWhiteColor;

    @ColorRes(R.color.black)
    int blackColor;

    @ColorRes(R.color.hint_color_dark)
    int wordingHintColor;

    @AnimationRes(R.anim.indicator_alpha_anim_out)
    Animation showHudAnimation;

    private final static int TEXTURE_MODE = 1;
    private final static int PICTURE_MODE = 2;
    private int mode = TEXTURE_MODE;

    private boolean keyBoardIsShowing = false;

    private WaitProgressDialog waitProgressDialog;

    public static final int SELECT_NOT_CLIP = 10;

    private static Bitmap mImageBitmap;

    public static boolean isSeeking = false;

    private StackBlurManager mStackBlurManager;

    private ImageManager imageManager;

    private Bitmap backgroundBitmap = null;

    private Bitmap textureBitmap = null;

    private TextureData.BLEND_MODE mBlendMode = TextureData.BLEND_MODE.MULTIPLY;

    private int backgroundIndex = 0;

    private int textureIndex = -1;

    private int fontIndex = 0;

    private float mDownX;
    private float mDownY;
    private float mDownRawX;
    private float mDownRawY;
    private float mWordingY;
    private float mWordingX;
    private float mWordingOriginX;
    private float mWordingOriginY;
    private float mWordingMovedX;
    private float mWordingMovedY;
    private boolean isOnClick;

    private static int screenHeight;
    private static int screenWidth;

    private String imageFrom;

    public boolean isEdited = false;

    final QQSharingManager qqSharingManager = new QQSharingManager();

    public Intent intent;
    String path;
    Uri uri;

    Boolean isPopupWindowShow = false;

    private int lastRootViewDiff = -1;


    @AfterViews
    void afterViews() {
        setHasOptionsMenu(true);

        screenHeight = getResources().getDisplayMetrics().heightPixels;
        screenWidth = getResources().getDisplayMetrics().widthPixels;

        mainResizeLayout.setOnResizeListener(this);

        mImageView.setImageDrawable(new ColorDrawable(solidWhiteColor));

        Drawable drawable = mImageView.getDrawable();
        mImageBitmap = ViewUtil.drawableToBitmap(drawable);

        mLightMaskView.setImageDrawable(new ColorDrawable(blackColor));
        mLightMaskView.setAlpha(0);

        mWording.setHintTextColor(wordingHintColor);
        mWording.setText(getString(R.string.app_name));
        int mOneLineHeight = mWording.getLineHeight();
        mWording.setText("");
        mWording.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                resetWordingPosition();
            }
        });

        mWording.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(final View v, final int left, final int top, final int right, final int bottom, final int oldLeft, final int oldTop, final int oldRight, final int oldBottom) {
                mWordingOriginX = mWording.getX();
                mWordingOriginY = mWording.getY();
                mWordingMovedX = mWordingOriginX;
                mWordingMovedY = mWordingOriginY;
                mWording.removeOnLayoutChangeListener(this);
            }
        });

        mainResizeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = mainResizeLayout.getRootView().getHeight() - mainResizeLayout.getHeight();
                if (heightDiff == lastRootViewDiff) {
                    return;
                }

                lastRootViewDiff = heightDiff;
                if (heightDiff >= 150) {
                    onKeyBoardOn();


                } else {
                    onKeyBoardOff();
                }
            }
        });


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mWording.getLayoutParams();
        layoutParams.topMargin = ((screenWidth - mOneLineHeight) / 2);
        layoutParams.addRule(CENTER_HORIZONTAL);

        backgroundBitmap = TextureData.getBackgroundColorBitmap(getActivity(), backgroundIndex);

        mImageView.setImageBitmap(compositeImages());

        ViewUtil.changeViewHeight(mStage, screenWidth);

        ViewUtil.changeViewHeight(editScrollView, screenWidth);

        ViewUtil.changeViewHeight(mBrightnessLayout, screenWidth);
        ViewUtil.changeViewHeight(mBrightnessSeekBar, screenWidth * 6 / 10);

        ViewUtil.changeViewHeight(mBlurLayout, screenWidth);
        ViewUtil.changeViewHeight(mBlurSeekBar, screenWidth * 6 / 10);

        SinaWeiboSharingManager.init(getActivity());

        WeChatSharingManager.init(getActivity());
        QQSharingManager.init(getActivity());

        imageManager = new ImageManager();
        imageManager.setImageSaveListener(this);
        mBlurLayoutAnimOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBlurLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mBrightnessLayoutAnimOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBrightnessLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        FontManager.getFonts(getActivity());

        DiskLruImageCache cache = new DiskLruImageCache(getActivity());

        Bitmap bitmap = cache.getBitmap(Constants.CAMERA_TMP_FILE_NAME);

        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
            isEdited = true;
            mStackBlurManager = new StackBlurManager(bitmap);

            onImageSelected();

            cache.clearCache();

            imageFrom = GAConstant.remove_picture_label_camera;

            GAUtil.trackTakingPictrue(getActivity(), GAConstant.pick_picture_from_taking_picture_action_finish);
        }
    }

    @OnActivityResult(CameraUtil.PHOTO_REQUEST_GALLERY_HEADER)
    void onChoosePhotoFromGalleryResult(int resultCode, Intent data) {
        if (getActivity().isFinishing() || resultCode != -1) {
            if (resultCode == Activity.RESULT_CANCELED) {
                GAUtil.trackPickPictureFromLibrary(getActivity(), GAConstant.pick_picture_from_library_action_cancel);
            }

            return;
        }

        if (data != null) {
            ImageUtil.startPhotoZoom(data.getData(), this);
        }

        GAUtil.trackPickPictureFromLibrary(getActivity(), GAConstant.pick_picture_from_library_action_finish);
    }

    @OnActivityResult(CameraUtil.PHOTO_REQUEST_TAKEPHOTO_HEADER)
    void onTakePhotoResult(int resultCode) {
        if (getActivity().isFinishing() || resultCode != -1) {
            if (resultCode == Activity.RESULT_CANCELED) {
                GAUtil.trackPickPictureFromLibrary(getActivity(), GAConstant.pick_picture_from_library_action_cancel);
            }

            return;
        }

        SharedPreferences share = getActivity().getSharedPreferences(CameraUtil.SHARED_PREFERENCES_NAME, 0);
        String fileName = share.getString(CameraUtil.SHARED_CAMERA_EXTRA_OUTPUT, "");

        File tempFile = new File(CameraUtil.IMAGE_ORIGI_FILE_DIR, fileName);

        Uri uri = Uri.fromFile(tempFile);
        ImageUtil.startPhotoZoom(uri, this);

        GAUtil.trackPickPictureFromLibrary(getActivity(), GAConstant.pick_picture_from_library_action_finish);
    }

    @OnActivityResult(CameraUtil.PHOTO_REQUEST_CUT_HEADER)
    void onCutImageResult(int resultCode) {
        if (getActivity().isFinishing() || resultCode != -1) {
            return;
        }

        SharedPreferences shareTmp = getActivity().getSharedPreferences(CameraUtil.PIC_ZOOM_OUTPUT_DB, 0);

        String fileNameTmp = shareTmp.getString(CameraUtil.PIC_ZOOM_OUTPUT_FILE_KEY2, "");

        File file = new File(CameraUtil.IMAGE_FINAL_FILE_DIR, fileNameTmp);
        String path = file.getAbsolutePath();

        mImageBitmap = BitmapFactory.decodeFile(path);

        int width = mImageBitmap.getWidth();
        if (width != screenWidth) {
            mImageBitmap = handleCutBitmap(mImageBitmap);
        }

        mImageView.setImageBitmap(mImageBitmap);
        mStackBlurManager = new StackBlurManager(mImageBitmap);
        isEdited = true;
        file.delete();

        onImageSelected();

        imageFrom = GAConstant.remove_picture_label_library;
    }

    @OnActivityResult(SELECT_NOT_CLIP)
    void onSelectNoClipResult(int resultCode, Intent data) {
        if (getActivity().isFinishing() || resultCode != -1) {
            return;
        }

        Uri myUri = data.getData();
        if (myUri == null)
            return;

        String[] filePath = {MediaStore.Images.Media.DATA};
        String myPath = "";
        Cursor cursor = null;

        try {
            cursor = getActivity().getContentResolver().query(myUri, filePath, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                myPath = cursor.getString(columnIndex);
            } else {
                return;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        mImageBitmap = BitmapFactory.decodeFile(myPath);

        mImageView.setImageBitmap(mImageBitmap);
        isEdited = true;
        imageFrom = GAConstant.remove_picture_label_library;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @OptionsItem(R.id.action_open_camera)
    void openCamera() {
        Intent intent = new Intent(getActivity(), CameraActivity_.class);
        startActivity(intent);
        GAUtil.trackTakingPictrue(getActivity(), GAConstant.pick_picture_from_taking_picture_action_init);
    }

    @OptionsItem(R.id.action_open_gallery)
    void openGallery() {
        startImageCapture();
        GAUtil.trackPickPictureFromLibrary(getActivity(), GAConstant.pick_picture_from_library_action_init);
    }

    @Click(R.id.home_color)
    void onBackgroundButtonClicked() {
        textureButton.setVisibility(View.VISIBLE);
        backgroundIndex++;
        backgroundIndex = backgroundIndex % TextureData.BACKGROUD_COLOR_SIZE;
        backgroundBitmap = TextureData.getBackgroundColorBitmap(getActivity(), backgroundIndex);
        int colorValue = TextureData.getBackgroundColor(getActivity(), backgroundIndex).getColorValue();

        mImageView.setImageBitmap(compositeImages());

        onBgClick(backgroundIndex, colorValue, true);
        isEdited = true;

        GAUtil.trackPickColor(getActivity(), TextureData.getBackgroundColor(getActivity(), backgroundIndex).getColorName());
    }

    @Click(R.id.home_setting)
    void onSettingButtonClicked() {
        Intent intent = new Intent(getActivity(), FontManagerActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.home_gift)
    void onGiftButtonClicked() {
        EventDialog dialog = new EventDialog(getActivity());
        dialog.show();
//        Intent intent = new Intent(getActivity(), EventActivity_.class);
//        startActivity(intent);
    }

    @Click(R.id.home_texture)
    void onTextureButtonClicked() {
        textureIndex++;
        textureIndex = textureIndex % TextureData.TEXTURE_SIZE;
        Texture texture = TextureData.getTextureBitmap(getActivity(), textureIndex);
        textureBitmap = texture.getmBitmap();
        mBlendMode = texture.getBlendMode();
        mImageView.setImageBitmap(compositeImages());
        String textureName;
        if (textureIndex == 0) {
            textureName = "纯平";
        } else {
            textureName = textureIndex + "";
        }
        isEdited = true;
        GAUtil.trackPickTexture(getActivity(), textureName);
    }

    @Click(R.id.home_photo)
    void onTakePhotoButtonClicked() {
        getActivity().openOptionsMenu();

    }

    @Click(R.id.home_confirm_button)
    void onFinishButtonClicked() {
        if (!isPopupWindowShow){
            if (isEdited) {
                savePicMenuItemSelected();
            }
            isEdited = false;
            showPopupWindow();
            GAUtil.trackShare(getActivity(), GAConstant.share_init);
        }
    }

    @Click(R.id.home_font)
    void onFontButtonClicked() {
        int fontSize = FontUtil.FONT_SIZE + FontUtil.otherFonts.size();
        fontIndex++;
        fontIndex = fontIndex % (fontSize);

        FontUtil.JiantuFont font = FontUtil.getFont(getActivity(), fontIndex);

        String fontName;
        if (font != null) {
            mWording.setTypeface(font.getTypeFace());
            mEditWording.setTypeface(font.getTypeFace());
            fontName = font.getFontName();
        } else {
            mWording.setTypeface(null);
            mEditWording.setTypeface(null);
            fontName = "默认";

        }
        if (fontSize == 1){
            Intent intent = new Intent(getActivity(), FontManagerActivity_.class);
            startActivity(intent);
        }

        onFontClick(fontName);
        isEdited = true;
        GAUtil.trackPickFont(getActivity(), fontName);
    }

    @LongClick(R.id.home_font)
    void onFontButtonLongClicked(){
        Intent intent = new Intent(getActivity(), FontManagerActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.home_delete)
    void deleteButtonClicked() {
        onImageDeleted();
        isEdited = true;
    }

    @Click(R.id.img_view)
    void onImageViewClicked() {
        clickToWriteWord();
        isEdited = true;
    }

    @Click(R.id.home_log_button)
    void onLogButtonClicked() {
        Intent intent = new Intent(getActivity(), LogActivity.class);
        startActivity(intent);
    }

    private void clickToWriteWord() {
        if (!TextUtils.isEmpty(mWording.getHint().toString())) {
            mWording.setHint("");
        }

        mWording.requestFocus();
        mWording.setSelection(mWording.length());
        InputMethodManager imm = (InputMethodManager) mWording.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    @Click(R.id.align_left)
    void onAlignLeftButtonClicked() {
        mWording.setGravity(Gravity.LEFT);
        mEditWording.setGravity(Gravity.LEFT);
        textAlign();
    }

    @Click(R.id.align_center)
    void onAlignCenterButtonClicked() {
        mWording.setGravity(Gravity.CENTER);
        mEditWording.setGravity(Gravity.CENTER);
        textAlign();
    }

    @Click(R.id.align_right)
    void onAlignRightButtonClicked() {
        mWording.setGravity(Gravity.RIGHT);
        mEditWording.setGravity(Gravity.RIGHT);
        textAlign();
    }

    @Touch(R.id.express_wording)
    boolean onWordingTouched(MotionEvent event) {
        return onImageViewTouched(event);
    }

    @Touch(R.id.img_view)
    boolean onImageViewTouched(MotionEvent event) {
        if (keyBoardIsShowing) {
            return false;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mDownRawY = event.getRawY();
                mDownRawX = event.getRawX();
                mWordingY = mWording.getY();
                mWordingX = mWording.getX();

                isOnClick = true;

                mWording.getParent().requestDisallowInterceptTouchEvent(true);

                break;

            case MotionEvent.ACTION_CANCEL:

            case MotionEvent.ACTION_UP:
                if (isOnClick) {
                    clickToWriteWord();
                } else {
                    GAUtil.trackChangeTextPostion(getActivity());
                }

                mWording.getParent().requestDisallowInterceptTouchEvent(false);

                if (mBlurLayout.getVisibility() == View.VISIBLE) {
                    mBlurLayout.startAnimation(mBlurLayoutAnimOut);
                }

                if (mBrightnessLayout.getVisibility() == View.VISIBLE) {
                    mBrightnessLayout.startAnimation(mBrightnessLayoutAnimOut);
                }

                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = Math.abs(mDownX - event.getX());
                float moveY = Math.abs(mDownY - event.getY());

                float SCROLL_THRESHOLD = 10;
                if (isOnClick && (moveX > SCROLL_THRESHOLD || moveY > SCROLL_THRESHOLD)) {
                    isOnClick = false;
                }

                if (!keyBoardIsShowing && (moveY > SCROLL_THRESHOLD || moveX > SCROLL_THRESHOLD)) {
                    mWordingMovedX = mWordingX + event.getRawX() - mDownRawX;
                    mWordingMovedY = mWordingY + event.getRawY() - mDownRawY;
                    mWording.setX(mWordingMovedX);
                    mWording.setY(mWordingMovedY);

                    mWording.requestLayout();
                }

                break;

            default:
                break;
        }
        isEdited = true;
        return true;
    }

    @Override
    public void OnResize(int w, int h, int oldw, int oldh) {
        if (oldh == 0) {
            return;
        }

        if (oldh > h) {
            keyBoardIsShowing = true;

            GAUtil.trackEditInit(getActivity());
        } else {
            if (mWording.getText() == null || mWording.getText().length() == 0) {
                mWording.setHint(R.string.express_wording_hint);
            }

            keyBoardIsShowing = false;

            String wording = mWording.getText().toString();
            wording = StringUtil.trimString(wording);//wording.replaceAll("\n", " ");

            mWording.setText(wording);
            mWording.invalidate();

            GAUtil.trackEditFinish(getActivity());
        }
    }

    @Touch({R.id.brightness_seekbar_layout, R.id.blur_seekbar_layout})
    boolean onSeekBarLayoutTouched(View view, MotionEvent event) {
        if (mode == TEXTURE_MODE) {
            return true;
        }

        if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            if (view.getId() == R.id.brightness_seekbar_layout) {
                mBrightnessSeekBar.dispatchTouchEvent(event);
            } else if (view.getId() == R.id.blur_seekbar_layout) {
                mBlurSeekBar.dispatchTouchEvent(event);
            }
        } else if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mBlurLayout.setVisibility(View.VISIBLE);
            mBrightnessLayout.setVisibility(View.VISIBLE);
        }
        isEdited = true;
        return true;
    }

    @SeekBarTouchStart({R.id.blur_seekbar, R.id.brightness_seekbar})
    void onProgressStartOnSeekBar() {
        isSeeking = true;

        mBlurLayout.clearAnimation();
        mBlurLayout.setVisibility(View.VISIBLE);

        mBrightnessLayout.clearAnimation();
        mBrightnessLayout.setVisibility(View.VISIBLE);
    }

    @SeekBarProgressChange({R.id.blur_seekbar, R.id.brightness_seekbar})
    void onProgressChangeOnSeekBar(SeekBar seekBar, int progress) {
        mBlurLayout.clearAnimation();
        mBlurLayout.setVisibility(View.VISIBLE);

        mBrightnessLayout.clearAnimation();
        mBrightnessLayout.setVisibility(View.VISIBLE);

        switch (seekBar.getId()) {
            case R.id.blur_seekbar:
                onBlur(progress);

                resetBlurSeekBarArrowsCooridate(progress);

                break;

            case R.id.brightness_seekbar:
                int innerProgress = 100 - progress;

                int maxAlpha = (int) (0.5 * 255);
                int alpha = maxAlpha * innerProgress / 100;
                mLightMaskView.setAlpha(alpha);

                resetBrightnessSeekBarArrowsCooridate(progress);

                break;
        }
    }

    @SeekBarTouchStop({R.id.blur_seekbar, R.id.brightness_seekbar})
    void onProgressStopOnSeekBar(SeekBar seekBar) {
        isSeeking = false;

        if (seekBar.getId() == mBlurSeekBar.getId()) {
            long value = (long) mBlurSeekBar.getProgress() * 18 / 100;

            GAUtil.trackBlurImage(getActivity(), value);
        } else {
            long value = 50 - (long) (100 - mBrightnessSeekBar.getProgress()) * 50 / 100;

            GAUtil.trackDarkenImage(getActivity(), value);
        }

        mBlurLayout.startAnimation(mBlurLayoutAnimOut);
        mBrightnessLayout.startAnimation(mBrightnessLayoutAnimOut);
    }

    private void resetBlurSeekBarArrowsCooridate(int progress) {
        float seekBarCenterPos;

        Rect blurThumb = mBlurSeekBar.getSeekBarThumb().getBounds();

        seekBarCenterPos = (((mBlurSeekBar.getTop() - mBlurSeekBar.getBottom()) * mBlurSeekBar.getProgress()) / mBlurSeekBar.getMax()) + mBlurSeekBar.getBottom();
        float widthOffset = (blurThumb.width() - blurThumb.left) * mBlurSeekBar.getProgress() * 1.0f / mBlurSeekBar.getMax();
        seekBarCenterPos += widthOffset;

        int diff = ViewUtil.convertToPx(getActivity(), 4);

        mBlurSeekBarUpArrow.setY(seekBarCenterPos - blurThumb.width() - mBlurSeekBarUpArrow.getHeight() - diff);
        mBlurSeekBarDownArrow.setY(seekBarCenterPos + diff);

        if (progress >= 90) {
            mBlurSeekBarUpArrow.setVisibility(View.INVISIBLE);
            mBlurSeekBarDownArrow.setVisibility(View.VISIBLE);
        } else if (progress <= 10) {
            mBlurSeekBarUpArrow.setVisibility(View.VISIBLE);
            mBlurSeekBarDownArrow.setVisibility(View.INVISIBLE);
        } else {
            mBlurSeekBarUpArrow.setVisibility(View.VISIBLE);
            mBlurSeekBarDownArrow.setVisibility(View.VISIBLE);
        }
    }

    private void resetBrightnessSeekBarArrowsCooridate(int progress) {
        float seekBarCenterPos;

        Rect brightnessThumb = mBrightnessSeekBar.getSeekBarThumb().getBounds();

        seekBarCenterPos = (((mBrightnessSeekBar.getTop() - mBrightnessSeekBar.getBottom()) * mBrightnessSeekBar.getProgress()) / mBrightnessSeekBar.getMax()) + mBrightnessSeekBar.getBottom();
        float brightnessWidthOffset = (brightnessThumb.width() - brightnessThumb.left) * mBrightnessSeekBar.getProgress() * 1.0f / mBrightnessSeekBar.getMax();
        seekBarCenterPos += brightnessWidthOffset;

        int diff = ViewUtil.convertToPx(getActivity(), 4);

        mBrightnessSeekBarUpArrow.setY(seekBarCenterPos - brightnessThumb.width() - mBlurSeekBarUpArrow.getHeight() - diff);
        mBrightnessSeekBarDownArrow.setY(seekBarCenterPos + diff);

        if (progress >= 90) {
            mBrightnessSeekBarUpArrow.setVisibility(View.INVISIBLE);
            mBrightnessSeekBarDownArrow.setVisibility(View.VISIBLE);
        } else if (progress <= 10) {
            mBrightnessSeekBarUpArrow.setVisibility(View.VISIBLE);
            mBrightnessSeekBarDownArrow.setVisibility(View.INVISIBLE);
        } else {
            mBrightnessSeekBarUpArrow.setVisibility(View.VISIBLE);
            mBrightnessSeekBarDownArrow.setVisibility(View.VISIBLE);
        }
    }

    @BeforeTextChange(R.id.express_wording)
    void beforeTextChangesOnExpressWording() {
    }

    @TextChange(R.id.express_wording)
    void onTextChangesOnExpressWording(CharSequence text) {
        if (mode == PICTURE_MODE) {
            confirmButton.setVisibility(View.VISIBLE);
        } else {
            if (isNoWording()) {
                confirmButton.setVisibility(View.GONE);
            } else {
                confirmButton.setVisibility(View.VISIBLE);
            }
        }

        int size = calculateInputSize(String.valueOf(text));
        if (size > 240) {
            mWording.setText(text.subSequence(0, text.length() - 1));
            mWording.setSelection(mWording.length());
        }
        if (mWording.getLineCount() > 8) {
            mWording.setText(text.subSequence(0, text.length() - 1));
            mWording.setSelection(mWording.length());
        }
    }

    @AfterTextChange(R.id.express_wording)
    void afterTextChangesOnExpressWording() {
    }

    private boolean isNoWording() {
        return TextUtils.isEmpty(mWording.getText().toString());

    }

    private void onFontClick(String fontName) {
        showHud(fontName);

        mWording.invalidate();
    }

    private void onBgClick(int index, int colorValue, boolean needShowHud) {
        int hintColorValue = TextColorUtil.getSuitableHintColorValue(getActivity(), colorValue);
        mWording.setHintTextColor(hintColorValue);

        wordingColor = TextColorUtil.getSuitableTextColorValue(getActivity(), colorValue);
        mWording.setTextColor(wordingColor);

        if (needShowHud) {
            showHud(TextureData.getBackgroundColor(getActivity(), index).getColorName());
        }
    }

    private void showHud(String text) {
        hud.setText(text);

        showHudAnimation.setAnimationListener(this);

        hud.startAnimation(showHudAnimation);
    }

    private void onBlur(int pProgress) {
        try {
            Bitmap bitmap = mStackBlurManager.processNatively(pProgress);
            mImageView.setImageBitmap(bitmap);
        } catch (Exception ex) {
            Debug.e(ex);
        }
    }

    private void startImageCapture() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, CameraUtil.PHOTO_REQUEST_GALLERY_HEADER);
        } catch (Exception e) {
            GAUtil.trackPickPictureFromLibrary(getActivity(), GAConstant.pick_picture_from_library_action_fail);
        }
    }

    int calculateInputSize(String arg) {
        if (arg == null)
            return 0;

        final String CHINESE_REGEX_STR = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]{1}$";
        Pattern mMatchChinesePattern = Pattern.compile(CHINESE_REGEX_STR);

        float engCount = 0;
        int chinaCount = 0;
        int len = arg.length();
        for (int i = 0; i < len; i++) {
            String c = arg.substring(i, i + 1);
            if (mMatchChinesePattern.matcher(c).matches()) {
                chinaCount++;
            } else {
                engCount++;
            }
        }

        return chinaCount * 2 + (int) engCount;
    }

    private void onImageDeleted() {
        mImageView.setImageBitmap(compositeImages());
        //mImageBitmap = null;
        mLightMaskView.setAlpha(0);

        deleteButton.setVisibility(View.GONE);
        colorButton.setVisibility(View.VISIBLE);
        textureButton.setVisibility(View.VISIBLE);

        mBrightnessLayout.setVisibility(View.INVISIBLE);
        mBlurLayout.setVisibility(View.INVISIBLE);

        if (isNoWording()) {
            confirmButton.setVisibility(View.GONE);
        }

        mode = TEXTURE_MODE;

        int colorValue = TextureData.getBackgroundColor(getActivity(), backgroundIndex).getColorValue();
        onBgClick(backgroundIndex, colorValue, false);

        GAUtil.trackRemovePicture(getActivity(), imageFrom);
    }

    @Override
    public void onSaveStarted() {
        //合成一张图
        String colorName = TextureData.getBackgroundColor(getActivity(), backgroundIndex).getColorName();

        String fontName;
        FontUtil.JiantuFont font = FontUtil.getFont(getActivity(), fontIndex);
        if (font != null) {
            fontName = font.getFontName();
        } else {
            fontName = "默认";
        }

        String textureName;
        if (textureIndex == 0) {
            textureName = "纯平";
        } else {
            textureName = textureIndex + "";
        }

        if (mode == TEXTURE_MODE) {
            GAUtil.trackJiantuPropertiesWhenGenearatePicture(getActivity(), fontName, mWording.getText().toString().length(), GAConstant.without_image_label);
            GAUtil.trackJiantuPropertiesWhenGenearatePictureInColorTexture(getActivity(), colorName, textureName);
        } else {
            GAUtil.trackJiantuPropertiesWhenGenearatePicture(getActivity(), fontName, mWording.getText().toString().length(), GAConstant.with_image_label);

            String imageUsageLabel;
            if (TextUtils.isEmpty(mWording.getText().toString())) {
                imageUsageLabel = GAConstant.without_text_label;
            } else {
                imageUsageLabel = GAConstant.with_text_label;
            }

            long blur = (long) mBlurSeekBar.getProgress() * 18 / 100;
            long dark = 50 - (long) (100 - mBrightnessSeekBar.getProgress()) * 50 / 100;

            GAUtil.trackJiantuPropertiesWhenGenearatePictureInPhoto(getActivity(), imageUsageLabel, imageFrom, blur, dark);
        }
    }

    @Override
    public void onSaveFinished() {
//        TODO
    }

    public void showProgressBar() {
        if (waitProgressDialog == null) {
            waitProgressDialog = new WaitProgressDialog(getActivity());
        }

        waitProgressDialog.showWaitBox();
    }

    public void hideProgressBar() {
        if (waitProgressDialog == null) {
            return;
        }

        waitProgressDialog.closeWaitBox();
    }

    public static Bitmap handleCutBitmap(Bitmap bmp) {
        int THUMB_SIZE = screenWidth;//输出图片变成320pixle 320

        Bitmap newbm = null;
        if (bmp == null)
            return newbm;

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos1);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = ((float) THUMB_SIZE) / width;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

        return newbm;
    }

    private void onImageSelected() {
        mBrightnessLayout.setVisibility(View.VISIBLE);
        mBrightnessSeekBar.setProgress(0);
        resetBrightnessSeekBarArrowsCooridate(0);
        mBrightnessSeekBar.setProgress(100);
        mBrightnessLayout.startAnimation(mBrightnessLayoutAnimOut);

        mBlurLayout.setVisibility(View.VISIBLE);
        mBlurSeekBar.setProgress(0);
        resetBlurSeekBarArrowsCooridate(0);
        mBlurLayout.startAnimation(mBlurLayoutAnimOut);

        wordingColor = getResources().getColor(R.color.font_color_white);
        mWording.setTextColor(wordingColor);
        mWording.setHintTextColor(getResources().getColor(R.color.hint_color_light));

        colorButton.setVisibility(View.GONE);
        textureButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.VISIBLE);
        confirmButton.setVisibility(View.VISIBLE);

        mode = PICTURE_MODE;
    }

    /**
     * 图像的合成，可以通过在同一个Canvas中绘制两张图片。
     * 只是在绘制第二章图片的时候，需要给Paint指定一个变幻模式TransferMode。
     * 在Android中有一个XFermode所有的变幻模式都是这个类的子类
     * 我们需要用到它的一个子类PorterDuffXfermode,关于这个类，其中用到PorterDuff类
     * 这个类很简单，就包含一个Enum是Mode，其中定义了一组规则，这组规则就是如何将
     * 一张图像和另一种图像进行合成
     * 关于图像合成有四种模式，LIGHTEN,DRAKEN,MULTIPLY,SCREEN
     */
    private Bitmap compositeImages() {
        if (textureBitmap == null) {
            return backgroundBitmap;
        }

        if (backgroundBitmap == null) {
            return textureBitmap;
        }

        Bitmap bmp;
        //下面这个Bitmap中创建的函数就可以创建一个空的Bitmap
        bmp = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), backgroundBitmap.getConfig());
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bmp);
        //首先绘制第一张图片，很简单，就是和方法中getDstImage一样
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);

        //在绘制第二张图片的时候，我们需要指定一个Xfermode
        //这里采用Multiply模式，这个模式是将两张图片的对应的点的像素相乘
        //，再除以255，然后以新的像素来重新绘制显示合成后的图像
        switch (mBlendMode) {
            case MULTIPLY:
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
                break;
            case SCREEN:
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
                break;
        }

        canvas.drawBitmap(textureBitmap, 0, 0, paint);//dstBitmap

        return bmp;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == null) {
            return;
        }

        if (animation.equals(showHudAnimation)) {
            hud.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == null) {
            return;
        }

        if (animation.equals(showHudAnimation)) {
            hud.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void resetWordingPosition() {
        if (mWording.getY() < mStage.getTop()) {
            mWording.setY(mStage.getTop());
        }

        float mWordingBottom = mWording.getY() + mWording.getMeasuredHeight();

        if (mWordingBottom > mStage.getBottom()) {
            mWording.setY(mStage.getBottom() - mWording.getMeasuredHeight());
        }
    }

    private void showPopupWindow() {

        isPopupWindowShow = true;

        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.layout_share, null, false);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopupWindowShow = false;
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.anim_sharepanel);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

        LinearLayout shareToWechat = (LinearLayout) contentView.findViewById(R.id.share_to_wechat);
        LinearLayout shareToWechatFriend = (LinearLayout) contentView.findViewById(R.id.share_to_wechat_friend);
        LinearLayout shareToWeibo = (LinearLayout) contentView.findViewById(R.id.share_to_sina_weibo);
        LinearLayout shareToQZone = (LinearLayout) contentView.findViewById(R.id.share_to_qzone);
        LinearLayout shareMore = (LinearLayout) contentView.findViewById(R.id.share_more);

        shareToWechat.setOnClickListener(new ShareListener());
        shareToWechatFriend.setOnClickListener(new ShareListener());
        shareToWeibo.setOnClickListener(new ShareListener());
        shareToQZone.setOnClickListener(new ShareListener());
        shareMore.setOnClickListener(new ShareListener());

        if (WeChatEntity.checkWechatVersion(getActivity(), WeChatSharingManager.getApi(getActivity()))) {
            shareToWechat.setVisibility(View.VISIBLE);
            shareToWechatFriend.setVisibility(View.VISIBLE);
        } else {
            shareToWechat.setVisibility(View.GONE);
            shareToWechatFriend.setVisibility(View.GONE);
        }
        if (SinaWeiboSharingManager.isWeiboAppInstalled()) {
            shareToWeibo.setVisibility(View.VISIBLE);
        } else {
            shareToWeibo.setVisibility(View.GONE);
        }
//        if (qqSharingManager.isPkgInstalled(pkgName)){
//            shareToQZone.setVisibility(View.VISIBLE);
//        }else{
//            shareToQZone.setVisibility(View.GONE);
//        }
    }

    class ShareListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            beforeSaveOrShare();
            Bitmap bitmap = ImageUtil.getBitmapFromView(getActivity(), mStage);
            switch (view.getId()) {
                case R.id.share_to_wechat:
                    WeChatSharingManager.sendToFriend(getActivity(), bitmap);
                    GAUtil.trackShare(getActivity(), GAConstant.share_to_wechat_session_init);
                    break;
                case R.id.share_to_wechat_friend:
                    WeChatSharingManager.sendToTimeline(getActivity(), bitmap);
                    GAUtil.trackShare(getActivity(), GAConstant.share_to_wechat_timeline_init);
                    break;
                case R.id.share_to_sina_weibo:
                    SinaWeiboSharingManager.send(mWording.getText().toString(), bitmap);
                    GAUtil.trackShare(getActivity(), GAConstant.share_to_weibo_init);
                    break;
                case R.id.share_to_qzone:
//                    intent = new Intent(getActivity(), QzoneAlbumActivity_.class);
//                    intent.putExtra("ImageUri", uri.toString());
//                    intent.putExtra("ImagePath", path);
//
//                    if (!qqSharingManager.ready()) {
//                        qqSharingManager.loginByQQ(getActivity());
//
//                    } else {
//                        startActivity(intent);
//                    }
                    qqSharingManager.onClickShare(path, getActivity());


                    break;
                case R.id.share_more:
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                    break;
                default:
                    break;
            }
            afterSaveOrShare();
        }
    }

    void savePicMenuItemSelected() {
        beforeSaveOrShare();

        GAUtil.trackShare(getActivity(), GAConstant.save_to_camera_roll_init);

        path = imageManager.saveViewToSDCard(getActivity(), mStage);

        uri = Uri.parse(path);

        GAUtil.trackShare(getActivity(), GAConstant.save_to_camera_roll_finish);

        afterSaveOrShare();
    }

    private void beforeSaveOrShare() {
        showProgressBar();

        if (TextUtils.isEmpty(mWording.getText())) {
            mWording.setHint("");
        }
        mWording.invalidate();
    }

    private void afterSaveOrShare() {
        if (TextUtils.isEmpty(mWording.getText())) {
            mWording.setHint(R.string.express_wording_hint);
        }
        hideProgressBar();
    }

    private void onKeyBoardOn() {
        mKeyboardToolbar.setVisibility(View.VISIBLE);
        settingButton.setVisibility(View.GONE);
        mWording.setVisibility(View.GONE);
        mEditWording.setVisibility(View.VISIBLE);
        mEditWording.setCursorVisible(true);
        mEditWording.requestFocus();
        mEditWording.setText(mWording.getText().toString());
        mEditWording.setSelection(mWording.length());
        mEditWording.getBackground().setAlpha(100);

    }

    private void onKeyBoardOff() {
        mKeyboardToolbar.setVisibility(View.GONE);
        settingButton.setVisibility(View.VISIBLE);
        mEditWording.setVisibility(View.GONE);
        mWording.setVisibility(View.VISIBLE);
        mWording.setText(mEditWording.getText().toString());

    }

    private void textAlign(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenDpi = dm.densityDpi;

        if (mWording.getX()<0){
            mWording.setX(0);
        }else if (mWording.getX() + mWording.getWidth() > ((screenDpi * 360) / 160)) {
            mWording.setX(screenDpi * 360 / 160 - mWording.getWidth());
        }
    }
}
