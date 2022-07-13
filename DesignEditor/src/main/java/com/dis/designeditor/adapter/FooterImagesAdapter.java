package com.dis.designeditor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.dis.designeditor.R;
import com.dis.designeditor.activity.DesignStudio;
import com.dis.designeditor.databinding.ClientProfileLayoutBinding;
import com.dis.designeditor.model.DesignStudioBottomModel;

import java.util.ArrayList;

import static com.dis.designeditor.activity.DesignStudio.onStickerSelected;


public class FooterImagesAdapter extends RecyclerView.Adapter<FooterImagesAdapter.ViewHolder> {
    Context context;
    String from;
    ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> materialList;
    int client_id;
    ImageView footer;

    public FooterImagesAdapter(Context context, String from, ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> materialList, int client_id, ImageView footer) {
        this.context = context;
        this.from = from;
        this.materialList = materialList;
        this.client_id = client_id;
        this.footer = footer;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ClientProfileLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d("footer_image","called----"+materialList.get(position).getImageURL());
        if(!materialList.get(position).getImageURL().equals("")){
            Glide.with(context)
                    .load(materialList.get(position).getImageURL())
                    // .transition(GenericTransitionOptions.with(animationObject))
                    .placeholder(R.color.white)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0))                    )
                    .into(holder.binding.pic);
            holder.binding.imageurl.setText(materialList.get(position).getImageURL());
            holder.binding.id.setText(materialList.get(position).getID()+"");
        }

        Log.d("footer_called","fav--"+materialList.get(position).getFavourite());
        if(materialList.get(position).getFavourite()==1){
            holder.binding.favimg.setImageDrawable(context.getResources().getDrawable(R.drawable.fav));
        }
        else{
            holder.binding.favimg.setImageDrawable(context.getResources().getDrawable(R.drawable.unfav));
        }



        holder.binding.pic.setOnClickListener(view -> {
            onStickerSelected("material","");
            DesignStudio.binding.warning.setVisibility(View.VISIBLE);
            DesignStudio.binding.btnApply.setVisibility(View.VISIBLE);
            DesignStudio.position = 1;
            Glide.with(context)
                    .load(materialList.get(position).getImageURL())
                    // .transition(GenericTransitionOptions.with(animationObject))
                    .placeholder(R.color.white)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0))                    )
                    .into(footer);

            Animation bottomUp = AnimationUtils.loadAnimation(context,
                    R.anim.bottom_down);

            DesignStudio.binding.text.setClickable(false);


        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }
    public void addItems(ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> postItems) {
        Log.d("TodayDesignAdapter","addItems--"+postItems.size());
        materialList.addAll(postItems);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ClientProfileLayoutBinding binding;
        public ViewHolder(@NonNull ClientProfileLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
