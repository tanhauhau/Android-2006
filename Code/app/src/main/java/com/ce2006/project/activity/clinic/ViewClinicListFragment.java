package com.ce2006.project.activity.clinic;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ce2006.project.adapter.ClinicListArrayAdapter;
import com.ce2006.project.model.Clinic;
import com.ce2006.project.server.ClinicManager;
import com.example.user.ce2006_project.R;

/**
 * View list of clinics
 * Created by lhtan on 31/3/15.
 */
public class ViewClinicListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ClinicManager builder;
    private ListView listView;
    private View progressBar;

    private LoadClinicTask task;
    private Listener listener;

    public static ViewClinicListFragment getFragment(ClinicManager builder) {
        ViewClinicListFragment fragment = new ViewClinicListFragment();
        fragment.builder = builder;
        return fragment;
    }

    public static ViewClinicListFragment getFragment(ClinicManager builder, Listener listener) {
        ViewClinicListFragment fragment = new ViewClinicListFragment();
        fragment.builder = builder;
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (listener == null) {
            try {
                listener = (Listener) activity;
            } catch (Exception e) {
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_list, null);
        listView = (ListView) view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progressBar);

        task = new LoadClinicTask(this, builder);
        task.execute();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        task.cancel(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.clinicSelected((Clinic) parent.getAdapter().getItem(position));
    }

    public static interface Listener {
        public void clinicSelected(Clinic clinic);
    }

    /**
     * Subclass of {@link android.os.AsyncTask}that calls
     * {@link com.ce2006.project.server.ClinicManager#getClinicList()}
     * and load the clinics into listview via
     * {@link com.ce2006.project.adapter.ClinicListArrayAdapter}
     */
    private class LoadClinicTask extends AsyncTask<Void, Void, Clinic[]> {
        private AdapterView.OnItemClickListener listener;
        private ClinicManager builder;

        private LoadClinicTask(AdapterView.OnItemClickListener listener, ClinicManager builder) {
            this.listener = listener;
            this.builder = builder;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Clinic[] doInBackground(Void... params) {
            return builder.getClinicList();
        }

        @Override
        protected void onPostExecute(Clinic[] clinics) {
            if (clinics != null) {
                listView.setAdapter(new ClinicListArrayAdapter(getActivity(), clinics));
                listView.setOnItemClickListener(listener);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}