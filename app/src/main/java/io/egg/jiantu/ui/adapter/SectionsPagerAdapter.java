package io.egg.jiantu.ui.adapter;//package io.egg.jiantu.ui.adapter;
//
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.v13.app.FragmentPagerAdapter;
//
//import io.egg.jiantu.ui.fragment.HomeFragment;
//import io.egg.jiantu.ui.fragment.HomeFragment_;
//
///**
// * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a fragment corresponding to
// * one of the sections/tabs/pages.
// */
//public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//    private Context mContext;
//
//    private Bitmap mBitmap;
//
//    private HomeFragment mHomeFragment;
//
//    public SectionsPagerAdapter(Context pContext, FragmentManager fm) {
//        super(fm);
//
//        this.mContext = pContext;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//                mHomeFragment = HomeFragment_.builder().build();
//                return mHomeFragment;
//
//            default:
//                break;
//        }
//
//        return null;
//    }
//
//    @Override
//    public int getCount() {
//        return 1;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return "";
//    }
//
//}
