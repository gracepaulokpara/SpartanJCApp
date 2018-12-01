package app.my.SpartanJCApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.my.SpartanJCApp.R;
import app.my.SpartanJCApp.model.ContactModel;

public class ContactListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ContactModel> contactModels;

    public ContactListAdapter(Context context, ArrayList<ContactModel> contactModels) {
        this.context = context;
        this.contactModels = contactModels;
    }

    @Override
    public int getCount() {
        return contactModels.size();
    }

    @Override
    public Object getItem(int i) {
        return contactModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ContactModel contactModel = contactModels.get(i);
        View root = LayoutInflater.from(context).inflate(R.layout.row_contacts, viewGroup, false);
        TextView name = (TextView) root.findViewById(R.id.textView_contact_name);
        TextView phone = (TextView) root.findViewById(R.id.textView_contact_number);

        // set the contact name and the first phone number in the view
        name.setText(contactModel.getFirstName() + " " + contactModel.getLastName());
        if (contactModel.getPhones().size() > 0)
            phone.setText(contactModel.getPhones().get(0).getDataValue());

        return root;
    }
}
