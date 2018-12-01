package app.my.SpartanJCApp.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import app.my.SpartanJCApp.R;
import app.my.SpartanJCApp.database.DatabaseManager;
import app.my.SpartanJCApp.database.SQLiteHelper;
import app.my.SpartanJCApp.model.ContactModel;
import app.my.SpartanJCApp.model.DataModel;

public class UpdateContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar mActionBar;
    private Toolbar mToolbar;
    private ContactModel mContactModel;

    private EditText mFirstNameEditText, mLastNameEditText, mAddressEditText;
    private Button mDoneButton, mPlusPhoneButton, mPlusEmailButton, mPlusSocialButton, mPlusNoteButton;
    private LinearLayout mPhonesLinearLayout, mEmailsLinearLayout, mSocialsLinearLayout, mNotesLinearLayout;
    private RelativeLayout mPrimaryPhonesRelativeLayout, mPrimaryEmailsRelativeLayout, mPrimarySocialsRelativeLayout, mPrimaryNotesRelativeLayout;

    private DatabaseManager mDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
        }

        mDatabaseManager = new DatabaseManager(this);

        mFirstNameEditText = (EditText) findViewById(R.id.editText_firstName);
        mLastNameEditText = (EditText) findViewById(R.id.editText_lastName);
        mAddressEditText = (EditText) findViewById(R.id.editText_address);
        mDoneButton = (Button) findViewById(R.id.button_done);
        mPlusPhoneButton = (Button) findViewById(R.id.button_plus_phone);
        mPlusEmailButton = (Button) findViewById(R.id.button_plus_email);
        mPlusSocialButton = (Button) findViewById(R.id.button_plus_social);
        mPlusNoteButton = (Button) findViewById(R.id.button_plus_note);

        mDoneButton.setOnClickListener(this);
        mPlusPhoneButton.setOnClickListener(this);
        mPlusEmailButton.setOnClickListener(this);
        mPlusSocialButton.setOnClickListener(this);
        mPlusNoteButton.setOnClickListener(this);

        /*For each type of contact data, a linear layout with vertical orientation is used in which relative
         * layouts are added one by one. These relative layouts contain the edit texts/fields*/

        // linear layouts contains the layouts which has contact data fields

        mPhonesLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_phones);
        mEmailsLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_emails);
        mSocialsLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_socials);
        mNotesLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_notes);

        // setting the category as tag so that later we can retrieve the data category from the layout
        mPhonesLinearLayout.setTag(SQLiteHelper.DATA_CATEGORY_PHONE);
        mEmailsLinearLayout.setTag(SQLiteHelper.DATA_CATEGORY_EMAIL);
        mSocialsLinearLayout.setTag(SQLiteHelper.DATA_CATEGORY_SOCIAL);
        mNotesLinearLayout.setTag(SQLiteHelper.DATA_CATEGORY_NOTE);

        // the default layout which contains the contact data fields
        mPrimaryPhonesRelativeLayout = (RelativeLayout) mPhonesLinearLayout.getChildAt(0);
        mPrimaryEmailsRelativeLayout = (RelativeLayout) mEmailsLinearLayout.getChildAt(0);
        mPrimarySocialsRelativeLayout = (RelativeLayout) mSocialsLinearLayout.getChildAt(0);
        mPrimaryNotesRelativeLayout = (RelativeLayout) mNotesLinearLayout.getChildAt(0);

        // hiding the cancel button in the default contact data layouts
        mPrimaryPhonesRelativeLayout.findViewById(R.id.imageButton_cancel).setVisibility(View.GONE);
        mPrimaryEmailsRelativeLayout.findViewById(R.id.imageButton_cancel).setVisibility(View.GONE);
        mPrimarySocialsRelativeLayout.findViewById(R.id.imageButton_cancel).setVisibility(View.GONE);
        mPrimaryNotesRelativeLayout.findViewById(R.id.imageButton_cancel).setVisibility(View.GONE);

        // setting the input type of edittext in relative layout of phone and email contact data
        ((EditText) mPrimaryPhonesRelativeLayout.findViewById(R.id.editText_value)).setInputType(InputType.TYPE_CLASS_PHONE);
        ((EditText) mPrimaryEmailsRelativeLayout.findViewById(R.id.editText_value)).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        mContactModel = (ContactModel) getIntent().getSerializableExtra("contact");
        setContactData(mContactModel);
        mDatabaseManager.open();
    }

    /**
     * Sets the contact data to this Activity's UI by populating the fields with the contact data
     */
    private void setContactData(ContactModel contactModel) {
        mFirstNameEditText.setText(contactModel.getFirstName());
        mLastNameEditText.setText(contactModel.getLastName());
        mAddressEditText.setText(contactModel.getAddress());
        setDataField(mPhonesLinearLayout, InputType.TYPE_CLASS_PHONE, contactModel.getPhones());
        setDataField(mEmailsLinearLayout, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, contactModel.getEmails());
        setDataField(mSocialsLinearLayout, InputType.TYPE_CLASS_TEXT, contactModel.getSocials());
        setDataField(mNotesLinearLayout, InputType.TYPE_CLASS_TEXT, contactModel.getNotes());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.button_done:
                if (mFirstNameEditText.getText().toString().trim().isEmpty() || mLastNameEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                mContactModel.setFirstName(mFirstNameEditText.getText().toString());
                mContactModel.setLastName(mLastNameEditText.getText().toString());
                mContactModel.setAddress(mAddressEditText.getText().toString());
                // retrieving the contact data and setting them to the contact model object
                mContactModel.setPhones(retrieveDataField(mPhonesLinearLayout));
                mContactModel.setEmails(retrieveDataField(mEmailsLinearLayout));
                mContactModel.setSocials(retrieveDataField(mSocialsLinearLayout));
                mContactModel.setNotes(retrieveDataField(mNotesLinearLayout));
                // updating the contact in the database
                mDatabaseManager.updateContact(mContactModel);

                Intent result = new Intent();
                result.putExtra("contact", mContactModel);
                setResult(RESULT_OK, result);
                finish();


                break;

            // for clicking the + button of each contact data category, create a new field for
            // data type and the value by calling
            // createDataField()

            case R.id.button_plus_phone:
                createDataField(mPhonesLinearLayout, InputType.TYPE_CLASS_PHONE, null);
                break;
            case R.id.button_plus_email:
                createDataField(mEmailsLinearLayout, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, null);
                break;
            case R.id.button_plus_social:
                createDataField(mSocialsLinearLayout, InputType.TYPE_CLASS_TEXT, null);
                break;
            case R.id.button_plus_note:
                createDataField(mNotesLinearLayout, InputType.TYPE_CLASS_TEXT, null);
                break;
            case R.id.imageButton_cancel:
                // remove the relative layout containing the data fields
                ((LinearLayout) view.getParent().getParent()).removeView((RelativeLayout) view.getParent());
                break;
        }
    }


    /**
     * Validates the data fields contained inside the relative layouts of the passed linear layout
     *
     * @param dataLayout the layout containing the {@link RelativeLayout} which contains the
     *                   data fields
     */

    private boolean validateDataFields(LinearLayout dataLayout) {
        for (int i = 0; i < dataLayout.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) dataLayout.getChildAt(i);
            EditText type = (EditText) relativeLayout.findViewById(R.id.editText_type);
            EditText value = (EditText) relativeLayout.findViewById(R.id.editText_value);
            if (type.getText().toString().trim().isEmpty() || value.getText().toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates the data fields for the linear layout with a relative layout containing the
     * data fields.
     *
     * @param dataLayout linear layout where the data fields will be created
     * @param inputType  the input type of the data value field
     */

    private void createDataField(LinearLayout dataLayout, int inputType, DataModel dataModel) {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.row_field, dataLayout, false);
        layout.findViewById(R.id.imageButton_cancel).setOnClickListener(this);
        dataLayout.addView(layout);
        EditText type = (EditText) layout.findViewById(R.id.editText_type);
        EditText value = (EditText) layout.findViewById(R.id.editText_value);
        type.requestFocus();
        value.setInputType(inputType);
    }

    /**
     * Retrieves the contact data from the fields of a linear layout
     *
     * @param dataLayout layout which contains the relative layout containing contact data
     */
    private ArrayList<DataModel> retrieveDataField(LinearLayout dataLayout) {
        ArrayList<DataModel> dataModels = new ArrayList<>();
        for (int i = 0; i < dataLayout.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) dataLayout.getChildAt(i);
            EditText type = (EditText) relativeLayout.findViewById(R.id.editText_type);
            EditText value = (EditText) relativeLayout.findViewById(R.id.editText_value);
            DataModel dataModel = new DataModel();
            String dataCategory = (String) dataLayout.getTag();
            String dataType = type.getText().toString().trim();
            String dataValue = value.getText().toString().trim();
            if (!dataType.isEmpty() && !dataValue.isEmpty()) {
                dataModel.setDataCategory(dataCategory);
                dataModel.setDataType(dataType);
                dataModel.setDataValue(dataValue);
                dataModels.add(dataModel);
            }
        }
        return dataModels;
    }


    /**
     * Get the contact data and create new fields in relative layout and adding it to the linear layout
     * and setting those contact data to the fields
     */
    private void setDataField(LinearLayout dataLayout, int inputType, ArrayList<DataModel> dataModels) {
        for (int i = 0; i < dataModels.size(); i++) {
            DataModel dataModel = dataModels.get(i);

            RelativeLayout relativeLayout;
            if (i == 0) { // if index is zero, we don't want to create new relative layout because it exists
                relativeLayout = (RelativeLayout) dataLayout.getChildAt(0);
            } else {
                // create relative layouts and set them to linear layout
                relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.row_field, dataLayout, false);
                ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.imageButton_cancel);
                imageView.setOnClickListener(this);
                dataLayout.addView(relativeLayout);
            }

            // set the contact data to the fields

            EditText type = (EditText) relativeLayout.findViewById(R.id.editText_type);
            EditText value = (EditText) relativeLayout.findViewById(R.id.editText_value);

            type.setText(dataModel.getDataType());
            value.setText(dataModel.getDataValue());
            value.setInputType(inputType);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseManager.close(); // close the database when this activity is destroyed
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
