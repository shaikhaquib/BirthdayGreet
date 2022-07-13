package com.dis.designeditor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.dis.designeditor.R;
import com.dis.designeditor.activity.DesignStudio;
import com.dis.designeditor.api.RetrofitClient;
import com.dis.designeditor.databinding.ClientProfileLayoutBinding;
import com.dis.designeditor.extras.SharedPrefManager;
import com.dis.designeditor.model.DesignStudioBottomModel;
import com.dis.designeditor.model.FavouriteModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dis.designeditor.activity.DesignStudio.onStickerSelected;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder> {
    Context context;
    String from;
    ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> materialList;
    int client_id;

    public QuotesAdapter(Context context, String from, ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> materialList,int client_id) {
        this.context = context;
        this.from = from;
        this.materialList = materialList;
        this.client_id = client_id;
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

        holder.binding.favimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id= Integer.parseInt(holder.binding.id.getText().toString());
                int pos=holder.getAdapterPosition();
                DesignStudioBottomModel.ClientWiseMaterialList model1=materialList.get(pos);
                int fav=0;
                if(model1.getFavourite()==1){
                    fav=0;
                }
                else{
                    fav=1;
                }

                Call<FavouriteModel> model= RetrofitClient
                        .getInstance().getApi().favouriteQuotes(RetrofitClient.AppName,"Quotes", fav,id,client_id, SharedPrefManager.getInstance(context).userName());
                model.enqueue(new Callback<FavouriteModel>() {
                    @Override
                    public void onResponse(Call<FavouriteModel> call, Response<FavouriteModel> response) {
                        Toast.makeText(context, response.body().getErrormsg(), Toast.LENGTH_SHORT).show();
                        if(!response.body().getError()){

                            if(response.body().getErrormsg().equals("Added as favourite.")){
                                Glide.with(context).load(R.drawable.fav).into(holder.binding.favimg);
                                final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.fav_animation);
                                holder.binding.favimg.startAnimation(myAnim);
                                myAnim.setAnimationListener(new Animation.AnimationListener() {
                                    public void onAnimationStart(Animation anim) {
                                    }

                                    public void onAnimationRepeat(Animation anim) {
                                    }

                                    public void onAnimationEnd(Animation anim) {
                                        model1.setFavourite(1);
                                        notifyDataSetChanged();
                                    }
                                });


                            }
                            else if(response.body().getErrormsg().equals("Removed from favourite list.")){
                                Glide.with(context).load(R.drawable.unfav).into(holder.binding.favimg);
                                final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.fav_animation);
                                holder.binding.favimg.startAnimation(myAnim);

                                myAnim.setAnimationListener(new Animation.AnimationListener() {
                                    public void onAnimationStart(Animation anim) {
                                    }

                                    public void onAnimationRepeat(Animation anim) {
                                    }

                                    public void onAnimationEnd(Animation anim) {
                                        model1.setFavourite(0);
                                        notifyDataSetChanged();
                                    }
                                });
                            }

                        }
                        else{

                        }
                    }

                    @Override
                    public void onFailure(Call<FavouriteModel> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        holder.binding.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStickerSelected("quotes",holder.binding.imageurl.getText().toString());
                DesignStudio.binding.warning.setVisibility(View.VISIBLE);
                DesignStudio.binding.btnApply.setVisibility(View.VISIBLE);
            }
        });

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
