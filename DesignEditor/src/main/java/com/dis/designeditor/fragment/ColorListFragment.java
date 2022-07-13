package com.dis.designeditor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dis.designeditor.adapter.ColorListAdapter;

import com.dis.designeditor.util.SpacesItemDecoration;
import com.dis.designeditor.databinding.ColorFragBinding;

import java.util.ArrayList;

public class ColorListFragment extends Fragment {
    private ColorFragBinding binding;
    StaggeredGridLayoutManager gridLayoutManager;

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

        gridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        gridLayoutManager.invalidateSpanAssignments();
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        binding.recycler.addItemDecoration(new SpacesItemDecoration(20));
        binding.recycler.setLayoutManager(gridLayoutManager);
        ArrayList<String> list=new ArrayList<>();
        list.add("teal_200");
        list.add("teal_700");
        list.add("green");
        list.add("purple");

        ArrayList<String> list1=new ArrayList<>();
        list1.add("#FF03DAC5");
        list1.add("#FF018786");
        list1.add("#63AA11");
        list1.add("#B943D2");

        ColorListAdapter adapter=new ColorListAdapter(getActivity(),list,list1);
        binding.recycler.setAdapter(adapter);
    }
}
