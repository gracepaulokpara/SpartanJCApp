package app.my.SpartanJCApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.my.SpartanJCApp.R;
import app.my.SpartanJCApp.database.SQLiteHelper;
import app.my.SpartanJCApp.model.DataModel;

public class ContactDataAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DataModel> dataModels;

    public ContactDataAdapter(Context context, ArrayList<DataModel> dataModels) {
        this.context = context;
        this.dataModels = dataModels;
    }

    @Override
    public int getCount() {
        return dataModels.size();
    }

    @Override
    public Object getItem(int i) {
        return dataModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DataModel dataModel = dataModels.get(i);
        View root = LayoutInflater.from(context).inflate(R.layout.row_contact_data, viewGroup, false);
        ImageView icon = (ImageView) root.findViewById(R.id.imageView_icon);
        TextView type = (TextView) root.findViewById(R.id.textView_data_type);
        TextView value = (TextView) root.findViewById(R.id.textView_data_value);


        // set the data type and data value
        type.setText(dataModel.getDataType());
        value.setText(dataModel.getDataValue());

        // get the data category and the icon according to it
        switch (dataModel.getDataCategory()) {
            case SQLiteHelper.DATA_CATEGORY_PHONE:
                icon.setImageResource(R.drawable.ic_phone);
                break;
            case SQLiteHelper.DATA_CATEGORY_EMAIL:
                icon.setImageResource(R.drawable.ic_email);
                break;
            case SQLiteHelper.DATA_CATEGORY_SOCIAL:
                icon.setImageResource(R.drawable.ic_social);
                break;
            case SQLiteHelper.DATA_CATEGORY_NOTE:
                icon.setImageResource(R.drawable.ic_note);
                break;
        }

        return root;
    }
}
