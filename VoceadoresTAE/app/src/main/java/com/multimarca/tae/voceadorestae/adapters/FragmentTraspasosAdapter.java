package com.multimarca.tae.voceadorestae.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.multimarca.tae.voceadorestae.Fragments.RTraspasosFragment;
import com.multimarca.tae.voceadorestae.Fragments.RTraspasosTableFragment;

/**
 * Created by erick on 1/11/16. Multimarca
 */
public class FragmentTraspasosAdapter extends FragmentPagerAdapter {

    public FragmentTraspasosAdapter(FragmentManager fm) {
        super(fm);
        Log.d("TAG", "aqui ando");
    }


    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position item
     */
    @Override
    public Fragment getItem(int position) {
        Log.d("item", "item: " + String.valueOf(position));

        RTraspasosFragment rtraspasosForm = RTraspasosFragment.newInstance();
        RTraspasosTableFragment rtraspasosTable = RTraspasosTableFragment.newInstance();
        switch (position) {
            case 0:
                return rtraspasosForm;
            case 1:
                return rtraspasosTable;
            default:
                return rtraspasosTable;
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
