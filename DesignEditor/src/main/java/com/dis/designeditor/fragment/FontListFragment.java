package com.dis.designeditor.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dis.designeditor.adapter.FontListAdapter;
import com.dis.designeditor.api.RetrofitClient;
import com.dis.designeditor.databinding.ColorFragBinding;
import com.dis.designeditor.model.DesignStudioBottomModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dis.designeditor.activity.DesignStudio.fontClientCd;
import static com.dis.designeditor.activity.DesignStudio.fonts;

public class FontListFragment extends Fragment {
    private ColorFragBinding binding;
    GridLayoutManager gridLayoutManager;
    private boolean isLoading = true;
    private int postVisibleItem,visibleItemCount,totalItemCount,previousTotal=0;
    private int viewThreshold=5;
    private int currentPage = 1;
    FontListAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ColorFragBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* gridLayoutManager = new GridLayoutManager(getActivity(),2);
        binding.recycler.addItemDecoration(new SpacesItemDecoration(1));*/
        binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        setPostData();

    }

    private void setPostData(){
        ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> fontList = new ArrayList<>();

        for (int i = 0; i < fonts.size(); i++) {
            DesignStudioBottomModel.ClientWiseMaterialList clientWiseMaterialList1 =
                    new DesignStudioBottomModel.ClientWiseMaterialList(
                            0, 10L, "",
                            fonts.get(i), 13L, "1");
            fontList.add(clientWiseMaterialList1);
        }
        adapter=new FontListAdapter(getActivity(),fontList);
        binding.recycler.setAdapter(adapter);





    }
    private void performPagination() {

        final ArrayList<DesignStudioBottomModel.ClientWiseMaterialList> items = new ArrayList<>();

        Call<DesignStudioBottomModel> call=RetrofitClient
                .getInstance().getApi().designStudioApi(RetrofitClient.AppName,"Fonts", fontClientCd,currentPage);
        call.enqueue(new Callback<DesignStudioBottomModel>() {
            @Override
            public void onResponse(Call<DesignStudioBottomModel> call, Response<DesignStudioBottomModel> response) {
                Log.d("footer_image","performPagination-"+response.code()+" "+response.body().getClientWiseMaterialList().size()+" current"+currentPage);

                if(response.body().getClientWiseMaterialList()!=null){
                    if(response.body().getClientWiseMaterialList().size()>0){
                        adapter.addItems(response.body().getClientWiseMaterialList());
                    }


                }

            }

            @Override
            public void onFailure(Call<DesignStudioBottomModel> call, Throwable t) {

            }
        });





    }
}
