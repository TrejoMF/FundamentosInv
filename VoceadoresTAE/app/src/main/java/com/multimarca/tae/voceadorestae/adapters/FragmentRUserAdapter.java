package com.multimarca.tae.voceadorestae.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.multimarca.tae.voceadorestae.Fragments.RAppFragment;
import com.multimarca.tae.voceadorestae.Fragments.RComisionesFragment;
import com.multimarca.tae.voceadorestae.Fragments.RUserFragment;
import com.multimarca.tae.voceadorestae.Fragments.RUserResumeFragment;

/**
 * Created by Home on 6/28/16.
 */
public class FragmentRUserAdapter extends FragmentPagerAdapter {


    public FragmentRUserAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 3:
                return RUserResumeFragment.newInstance();
            case 2:
                return RAppFragment.newInstance();
            case 1:
                return RComisionesFragment.newInstance();
            case 0:
                return RUserFragment.newInstance();
            default:
                return RComisionesFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
