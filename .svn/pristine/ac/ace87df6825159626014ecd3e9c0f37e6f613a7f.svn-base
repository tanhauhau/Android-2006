package com.ce2006.project.activity.doctor;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ce2006.project.adapter.DoctorListArrayAdapter;
import com.ce2006.project.model.Doctor;
import com.ce2006.project.server.DoctorManager;
import com.example.user.ce2006_project.R;

/**
 * View list of doctors
 * Created by lhtan on 31/3/15.
 */
public class ViewDoctorListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private DoctorManager builder;
    private ListView listView;
    private View progressBar;

    private LoadDoctorTask task;
    private Listener listener;

    public static ViewDoctorListFragment getFragment(DoctorManager builder) {
        ViewDoctorListFragment fragment = new ViewDoctorListFragment();
        fragment.builder = builder;
        return fragment;
    }

    public static ViewDoctorListFragment getFragment(DoctorManager builder, Listener listener) {
        ViewDoctorListFragment fragment = new ViewDoctorListFragment();
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

        task = new LoadDoctorTask(this, builder);
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
        listener.doctorSelected((Doctor) parent.getAdapter().getItem(position));
    }

    public static interface Listener {
        public void doctorSelected(Doctor doctor);
    }

    /**
     * Subclass of {@link android.os.AsyncTask} that calls
     * {@link com.ce2006.project.server.DoctorManager#getDoctorList()}
     * and loads the doctors into listview via
     * {@link com.ce2006.project.adapter.DoctorListArrayAdapter}
     */
    private class LoadDoctorTask extends AsyncTask<Void, Void, Doctor[]> {
        private AdapterView.OnItemClickListener listener;
        private DoctorManager builder;

        private LoadDoctorTask(AdapterView.OnItemClickListener listener, DoctorManager builder) {
            this.listener = listener;
            this.builder = builder;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Doctor[] doInBackground(Void... params) {
            return builder.getDoctorList();
        }

        @Override
        protected void onPostExecute(Doctor[] doctors) {
            if (doctors != null) {
                listView.setAdapter(new DoctorListArrayAdapter(getActivity(), doctors));
                listView.setOnItemClickListener(listener);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}