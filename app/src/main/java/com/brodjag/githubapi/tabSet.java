package com.brodjag.githubapi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brodjag on 04.03.16.
 */
public class tabSet {
    MainActivity activity;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    private Map<Integer, String> mFragmentTags = new HashMap<Integer, String>();;

    ActionBar actionBar;

    public tabSet(MainActivity mainActivity){
        activity=mainActivity;

        pager=(ViewPager) activity.findViewById(R.id.pager);
        pagerAdapter=new mPageAdaper(activity.getSupportFragmentManager());




        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //// Asking for the default ActionBar element that our platform supports.

        setTabs();

    }



    void setTabs(){

        actionBar= activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab Tab1 = actionBar.newTab().setText("A-H");
        ActionBar.Tab Tab2 = actionBar.newTab().setText("I-P");
        ActionBar.Tab Tab3 = actionBar.newTab().setText("Q-Z");


        Tab1.setTabListener(new myTabListener(0));
        Tab2.setTabListener(new myTabListener(1));
        Tab3.setTabListener(new myTabListener(2));


        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
        actionBar.addTab(Tab3);
    }

    class  myTabListener implements ActionBar.TabListener{
        Fragment fragment;
        public myTabListener(int position){
            fragment=BlankFragment.newInstance(position);
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
            //ft.replace(R.id.contaner,fragment);
            pager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
            //ft.remove(fragment);

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

        }
    }


    public class mPageAdaper extends FragmentStatePagerAdapter {
        public mPageAdaper(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment=BlankFragment.newInstance(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "pos "+position;
        }




    }







}
