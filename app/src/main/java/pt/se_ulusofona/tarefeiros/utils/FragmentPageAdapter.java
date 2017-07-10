package pt.se_ulusofona.tarefeiros.utils;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    public void addFragments(Fragment fragments) {
        this.fragments.add(fragments);
    }

    public FragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    //em que fragmento esta actualmente
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    //quantos fragmentos
    @Override
    public int getCount() {
        return fragments.size();
    }
}
