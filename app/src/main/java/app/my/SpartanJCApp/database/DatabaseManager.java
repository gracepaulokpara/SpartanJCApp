package app.my.SpartanJCApp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import app.my.SpartanJCApp.model.ContactModel;
import app.my.SpartanJCApp.model.DataModel;

public class DatabaseManager {
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteHelper sqLiteHelper;
    private Context mContext;

    public DatabaseManager(Context context) {
        sqLiteHelper = new SQLiteHelper(context);
        this.mContext = context;
    }

    /**
     * Opens database to perform database related operations
     */
    public void open() {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
    }

    /**
     * Closes database.
     */
    public void close() {
        sqLiteDatabase.close();
    }

    /**
     * Creates a contact in the database
     *
     * @param contactModel contains all the info about the contact
     * @return true if the contact was created successfully, false if the contact exists
     */

    public boolean createContact(ContactModel contactModel) {

        // check if the contact exists
        String[] columns = new String[]{SQLiteHelper.COLUMN_ID};
        String selection = SQLiteHelper.COLUMN_CONTACT_FIRST_NAME + " = ? AND " + SQLiteHelper.COLUMN_CONTACT_LAST_NAME + " = ?";
        String[] selectionArgs = {contactModel.getFirstName(), contactModel.getLastName()};
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME_CONTACT, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return false;
            }
        }

        // contact doesn't exist, so continue

        sqLiteDatabase.beginTransaction();

        // set the values to ContentValue

        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COLUMN_CONTACT_FIRST_NAME, contactModel.getFirstName());
        contentValues.put(SQLiteHelper.COLUMN_CONTACT_LAST_NAME, contactModel.getLastName());
        contentValues.put(SQLiteHelper.COLUMN_CONTACT_ADDRESS, contactModel.getAddress());

        // contactId is the number of newly inserted row, we will use this to add contact data in the contact data table
        long contactId = sqLiteDatabase.insert(SQLiteHelper.TABLE_NAME_CONTACT, null, contentValues);

        if (contactId != -1) { // -1 means it failed to create the row
            // create the contact data for each type of data
            createContactData(String.valueOf(contactId), contactModel.getPhones());
            createContactData(String.valueOf(contactId), contactModel.getEmails());
            createContactData(String.valueOf(contactId), contactModel.getSocials());
            createContactData(String.valueOf(contactId), contactModel.getNotes());

            sqLiteDatabase.setTransactionSuccessful();
        } else {
            return false;
        }

        sqLiteDatabase.endTransaction();
        return true;
    }

    /**
     * Get all contacts from the database
     *
     * @return an {@link ArrayList} containing all contacts
     */
    public ArrayList<ContactModel> retrieveContacts() {

        // a String array containing the columns we want to get from the contact table in database
        String[] contactColumns = new String[]{SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_CONTACT_FIRST_NAME, SQLiteHelper.COLUMN_CONTACT_LAST_NAME, SQLiteHelper.COLUMN_CONTACT_ADDRESS};
        // getting a cursor by querying the database
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME_CONTACT, contactColumns, null, null, null, null, SQLiteHelper.COLUMN_CONTACT_FIRST_NAME);

        // this array contains the columns we want from the contact data table in database
        // this is initialized here to reduce the object creation in the loop
        String[] contactDataColumns = new String[]{SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_CONTACT_ID, SQLiteHelper.COLUMN_DATA_CATEGORY, SQLiteHelper.COLUMN_DATA_TYPE, SQLiteHelper.COLUMN_DATA_VALUE};
        // this is the selection string for the query method, this will be used to get contact spedicif data from the database
        String contactDataSelection = SQLiteHelper.COLUMN_CONTACT_ID + " = ?";

        // these will contain the info about the contact
        ArrayList<ContactModel> contactModels = new ArrayList<>();
        ArrayList<DataModel> phones, emails, socials, notes;
        phones = new ArrayList<>();
        emails = new ArrayList<>();
        socials = new ArrayList<>();
        notes = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ContactModel contactModel = new ContactModel();
            // clearing it every time so that previous contact data doesn't get inserted in the arraylist
            // this also, to reduce the amount of object creation
            phones.clear();
            emails.clear();
            socials.clear();
            notes.clear();

            // setting the contact info in the
            contactModel.setId(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)));
            contactModel.setFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_CONTACT_FIRST_NAME)));
            contactModel.setLastName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_CONTACT_LAST_NAME)));
            contactModel.setAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_CONTACT_ADDRESS)));

            // selection args to get the data from the table for specific contact id
            String[] selectionArgs = {cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID))};

            // querying for the contact data
            Cursor contactDataCursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME_CONTACT_DATA, contactDataColumns, contactDataSelection, selectionArgs, null, null, null);

            for (contactDataCursor.moveToFirst(); !contactDataCursor.isAfterLast(); contactDataCursor.moveToNext()) {

                // getting the data and then adding it the contact model based on its category

                String dataCategory = contactDataCursor.getString(contactDataCursor.getColumnIndex(SQLiteHelper.COLUMN_DATA_CATEGORY));
                String dataType = contactDataCursor.getString(contactDataCursor.getColumnIndex(SQLiteHelper.COLUMN_DATA_TYPE));
                String dataValue = contactDataCursor.getString(contactDataCursor.getColumnIndex(SQLiteHelper.COLUMN_DATA_VALUE));

                DataModel dataModel = new DataModel();
                dataModel.setDataCategory(dataCategory);
                dataModel.setDataType(dataType);
                dataModel.setDataValue(dataValue);

                if (dataCategory.equals(SQLiteHelper.DATA_CATEGORY_PHONE)) {
                    phones.add(dataModel);
                } else if (dataCategory.equals(SQLiteHelper.DATA_CATEGORY_EMAIL)) {
                    emails.add(dataModel);
                } else if (dataCategory.equals(SQLiteHelper.DATA_CATEGORY_SOCIAL)) {
                    socials.add(dataModel);
                } else if (dataCategory.equals(SQLiteHelper.DATA_CATEGORY_NOTE)) {
                    notes.add(dataModel);
                }
            }

            contactModel.setPhones(phones);
            contactModel.setEmails(emails);
            contactModel.setSocials(socials);
            contactModel.setNotes(notes);
            contactDataCursor.close();

            contactModels.add(contactModel);
        }
        cursor.close();
        return contactModels;
    }

    /**
     * Deletes the contact
     *
     * @param contactId contact id of the contact which needs to be deleted
     */
    public void deleteContact(String contactId) {
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME_CONTACT, SQLiteHelper.COLUMN_ID + " = ?",
                new String[]{contactId});

        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME_CONTACT_DATA, SQLiteHelper.COLUMN_CONTACT_ID + " = ?",
                new String[]{contactId});
    }

    /**
     * Update contact
     *
     * @param contactModel ContactModel object containing the updated data
     */
    public void updateContact(ContactModel contactModel) {
        sqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();

        // update the name and address
        contentValues.put(SQLiteHelper.COLUMN_CONTACT_FIRST_NAME, contactModel.getFirstName());
        contentValues.put(SQLiteHelper.COLUMN_CONTACT_LAST_NAME, contactModel.getLastName());
        contentValues.put(SQLiteHelper.COLUMN_CONTACT_ADDRESS, contactModel.getAddress());

        sqLiteDatabase.update(SQLiteHelper.TABLE_NAME_CONTACT, contentValues,
                SQLiteHelper.COLUMN_ID + " = ?", new String[]{contactModel.getId()});

        // delete the existing contact data
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME_CONTACT_DATA, SQLiteHelper.COLUMN_CONTACT_ID + " = ?",
                new String[]{contactModel.getId()});

        // then insert the updated contact data again
        createContactData(contactModel.getId(), contactModel.getPhones());
        createContactData(contactModel.getId(), contactModel.getEmails());
        createContactData(contactModel.getId(), contactModel.getSocials());
        createContactData(contactModel.getId(), contactModel.getNotes());

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    /**
     * Creates the contact data for a contact. Such as phones, emails, socials, notes
     *
     * @param contactId  contact id of the contact, basically the row number in the table
     * @param dataModels an {@link ArrayList} containing the data
     */
    private void createContactData(String contactId, ArrayList<DataModel> dataModels) {
        ContentValues contentValues = new ContentValues();
        for (DataModel dataModel : dataModels) {
            contentValues.clear();
            contentValues.put(SQLiteHelper.COLUMN_CONTACT_ID, contactId);
            contentValues.put(SQLiteHelper.COLUMN_DATA_CATEGORY, dataModel.getDataCategory());
            contentValues.put(SQLiteHelper.COLUMN_DATA_TYPE, dataModel.getDataType());
            contentValues.put(SQLiteHelper.COLUMN_DATA_VALUE, dataModel.getDataValue());
            sqLiteDatabase.insert(SQLiteHelper.TABLE_NAME_CONTACT_DATA, null, contentValues);
        }
    }
}
