package com.ce2006.project.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.example.user.ce2006_project.R;

/**
 * Help activity shows help contact to the developers
 */
public class HelpActivity extends ActionBarActivity implements View.OnClickListener {
    TextView txtEmergency, txtTechEmail, txtTechContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        txtEmergency = (TextView) findViewById(R.id.txtEmergency);
        txtTechEmail = (TextView) findViewById(R.id.txtTechEmail);
        txtTechContact = (TextView) findViewById(R.id.txtTechContact);
        txtEmergency.setOnClickListener(this);
        txtTechEmail.setOnClickListener(this);
        txtTechContact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == txtEmergency) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getString(R.string.EmergencyNum)));
            startActivity(intent);
        } else if (v == txtTechEmail) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.TechEmailAddress));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Report");
            startActivity(intent);
        } else if (v == txtTechContact) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getString(R.string.TechContactNum)));
            startActivity(intent);
        }
    }
}
