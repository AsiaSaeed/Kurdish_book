package com.example.myapplication;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class View_Page_Adpt_c extends FragmentPagerAdapter {

private  final List<Fragment>fragmentList=new ArrayList<>();
private final List<String>FragmentListTitle=new ArrayList<>();




    public View_Page_Adpt_c(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        return fragmentList.get(position);


    }

    @Override
    public int getCount() {


        return FragmentListTitle.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentListTitle.get(position);
    }

    public  void AddFragment (Fragment fragment, String Title){

        fragmentList.add(fragment);
        FragmentListTitle.add(Title);


    }
}

