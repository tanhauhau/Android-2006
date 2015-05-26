package com.ce2006.project.activity.firstaid;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.user.ce2006_project.R;

/**
 * first aid activity,
 * showing various first aid guide in a grid view
 */
public class FirstAidActivity extends ActionBarActivity implements FirstAidListFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        getFragmentManager().beginTransaction()
                .addToBackStack("list")
                .add(R.id.container, new FirstAidListFragment())
                .commit();
    }

    /**
     * show details of the first aid
     * @param position
     */
    @Override
    public void onItemClicked(int position) {
        TypedArray imgs = getResources().obtainTypedArray(R.array.first_aid_img);
        TypedArray descs = getResources().obtainTypedArray(R.array.first_aid_desc);
        String[] titles = getResources().getStringArray(R.array.first_aid_titles);
        FirstAidFragment fragment =
                FirstAidFragment.getFragment(imgs.getResourceId(position, -1),
                        descs.getText(position),
                        titles[position]);
        imgs.recycle();
        getFragmentManager()
                .beginTransaction()
                .addToBackStack("detail")
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
