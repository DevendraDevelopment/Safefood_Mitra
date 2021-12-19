package com.safefoodmitra.safefoodmitra.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.safefoodmitra.safefoodmitra.Fragments.RecivedFragment;
import com.safefoodmitra.safefoodmitra.Fragments.SentFragment;
import com.safefoodmitra.safefoodmitra.Fragments.VerifyFragment;


public class Viewadapter extends FragmentPagerAdapter {

    public Viewadapter(FragmentManager fm) {
        super(fm);

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment =null;
        if (position ==0){
            fragment=new SentFragment();

        }
        else if (position==1){
            fragment=new RecivedFragment();

        }

       /* else if (position==2){
            fragment=new VerifyFragment();

        }*/


        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title =null;
        if (position==0){
            title="SENT";
        }
        else if (position==1){
            title="RECEIVED";
        }

        /*else if (position==2){
            title="VERIFIED";
        }*/

        return title;
    }
}
