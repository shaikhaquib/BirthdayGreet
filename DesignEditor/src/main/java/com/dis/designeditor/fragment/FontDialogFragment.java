package com.dis.designeditor.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.dis.designeditor.adapter.BottomPager;
import com.dis.designeditor.R;

import com.dis.designeditor.databinding.FontPopupBinding;
import static com.dis.designeditor.activity.DesignStudio.*;


public class FontDialogFragment extends DialogFragment implements  TabLayout.OnTabSelectedListener,View.OnClickListener {
   private static Context context1;
   static boolean isbold=false,isitalic=false;

    public static FontPopupBinding binding;
   // public static Typeface selectedTypeface=null;
    public static  String selectedColor="#000000",selectedTypeface="",selectedsize="4";


    public static interface OnCompleteListener {
        public abstract void onComplete(String text,String font,String color,boolean isbold,boolean isitalic,String textsize);
    }

    private OnCompleteListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FontPopupBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        selectedColor="#000000";
        selectedTypeface="";
        isbold=false;isitalic=false;
        selectedsize="4";

        getDialog().setCanceledOnTouchOutside(false);

        binding.boldimg.setOnClickListener(this);
        binding.italicimg.setOnClickListener(this);
        binding.uploadimg.setOnClickListener(this);
        binding.textimage.setOnClickListener(this);

        binding.text.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                binding.previewtext.setText(s);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });



        binding.bottomSheetTabs.addTab(binding.bottomSheetTabs.newTab().setText("Font"));
        binding.bottomSheetTabs.addTab(binding.bottomSheetTabs.newTab().setText("Font Colour"));
        binding.bottomSheetTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        for(int i=0; i < binding.bottomSheetTabs.getTabCount(); i++) {
            View tab = ((ViewGroup) binding.bottomSheetTabs.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(30, 0, 30, 0);
            tab.requestLayout();
        }
        BottomPager bottomPager = new BottomPager(getChildFragmentManager(), binding.bottomSheetTabs.getTabCount());
        binding.bottomSheetViewpager.setAdapter(bottomPager);
        binding.bottomSheetViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.bottomSheetTabs));
        binding.bottomSheetTabs.addOnTabSelectedListener(this);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        context1=context;
        try {
            this.mListener = (OnCompleteListener)context1;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context1.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dismissAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = getDialog().getWindow();
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.80);
            window.setLayout(width,height);

            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

           window.setGravity(Gravity.CENTER_VERTICAL);
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.0f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(windowParams);

        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        dismissAllowingStateLoss();
      //  this.mListener.onComplete(binding.previewtext.getText().toString(),selectedTypeface,selectedColor,isbold,isitalic,selectedsize);

    }

    @Override
    public void onClick(View view) {
         if(view ==binding.boldimg){
             if(!isbold){
                 binding.boldimg.setBackground(context1.getResources().getDrawable(R.drawable.font_popup_bold_select_bg));
                 isbold=true;
                 if(isitalic){
                     binding.previewtext.setTypeface(binding.previewtext.getTypeface(), Typeface.BOLD_ITALIC);
                 }
                 else{
                     binding.previewtext.setTypeface(binding.previewtext.getTypeface(), Typeface.BOLD);
                 }
             }else{
                 binding.boldimg.setBackground(context1.getResources().getDrawable(R.drawable.font_popup_bold_bg));
                 isbold=false;
                 if(isitalic){
                     binding.previewtext.setTypeface(binding.previewtext.getTypeface(), Typeface.ITALIC);
                 } else{
                     binding.previewtext.setTypeface(Typeface.create(binding.previewtext.getTypeface(), Typeface.NORMAL));
                     // binding.previewtext.setTypeface(binding.previewtext.getTypeface(), Typeface.NORMAL);
                 }
             }
         }
         else if(view==binding.italicimg){
             if(!isitalic){

                 binding.italicimg.setBackground(context1.getResources().getDrawable(R.drawable.font_popup_bold_select_bg));

                 isitalic=true;
                 if(isbold){
                     binding.previewtext.setTypeface(binding.previewtext.getTypeface(), Typeface.BOLD_ITALIC);
                 }
                 else{
                     binding.previewtext.setTypeface(binding.previewtext.getTypeface(), Typeface.ITALIC);
                 }

             }
             else{
                 binding.italicimg.setBackground(context1.getResources().getDrawable(R.drawable.font_popup_bold_bg));
                 isitalic=false;
                 if(isbold){
                     binding.previewtext.setTypeface(binding.previewtext.getTypeface(), Typeface.BOLD);
                 }
                 else{
                     binding.previewtext.setTypeface(Typeface.create(binding.previewtext.getTypeface(), Typeface.NORMAL));
                     //binding.previewtext.setTypeface(binding.previewtext.getTypeface(), Typeface.NORMAL);
                 }
             }
         }
         else if(view == binding.textimage){
             final CharSequence[] items = { "8","9", "10", "11","12","13","14","15","16","17","18","19","20" };
             AlertDialog.Builder builder = new AlertDialog.Builder(context1);
             builder.setTitle("Size selection");
             //builder.setCancelable(true);
             builder.setItems(items, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int item) {
                     binding.textimage.setBackground(context1.getResources().getDrawable(R.drawable.font_popup_bold_select_bg));
                     // will toast your selection
                     selectedsize=items[item].toString();
                     int textsize= Integer.parseInt(selectedsize);
                     binding.previewtext.setTextSize( (convertSpToPx(textsize)));
                   //  showToast("Name: " + items[item]);
                     dialog.dismiss();

                 }
             });
             AlertDialog dialog = builder.create();
             dialog.setCanceledOnTouchOutside(true);
             dialog.setCancelable(true);
             dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                 @Override
                 public void onCancel(DialogInterface dialogInterface) {
                     binding.textimage.setBackground(context1.getResources().getDrawable(R.drawable.font_popup_bold_bg));
                 }
             });
             int width = (int)(getResources().getDisplayMetrics().widthPixels*0.70);
             int height = (int)(getResources().getDisplayMetrics().heightPixels*0.70);
             dialog.getWindow().setLayout(width, height);
             dialog.show();
         } else if(view == binding.uploadimg){
             dismiss();
             this.mListener.onComplete(binding.previewtext.getText().toString(),selectedTypeface,selectedColor,isbold,isitalic,selectedsize);
         }

    }

    private float convertSpToPx(float scaledPixels) {
        return scaledPixels * context1.getResources().getDisplayMetrics().scaledDensity;
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getPosition()==1){
            ColorPickerDialogBuilder
                    .with(context1)
                    .setTitle("Choose color")
                    // .initialColor(000000)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {
                            // toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                        }
                    })
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            // changeBackgroundColor(selectedColor);

                            changeColor("#"+Integer.toHexString(selectedColor));



                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .build()
                    .show();
           // Toast.makeText(getActivity(),"open colour",Toast.LENGTH_LONG).show();
        }
        else{
            binding.bottomSheetViewpager.setCurrentItem(tab.getPosition());
        }

        }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if(tab.getPosition()==1){
            ColorPickerDialogBuilder
                    .with(context1)
                    .setTitle("Choose color")
                    // .initialColor(000000)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {
                            // toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                        }
                    })
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            // changeBackgroundColor(selectedColor);

                            changeColor("#"+Integer.toHexString(selectedColor));



                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .build()
                    .show();
            // Toast.makeText(getActivity(),"open colour",Toast.LENGTH_LONG).show();
        }
    }

 public static void changeFont(String name){
     if(binding.previewtext.getText().equals("")){

         Toast.makeText(context1,"Please enter text first",Toast.LENGTH_SHORT).show();
     }
     else{
         if(name.equals("")){
             selectedTypeface=name;
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                // binding.previewtext.setTypeface(null);
                 if(isbold && isitalic){
                     binding.previewtext.setTypeface(null, Typeface.BOLD_ITALIC);

                 }
                 else if(isbold){
                     binding.previewtext.setTypeface(null, Typeface.BOLD);
                 }
                 else if(isitalic){
                     binding.previewtext.setTypeface(null, Typeface.ITALIC);
                 }
                 else{
                     binding.previewtext.setTypeface(null);
                 }
             }
         }
         else{
             selectedTypeface=name;
             Typeface face = fontProvider.getTypeface(name);

             if(isbold && isitalic){
                 binding.previewtext.setTypeface(face, Typeface.BOLD_ITALIC);

             }
             else if(isbold){
                 binding.previewtext.setTypeface(face, Typeface.BOLD);
             }
             else if(isitalic){
                 binding.previewtext.setTypeface(face, Typeface.ITALIC);
             }
             else{
                 binding.previewtext.setTypeface(face);
             }
         }


     }


 }

    public static void changeColor(String name){
        if(binding.previewtext.getText().equals("")){

            Toast.makeText(context1,"Please enter text first",Toast.LENGTH_SHORT).show();
        }
        else{
            if(name.equals("")){
                selectedColor="#ffffff";
                binding.previewtext.setTextColor(Color.parseColor("#ffffff"));
            }
            else{
                selectedColor=name;
                binding.previewtext.setTextColor(Color.parseColor(name));
            }
        }


    }


}
