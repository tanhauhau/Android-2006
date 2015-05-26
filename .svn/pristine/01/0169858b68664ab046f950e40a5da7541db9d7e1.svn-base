package com.ce2006.project.activity.firstaid;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.user.ce2006_project.R;

/**
 * Showing list of first aid in a grid view
 */
public class FirstAidListFragment extends Fragment implements AdapterView.OnItemClickListener {
    Listener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (Listener) activity;
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_aid_list, null);
        GridView grid = (GridView) view.findViewById(R.id.gridview);
        grid.setAdapter(new ImageAdapter(getActivity()));

        grid.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.onItemClicked(position);
    }

    static interface Listener {
        public void onItemClicked(int position);
    }
}
