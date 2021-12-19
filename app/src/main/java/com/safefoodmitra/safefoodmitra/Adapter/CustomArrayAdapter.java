package com.safefoodmitra.safefoodmitra.Adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.EqupmentModals;
import com.safefoodmitra.safefoodmitra.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomArrayAdapter extends ArrayAdapter<EqupmentModals> {

    private ArrayList<EqupmentModals> arrayList;
    private Context context;
    private boolean isFromView = false;
    private String headerText;
    int listPos;
   public static Map<String, String> arraylistequpment = new HashMap<String, String>();
    public CustomArrayAdapter(Context context, String headerText, int resourceId,
                              ArrayList<EqupmentModals> arrayList) {
        super(context, resourceId,arrayList);
        this.arrayList = arrayList;
        this.context = context;
        this.headerText = headerText;
    }
    public int getCount() {
        return arrayList.size();
    }

    public EqupmentModals getItem(int position) {
        if( position < 1 ) {
            return null;
        }
        else {
            return arrayList.get(position-1);
        }
       // return arrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        View row=inflater.inflate(R.layout.customcheckitem, parent, false);
        TextView textView1 = (TextView)row.findViewById(R.id.equipmentname);
        CheckBox checkBox = (CheckBox)row.findViewById(R.id.checkname);
        if( position < 1 ) {
            checkBox.setVisibility(View.GONE);
            textView1.setText(headerText);
        }
        else {
            listPos = position-1;
            Gson gson = new Gson();
            textView1.setText(arrayList.get(listPos).getEquip_name());
            isFromView = true;
            checkBox.setChecked(arrayList.get(listPos).isSelected());
            isFromView = false;
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setTag(listPos);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                   /* int getPosition = (Integer) buttonView.getTag();

                    if (!isFromView) {
                        arrayList.get(position).setSelected(isChecked);

                    }*/
                    if (!isFromView){
                        arrayList.get(position-1).setSelected(isChecked);
                    }
                    if (arrayList.get(position-1).isSelected()){
                        Toast.makeText(context,arrayList.get(position-1).getId(),Toast.LENGTH_LONG).show();

                            arraylistequpment.put(String.valueOf(position-1), arrayList.get(position-1).getId());
                            String json = gson.toJson(arraylistequpment);
                            Log.d("array",json);

                    }else {
                        arraylistequpment.remove(String.valueOf(position-1));
                        String json = gson.toJson(arraylistequpment);
                        Log.d("array",json);

                    }
                }
            });
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.toggle();
                    Toast.makeText(context,arrayList.get(position-1).getId(),Toast.LENGTH_LONG).show();
                }
            });

        }



        return row;
    }

}