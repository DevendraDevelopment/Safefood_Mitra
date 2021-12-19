package com.safefoodmitra.safefoodmitra.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.safefoodmitra.safefoodmitra.Activities.PdfActivity;
import com.safefoodmitra.safefoodmitra.Activities.SubfoodsafetystandardActivity;
import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.DocumentspdfModel;
import com.safefoodmitra.safefoodmitra.Modals.SubFoodDocspdfModel;
import com.safefoodmitra.safefoodmitra.Modals.UnitModals;
import com.safefoodmitra.safefoodmitra.R;
import com.safefoodmitra.safefoodmitra.databinding.DocumentsitempdfBinding;
import com.safefoodmitra.safefoodmitra.databinding.SubfooddocitempdfBinding;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;


public class SubFoodDocpdfAdapter extends RecyclerView.Adapter<SubFoodDocpdfAdapter.Myview> implements Filterable {
    LayoutInflater layoutInflater;
    Context context;
    List<SubFoodDocspdfModel> subFoodDocspdfModels;
    List<SubFoodDocspdfModel> subFoodDocspdfModels2;
    String pdfFileName,baseurl,toturl;
    public SubFoodDocpdfAdapter(Context context, List<SubFoodDocspdfModel> subFoodDocspdfModels) {
        this.context = context;
        this.subFoodDocspdfModels = subFoodDocspdfModels;
        this.subFoodDocspdfModels2 = new ArrayList<>(subFoodDocspdfModels);
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SubfooddocitempdfBinding subfooddocitempdfBinding = DataBindingUtil.inflate(layoutInflater, R.layout.subfooddocitempdf,parent,false);
        return new Myview(subfooddocitempdfBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myview holder, final int position) {
        SubFoodDocspdfModel subFoodDocspdfModel = subFoodDocspdfModels.get(position);
        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#fafafa"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        holder.subfooddocitempdfBinding.pdftitles.setText(subFoodDocspdfModels.get(position).getDoc_title());
        if (subFoodDocspdfModel.getDoctype().equals("1")){
            holder.subfooddocitempdfBinding.imgyoutube.setVisibility(View.GONE);
            holder.subfooddocitempdfBinding.imglink.setVisibility(View.GONE);
            holder.subfooddocitempdfBinding.imgpdf.setVisibility(View.VISIBLE);
            holder.subfooddocitempdfBinding.pdftitles.setText(subFoodDocspdfModels.get(position).getDoc_title());
            //  holder.subfooddocitempdfBinding.tvpdfslno.setText(String.valueOf(position+1));
            baseurl=subFoodDocspdfModel.getBaseurl();
            pdfFileName=subFoodDocspdfModel.getDoc_path();
            toturl=baseurl+"/"+pdfFileName;
            if (toturl!=null){
                holder.subfooddocitempdfBinding.linearpdffile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(toturl)));
                        //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(toturl)));
                        context.startActivity(new Intent(context, PdfActivity.class).putExtra("detail", Utlity.gson.toJson(subFoodDocspdfModel)));
                    }
                });
            }

        }

        else if (subFoodDocspdfModel.getDoctype().equals("2")){
            holder.subfooddocitempdfBinding.imgpdf.setVisibility(View.GONE);
            holder.subfooddocitempdfBinding.imglink.setVisibility(View.GONE);
            holder.subfooddocitempdfBinding.imgyoutube.setVisibility(View.VISIBLE);
            // holder.subfooddocitempdfBinding.tvyoutubeslno.setText(String.valueOf(position+1));
            //  holder.subfooddocitempdfBinding.pdftitles.setText(subFoodDocspdfModels.get(position).getDoc_title());
            if (subFoodDocspdfModel.getDoc_title()!=null){
                holder.subfooddocitempdfBinding.linearpdffile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof SubfoodsafetystandardActivity) {
                            ((SubfoodsafetystandardActivity)context).youtubeData(subFoodDocspdfModel.getDoc_path());
                        }
                    }
                });
            }

        } else if (subFoodDocspdfModel.getDoctype().equals("3")){
            holder.subfooddocitempdfBinding.imgpdf.setVisibility(View.GONE);
            holder.subfooddocitempdfBinding.imgyoutube.setVisibility(View.GONE);
            holder.subfooddocitempdfBinding.imglink.setVisibility(View.VISIBLE);
            // holder.subfooddocitempdfBinding.tvlinkslno.setText(String.valueOf(position+1));
            // holder.subfooddocitempdfBinding.pdftitles.setText(subFoodDocspdfModels.get(position).getDoc_title());
            if (subFoodDocspdfModel.getDoc_title()!=null){
                holder.subfooddocitempdfBinding.linearpdffile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    /*Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(subFoodDocspdfModel.getDoc_path()));
                    context.startActivity(intent);*/
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                        try {
                            intent.setData(Uri.parse(subFoodDocspdfModel.getDoc_path()));
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException exception) {

                        }
                    }
                });
            }

        }



    }

    @Override
    public int getItemCount() {
        return subFoodDocspdfModels.size();
    }

    public Filter getFilter() {
        return selectfilter;
    }
    private Filter selectfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SubFoodDocspdfModel> filterdlist = new ArrayList<>();

            if (constraint==null || constraint.length() ==0){
                filterdlist.addAll(subFoodDocspdfModels2);

            }
            else {
                for (SubFoodDocspdfModel subFoodDocspdfModels:subFoodDocspdfModels2){
                    if (subFoodDocspdfModels.getDoctype().equalsIgnoreCase("1")){
                        String textfilter= subFoodDocspdfModels.getDoc_title().toLowerCase();
                        //String idfilter= subFoodDocspdfModels.getId().toLowerCase();
                        if(textfilter.contains(constraint.toString().toLowerCase())){
                            filterdlist.add(subFoodDocspdfModels);
                        }
                    }

                    else if (subFoodDocspdfModels.getDoctype().equalsIgnoreCase("2")){
                        String textfilter= subFoodDocspdfModels.getDoc_title().toLowerCase();
                        //String idfilter= subFoodDocspdfModels.getId().toLowerCase();
                        if(textfilter.contains(constraint.toString().toLowerCase())){
                            filterdlist.add(subFoodDocspdfModels);
                        }
                    }

                    else if (subFoodDocspdfModels.getDoctype().equalsIgnoreCase("3")){
                        String textfilter= subFoodDocspdfModels.getDoc_title().toLowerCase();
                        //String idfilter= subFoodDocspdfModels.getId().toLowerCase();
                        if(textfilter.contains(constraint.toString().toLowerCase())){
                            filterdlist.add(subFoodDocspdfModels);
                        }
                    }

                   /*  else if (idfilter.contains(constraint.toString().toLowerCase())){
                        filterdlist.add(subFoodDocspdfModels);
                    }
                   if (zoneModals.getZone_name().equals(filterpattern)||zoneModals.getId().equals(filterpattern)){
                        filterdlist.add(zoneModals);
                    }

                   */


                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdlist;
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            subFoodDocspdfModels.clear();
            subFoodDocspdfModels.addAll((ArrayList) results.values);
            notifyDataSetChanged();


        }
    };

    public class Myview extends RecyclerView.ViewHolder {
        SubfooddocitempdfBinding subfooddocitempdfBinding;
        public Myview(SubfooddocitempdfBinding subfooddocitempdfBinding) {
            super(subfooddocitempdfBinding.getRoot());
            this.subfooddocitempdfBinding=subfooddocitempdfBinding;
        }
    }
}