package com.ce2006.project.activity.clinic;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.model.Clinic;
import com.ce2006.project.model.Credential;
import com.ce2006.project.server.ClinicManager;
import com.example.user.ce2006_project.R;

/**
 * Activity that show lists of clinics and allow user to click to see details of it
 * and options to modify or delete the clinic
 */
public class ViewModifyClinicActivity extends ActionBarActivity implements ViewClinicListFragment.Listener, ModifyClinicFragment.Listener, FragmentManager.OnBackStackChangedListener {

    private ClinicManager builder;
    private boolean showEdit;
    private Clinic currentClinic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        PreferenceManager preferenceManager = PreferenceManager.getManager(this);
        Credential credential = Credential.getCredential(preferenceManager);
        builder = new ClinicManager(credential);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get("clinic") != null) {
            Clinic clinic = (Clinic) extras.get("clinic");
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, ViewClinicListFragment.getFragment(builder), "list")
                    .addToBackStack("list")
                    .commit();
            clinicSelected(clinic);
            showEdit = true;
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, ViewClinicListFragment.getFragment(builder), "list")
                    .addToBackStack("list")
                    .commit();
            showEdit = false;
        }
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
            showEdit = !showEdit;
        } else {
            super.onBackPressed();
        }
    }
    /**
     * handle edit and delete options menu
     * @param item menu item clicked
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (showEdit) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, ModifyClinicFragment.getFragment(currentClinic, builder), "edit")
                            .addToBackStack("edit")
                            .commit();
                    showEdit = false;
                    return true;
                case R.id.action_delete:
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);

                    alert.setTitle("Remove Clinic");
                    alert.setMessage("Are you sure you want to remove the clinic");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            deleteCurrentClinic();
                            onBackPressed();
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * delete the current selected clinic
     */
    private void deleteCurrentClinic() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                builder.deleteClinic(currentClinic);
                return null;
            }
        }.execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (showEdit) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list_modify, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackStackChanged() {
        invalidateOptionsMenu();
        //force refresh
        Fragment frag = getFragmentManager().findFragmentByTag(getFragmentManager()
                .getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1)
                .getName());
        if (frag != null) frag.onResume();
    }

    /**
     * show details of the clinic
     * @param clinic
     * @see com.ce2006.project.activity.clinic.ViewClinicFragment
     */
    @Override
    public void clinicSelected(Clinic clinic) {
        currentClinic = clinic;
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, ViewClinicFragment.getFragment(clinic, builder))
                .addToBackStack("detail")
                .commit();
        showEdit = true;
    }
    @Override
    public void clinicModified(Clinic clinic) {
        onBackPressed();
    }
}