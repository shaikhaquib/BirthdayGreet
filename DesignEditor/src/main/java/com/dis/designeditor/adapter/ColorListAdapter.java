package com.dis.designeditor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dis.designeditor.R;

import com.dis.designeditor.databinding.ColorLayoutBinding;

import java.util.ArrayList;

import static com.dis.designeditor.fragment.FontDialogFragment.changeColor;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ViewHolder> {
    Context context;
    ArrayList<String> nameList;
    ArrayList<String> colorList;
    private int selected_position = -1;

    public ColorListAdapter(Context context, ArrayList<String> nameList,ArrayList<String> colorList) {
        this.context = context;
        this.nameList = nameList;
        this.colorList = colorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ColorLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.binding.fontname.setText(nameList.get(position));
       holder.binding.fontcolorname.setText(colorList.get(position));
       holder.binding.fontcolor.setBackgroundColor(Color.parseColor(colorList.get(position)));

        if (selected_position == position) {
            // do your stuff here like

            holder.binding.fontbackground.setBackground(context.getResources().getDrawable(R.drawable.font_color_selector));

        } else {
            // do your stuff here like

            holder.binding.fontbackground.setBackground(null);
        }

        holder.binding.mainrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_position==position){
                    selected_position=-1;
                    changeColor("");
                    notifyDataSetChanged();
                    return;
                }
                else{
                    selected_position = position;
                    changeColor(holder.binding.fontcolorname.getText().toString());
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ColorLayoutBinding binding;
        public ViewHolder(@NonNull ColorLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
