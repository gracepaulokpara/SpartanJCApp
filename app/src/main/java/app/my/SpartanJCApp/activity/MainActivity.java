package app.my.SpartanJCApp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.my.SpartanJCApp.R;
import app.my.SpartanJCApp.adapter.ContactListAdapter;
import app.my.SpartanJCApp.database.DatabaseManager;
import app.my.SpartanJCApp.model.ContactModel;

public class MainActivity extends AppCompatActivity {

    Button mAddButton;
    ListView mContactsListView;
    TextView textView;
    ArrayList<ContactModel> contactModels;
    ContactListAdapter contactListAdapter;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddButton = (Button) findViewById(R.id.button_add);
        mContactsListView = (ListView) findViewById(R.id.listview_contacts);
        textView = (TextView) findViewById(R.id.textView);

        // initializing the database manager and open it
        databaseManager = new DatabaseManager(this);
        databaseManager.open();
        contactModels = new ArrayList<>();
        contactListAdapter = new ContactListAdapter(this, contactModels);
        mContactsListView.setAdapter(contactListAdapter);

        mContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // pass the contact model object to ViewContactActivity

                ContactModel contactModel = contactModels.get(i);
                Intent intent = new Intent(MainActivity.this, ViewContactActivity.class);
                intent.putExtra("contact", contactModel);
                startActivity(intent);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateContactActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        contactModels.clear();
        contactModels.addAll(databaseManager.retrieveContacts()); // get all the contacts
        contactListAdapter.notifyDataSetChanged();
        if (contactModels.size() > 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseManager.close();
    }
}
