package com.safefoodmitra.safefoodmitra.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.safefoodmitra.safefoodmitra.Helper.Utlity;
import com.safefoodmitra.safefoodmitra.Modals.MySliderList;
import com.safefoodmitra.safefoodmitra.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

/*public class MySliderAdapter extends RecyclerView.Adapter<MySliderAdapter.ViewHolder> {
    private List<MySliderList> mySliderLists;
    private LayoutInflater mInflater;
    //private ViewPager2 viewPager;
    Context context;

    public MySliderAdapter(Context context, List<MySliderList> mySliderLists) {
        this.mySliderLists = mySliderLists;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater==null){
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(R.layout.slider_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MySliderList ob= mySliderLists.get(position);
        Utlity.Set_imageGlideSlide(context,mySliderLists.get(position).getImagename(),holder.imageViewBackground);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mySliderLists.get(position).getImageurl()!=null){

                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mySliderLists.get(position).getImageurl()));
                        context.startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No application can handle this request."
                                + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }

            }
        });
//
    }

    @Override
    public int getItemCount() {
        return mySliderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        ImageView imageViewBackground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;

        }
    }
}*/

public class MySliderAdapter extends SliderViewAdapter<MySliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<MySliderList> mSliderItems;
    private Context context;
    // Constructor
    public MySliderAdapter(Context context, List<MySliderList> mySliderListArrayList) {
        this.mSliderItems = mySliderListArrayList;
        this.context = context;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final MySliderList sliderItem = mSliderItems.get(position);
        Utlity.Set_imageGlideSlide(context,mSliderItems.get(position).getImagename(),viewHolder.imageViewBackground);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSliderItems.get(position).getImageurl()!=null){

                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSliderItems.get(position).getImageurl()));
                        context.startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No application can handle this request."
                                + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }
}
