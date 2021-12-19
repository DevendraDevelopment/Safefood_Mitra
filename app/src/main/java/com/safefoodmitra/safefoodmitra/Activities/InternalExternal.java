package com.safefoodmitra.safefoodmitra.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.safefoodmitra.safefoodmitra.Adapter.DocumentsAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.ExternalAdapter;
import com.safefoodmitra.safefoodmitra.Adapter.InternalAdapter;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.ActivityInternalExternalBinding;

import java.util.ArrayList;

public class InternalExternal extends AppCompatActivity {
    ActivityInternalExternalBinding internalExternalBinding;
    String All;
    String Internal;
    String External;
    ArrayList<String> Internallist;
    ArrayList<String> Externallist;
    InternalAdapter internalAdapter;
    ExternalAdapter externalAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        internalExternalBinding = DataBindingUtil.setContentView(this,R.layout.activity_internal_external);
        Bundle bundle = getIntent().getExtras();
      /*  All = bundle.getString("all");
        Internal = bundle.getString("internal");
        External = bundle.getString("external");
        if (All!=null){
            if (All.equals("0")){
                internalExternalBinding.linearInternal.setVisibility(View.VISIBLE);
                internalExternalBinding.linearExternal.setVisibility(View.VISIBLE);
            }else if (Internal.equals("1")){
                internalExternalBinding.linearInternal.setVisibility(View.VISIBLE);
                internalExternalBinding.linearExternal.setVisibility(View.GONE);
            }else if (External.equals("3")){
                internalExternalBinding.linearInternal.setVisibility(View.GONE);
                internalExternalBinding.linearExternal.setVisibility(View.VISIBLE);

            }
        }else if (Internal!=null){
           if (Internal.equals("1")){
                internalExternalBinding.linearInternal.setVisibility(View.VISIBLE);
                internalExternalBinding.linearExternal.setVisibility(View.GONE);
            }
        }else if (External!=null){
          if (External.equals("2")){
                internalExternalBinding.linearInternal.setVisibility(View.GONE);
                internalExternalBinding.linearExternal.setVisibility(View.VISIBLE);

            }
        }*/

        Internallist = new ArrayList<>();
        Internallist.add("Department");
        Internallist.add("Department Name 1");
        Internallist.add("Department Name 2");
        Internallist.add("Department Name 3");
        Internallist.add("Department Name 4");
        Internallist.add("Department Name 5");
        ArrayAdapter<String> intadapter =
                new ArrayAdapter<String>(this,  R.layout.spinneritem, Internallist);
        intadapter.setDropDownViewResource( R.layout.spinneritem);
        internalExternalBinding.internallists.setAdapter(intadapter);
        internalExternalBinding.internallists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Externallist = new ArrayList<>();
        Externallist.add("Company");
        Externallist.add("Company Name 1");
        Externallist.add("Company Name 2");
        Externallist.add("Company Name 3");
        Externallist.add("Company Name 4");
        Externallist.add("Company Name 5");
        ArrayAdapter<String> extadapter =
                new ArrayAdapter<String>(this,  R.layout.spinneritem, Externallist);
        extadapter.setDropDownViewResource( R.layout.spinneritem);
        internalExternalBinding.externallists.setAdapter(extadapter);
        internalExternalBinding.externallists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (Utlity.is_online(this)){
            internalData();
            externalData();
        }
        else {
            Utlity.show_toast(InternalExternal.this,getResources().getString(R.string.nointernet));

        }
        click();
    }

    private void externalData() {
        internalExternalBinding.internalRecycler.setLayoutManager(new GridLayoutManager(InternalExternal.this,4));
        internalAdapter = new InternalAdapter(InternalExternal.this);
        internalExternalBinding.internalRecycler.setAdapter(internalAdapter);
    }
    private void internalData(){
        internalExternalBinding.externalrecycler.setLayoutManager(new GridLayoutManager(InternalExternal.this,4));
        externalAdapter = new ExternalAdapter(InternalExternal.this);
        internalExternalBinding.externalrecycler.setAdapter(externalAdapter);
    }
    private void click() {

    }
}