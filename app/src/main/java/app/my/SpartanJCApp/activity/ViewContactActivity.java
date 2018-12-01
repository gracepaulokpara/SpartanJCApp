package app.my.SpartanJCApp.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.my.SpartanJCApp.R;
import app.my.SpartanJCApp.adapter.ContactDataAdapter;
import app.my.SpartanJCApp.database.DatabaseManager;
import app.my.SpartanJCApp.model.ContactModel;
import app.my.SpartanJCApp.model.DataModel;

public class ViewContactActivity extends AppCompatActivity {

    private TextView mContactNameTextView, mContactAddressTextView, mTitleTextView;
    private ListView mContactDataListView;
    private ContactModel mContactModel;
    private ContactDataAdapter mContactDataAdapter;
    private ArrayList<DataModel> mDataModels;
    private ActionBar mActionBar;
    private Toolbar mToolbar;
    private Button mDeleteButton;
    private FloatingActionButton mUpdateFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
        }

        mTitleTextView = (TextView) findViewById(R.id.textView_title);
        mContactNameTextView = (TextView) findViewById(R.id.textView_contact_name);
        mContactAddressTextView = (TextView) findViewById(R.id.textView_contact_address);
        mContactDataListView = (ListView) findViewById(R.id.listView_contacts_data);
        mDeleteButton = (Button) findViewById(R.id.button_delete);
        mUpdateFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_update);
        mDataModels = new ArrayList<>();
        // get the contact model object from the intent extras
        mContactModel = (ContactModel) getIntent().getSerializableExtra("contact");
        mContactDataAdapter = new ContactDataAdapter(this, mDataModels);
        mContactDataListView.setAdapter(mContactDataAdapter);

        // set the contact data to the UI
        setContactData(mContactModel);

        // deletes this contact from the database
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager databaseManager = new DatabaseManager(ViewContactActivity.this);
                databaseManager.open();
                databaseManager.deleteContact(mContactModel.getId());
                databaseManager.close();
                finish();
            }
        });

        // opens an activity for result. that activity is for editing the contact
        mUpdateFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewContactActivity.this, UpdateContactActivity.class);
                intent.putExtra("contact", mContactModel);
                startActivityForResult(intent, 1001);
            }
        });
    }

    /**
     * Sets the contact data to this Activity's UI
     */
    private void setContactData(ContactModel mContactModel) {
        mContactNameTextView.setText(mContactModel.getFirstName() + " " + mContactModel.getLastName());
        mContactAddressTextView.setText(mContactModel.getAddress());
        if (mContactModel.getAddress().trim().isEmpty()) {
            findViewById(R.id.textView4).setVisibility(View.GONE);
            mContactAddressTextView.setVisibility(View.GONE);
        } else {
            findViewById(R.id.textView4).setVisibility(View.VISIBLE);
            mContactAddressTextView.setVisibility(View.VISIBLE);
        }
        mTitleTextView.setText(mContactModel.getFirstName() + " " + mContactModel.getLastName());
        mDataModels.clear();
        mDataModels.addAll(mContactModel.getPhones());
        mDataModels.addAll(mContactModel.getEmails());
        mDataModels.addAll(mContactModel.getSocials());
        mDataModels.addAll(mContactModel.getNotes());
        mContactDataAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 & resultCode == RESULT_OK) {
            ContactModel contactModel = (ContactModel) data.getSerializableExtra("contact");
            setContactData(contactModel); // update the contact data of this activity
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // finish this activity when the arrow button is selected
                return true;
            default:
                return true;
        }
    }
}
