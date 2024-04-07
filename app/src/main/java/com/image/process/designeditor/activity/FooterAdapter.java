package com.image.process.designeditor.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.card.MaterialCardView;
import com.image.process.R;
import com.image.process.model.AllDetailsOfClientItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FooterAdapter extends RecyclerView.Adapter<FooterAdapter.ViewHolder> {
    private Context context;
    private List<AllDetailsOfClientItem> filteredList; // Assume AllDetailsOfClientItem is your data model
    private int footerSelectedPosition = -1;
    private List<AllDetailsOfClientItem> originalList;

    public FooterAdapter(Context context, List<AllDetailsOfClientItem> filteredList) {
        this.context = context;
        this.filteredList = new ArrayList<>(filteredList);;
        this.originalList = filteredList;
    }

    @NonNull
    @Override
    public FooterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
        return new ViewHolder(view);
    }
    

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        if (filteredList.get(position) != null) {
            Glide.with(context)
                    .load(filteredList.get(position).getHeader())
                    // .transition(GenericTransitionOptions.with(animationObject))
                    .placeholder(R.color.white)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                    .into(viewHolder.footer);
        }else {
            Glide.with(context)
                    .load(R.color.white)
                    // .transition(GenericTransitionOptions.with(animationObject))
                    .placeholder(R.color.white)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                    .into(viewHolder.footer);
        }

        if (footerSelectedPosition == position){
            viewHolder.footerBack.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }else {
            viewHolder.footerBack.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (filteredList.get(position) != null) {
                    footerSelectedPosition  = position ;
                    Glide.with(context)
                            .load(filteredList.get(position).getHeader())
                            // .transition(GenericTransitionOptions.with(animationObject))
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                            .into(holder.footer);
                } else {
                    Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView footer;
        MaterialCardView footerBack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            footer = itemView.findViewById(R.id.footer);
            footerBack = itemView.findViewById(R.id.background);
        }
    }

    public void filter(String query) {
        filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (AllDetailsOfClientItem item : originalList) {
                if (item.getClientName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
