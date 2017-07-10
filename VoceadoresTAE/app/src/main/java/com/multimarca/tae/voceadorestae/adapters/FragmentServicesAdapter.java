package com.multimarca.tae.voceadorestae.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.multimarca.tae.voceadorestae.Fragments.ResumeServices;
import com.multimarca.tae.voceadorestae.Fragments.ServicesFragment;

/**
 * Created by erick on 12/20/15. Multimarca
 */
public class FragmentServicesAdapter extends FragmentPagerAdapter {

    public FragmentServicesAdapter(FragmentManager fm) {
        super(fm);
        Log.d("TAG", "aqui ando");
    }


    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position position item
     */
    @Override
    public Fragment getItem(int position) {
        Log.d("item", "item: " + String.valueOf(position));
        switch (position) {
            case 0:
                return ServicesFragment.newInstance();
            case 1:
                return ResumeServices.newInstance();
            default:
                return ResumeServices.newInstance();
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 2;
    }


}