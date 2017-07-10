package com.multimarca.tae.voceadorestae.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.multimarca.tae.voceadorestae.Fragments.DepositosDataFragment;
import com.multimarca.tae.voceadorestae.Fragments.DepositosFieldsFragment;
import com.multimarca.tae.voceadorestae.Fragments.DepositosFormFragment;


/**
 * Created by erick on 12/20/15. Multimarca
 */
public class FragmentDepositosAdapter extends FragmentPagerAdapter {

    public FragmentDepositosAdapter(FragmentManager fm) {
        super(fm);
    }


    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position position item
     */
    @Override
    public Fragment getItem(int position) {
        DepositosDataFragment depData = DepositosDataFragment.newInstance();
        DepositosFieldsFragment depField = DepositosFieldsFragment.newInstance();
        DepositosFormFragment depForm = DepositosFormFragment.newInstance();
        switch (position) {
            case 0:
                return depForm;
            case 1:
                return depField;
            case 2:
                return depData;
            default:
                return DepositosDataFragment.newInstance();
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