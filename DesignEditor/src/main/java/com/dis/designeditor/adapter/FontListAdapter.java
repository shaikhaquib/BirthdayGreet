package com.dis.designeditor.adapter;

import static com.dis.designeditor.activity.DesignStudio.fontProvider;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dis.designeditor.databinding.FontListLayoutBinding;
import com.dis.designeditor.model.DesignStudioBottomModel;

import java.util.ArrayList;

public class FontListAdapter extends RecyclerView.Adapter<FontListAdapter.ViewHolder> {
    Context context;
    ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> fontList;

    private int selected_position = -1;


    public FontListAdapter(Context context, ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> fontList) {
        this.context = context;
        this.fontList = fontList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FontListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

      /* 
       if (selected_position == position) {
            // do your stuff here like

            holder.binding.fontimage.setBackground(context.getResources().getDrawable(R.drawable.font_color_selector));

        } else {
            // do your stuff here like

            holder.binding.fontimage.setBackground(null);
        }
        */
        holder.binding.fontname.setText(fontList.get(position).getName());
        holder.binding.fontname.setTypeface(fontProvider.getTypeface(fontList.get(position).getName()));;
       // holder.binding.fontid.setText(fontList.get(position).getID()+"");



    }

    public void addItems(ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> postItems) {
        Log.d("TodayDesignAdapter","addItems--"+postItems.size());
        fontList.addAll(postItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FontListLayoutBinding binding;
        public ViewHolder(@NonNull FontListLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
