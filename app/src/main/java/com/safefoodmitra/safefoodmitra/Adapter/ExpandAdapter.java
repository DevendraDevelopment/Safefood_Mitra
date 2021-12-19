package com.safefoodmitra.safefoodmitra.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safefoodmitra.safefoodmitra.Modals.InspectrespoModals;
import com.safefoodmitra.safefoodmitra.Modals.Root;
import com.safefoodmitra.safefoodmitra.R;

import java.util.ArrayList;
import java.util.List;

public class ExpandAdapter extends BaseExpandableListAdapter implements Filterable{

    private Context context;
    private ArrayList<Root> roots;
    private ArrayList<Root> roots1;
    //private HashMap<String, List<String>> expandableListDetail;
    TextView prod_name,paramcat,parameter,perm_limit,discription;
    LinearLayout linearLayout;
    public ExpandAdapter(Context context, ArrayList<Root> roots) {
        this.context = context;
        this.roots = roots;
        this.roots1 = new ArrayList<>(roots);
    }

    @Override
    public Object getChild(int groupPosition, int expandedListPosition) {
        List<Root.ValuesBean> productList =roots.get(groupPosition).getValues();
        return productList.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        Root.ValuesBean detailInfo = (Root.ValuesBean) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listtitle, null);
        }

        prod_name = (TextView) view.findViewById(R.id.prod_name);
        parameter = (TextView) view.findViewById(R.id.parameter);
        perm_limit = (TextView) view.findViewById(R.id.perm_limit);
        discription = (TextView) view.findViewById(R.id.discrpition);
        linearLayout = (LinearLayout) view.findViewById(R.id.lineardata);
       /* if (detailInfo.getProd_name()!=null){
            prod_name.setVisibility(View.VISIBLE);
            prod_name.setText(detailInfo.getProd_name());
        }*/
        if (detailInfo.getParameter()!=null && detailInfo.getPerm_limit()!=null){
            linearLayout.setVisibility(View.VISIBLE);
            parameter.setText(detailInfo.getParameter());
            perm_limit.setText(detailInfo.getPerm_limit());
        }
        else {
            linearLayout.setVisibility(View.GONE);
        }

        if (detailInfo.getPerm_desc()!=null){
            discription.setVisibility(View.VISIBLE);
            discription.setText(detailInfo.getPerm_desc());
        }
        else {
            discription.setVisibility(View.GONE);

        }

        return view;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        List<Root.ValuesBean> productList = roots.get(listPosition).getValues();
        return productList.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.roots.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.roots.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        Root headerInfo = (Root) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.listgroup, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.titles);
        heading.setText(headerInfo.getParcat_name());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void setCacheMenuRes(ArrayList<Root> inspectrespoModals)
    {
        this.roots = inspectrespoModals;
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return null;
    }
}
