package com.image.process.designeditor.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.image.process.R;
import com.image.process.Test;
import com.image.process.TextEditorDialogFragment;
import com.image.process.databinding.ActivityDesignStudioBinding;

import com.image.process.model.AllDetailsOfClientItem;
import com.image.process.model.HeaderResponse;
import com.image.process.stickerview.BitmapStickerIcon;
import com.image.process.stickerview.DeleteIconEvent;
import com.image.process.stickerview.DrawableSticker;
import com.image.process.stickerview.Sticker;
import com.image.process.stickerview.StickerView;
import com.image.process.stickerview.TextSticker;
import com.image.process.stickerview.ZoomIconEvent;
import com.image.process.stickerview.util.FontProvider;
import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.rtugeek.android.colorseekbar.OnColorChangeListener;
import com.theartofdev.edmodo.cropper.CropImage;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.TextShadow;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;


public class DesignStudio extends AppCompatActivity implements View.OnClickListener,  DialogInterface.OnDismissListener, OnPhotoEditorListener {
    public static ActivityDesignStudioBinding binding;
    public static String userID;
    PhotoEditor mPhotoEditor;
    boolean showImage = true;


    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;

    int editorTextColorCode;
    float editorTextSize = 30f;
    public static final int PERM_RQST_CODE = 110;
    static Context contextMain;
    public static ArrayList<Drawable> saveDrawable = new ArrayList<>();
    public static int position = 000;
    static Drawable drawable;
    public static int fontClientCd;
    private boolean isUserNameEdit = false;
    public static FontProvider fontProvider;
    public static List<String> fonts = new ArrayList<>();
    TextShadow textShadow=new TextShadow(2,2,2,Color.parseColor("#000000"));
    int footerSelectedPosition = -1 ;
    private void requestAppPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 200); // your request code
    }


    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }





    private List<AllDetailsOfClientItem> footerList = new ArrayList<>();




    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        binding = ActivityDesignStudioBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        requestAppPermissions();
        contextMain = this;
        fontProvider = new FontProvider(getResources());
        fonts = fontProvider.getFontNames();

        bottom_sheet = binding.bottomSheet;
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        mPhotoEditor = new PhotoEditor.Builder(this, binding.photoEditorView)
                .setPinchTextScalable(true)
                // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk


        mPhotoEditor.setOnPhotoEditorListener(this);



        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        binding.stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon));
        binding.stickerView.setBackgroundColor(Color.WHITE);
        binding.stickerView.setLocked(false);
        binding.stickerView.setConstrained(true);

        binding.stickerView1.setIcons(Arrays.asList(deleteIcon, zoomIcon));
        binding.stickerView1.setBackgroundColor(Color.WHITE);
        binding.stickerView1.setLocked(false);
        binding.stickerView1.setConstrained(true);


        binding.clientmaterial.setOnClickListener(this);
        binding.customFooter.setOnClickListener(this);

        binding.text.setOnClickListener(this);
        binding.save.setOnClickListener(this);
        binding.btnApply.setOnClickListener(this);
        binding.back.setOnClickListener(this);
      
        /*binding.gesture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(DesignStudio.this, "backgroundToch", Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/
        String extras = getIntent().getStringExtra("image");
        if (extras != null) {
            String image = extras;
            Glide.with(getApplicationContext())
                    .load(image)
                    // .transition(GenericTransitionOptions.with(animationObject))
                    .placeholder(R.color.white)

                    .into(binding.photoView);

            binding.photoBack.setBackgroundColor(Color.parseColor("#000000"));



            binding.gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePicker.with(DesignStudio.this)
                            .galleryMimeTypes(
                                    new String[]{
                                            "image/png",
                                            "image/jpg",
                                            "image/jpeg"
                                    } //Exclude gif images
                            )
                            .start();

                }
            });
            binding.customFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePicker.with(DesignStudio.this)
                            .galleryMimeTypes(
                                    new String[]{
                                            "image/png",
                                            "image/jpg",
                                            "image/jpeg"
                                    } //Exclude gif images
                            )
                            .start(1100);

                }
            });


        }





        getFooters();

    }




    private void getFooters() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.254:8080/icreate/API/Client_Det_And_HeaderImage.php";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Footer ", "onResponse: "+ response);
                        Gson gson = new Gson();
                        HeaderResponse m = gson.fromJson(response, HeaderResponse.class);
                        footerList.clear();
                        for  (AllDetailsOfClientItem itemfooter:m.getAllDetailsOfClient()) {
                            if (itemfooter != null)
                            footerList.add(itemfooter);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }





    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        if (view == binding.clientmaterial) {
            showBottomSheetDialogFooter();
        } else if (view == binding.text) {
            TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
            textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode) -> {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);
                styleBuilder.withTextSize(30f);
                styleBuilder.withTextShadow(textShadow);
                mPhotoEditor.addText(inputText, styleBuilder);
                //  mTxtCurrentTool.setText(R.string.label_text);

                binding.editorMessage.setVisibility(View.VISIBLE);
            });

        } else if (view == binding.btnApply) {
            binding.warning.setVisibility(View.GONE);
            binding.btnApply.setVisibility(View.GONE);
            saveDrawable.add(drawable);
            position = 000;
            binding.clientmaterial.setClickable(true);
            //  binding.clientmaterial.getDrawable().setAlpha(255);

            binding.text.setClickable(true);
            //binding.text.getDrawable().setAlpha(255);
        } else if (view == binding.save) {
            mPhotoEditor.clearHelperBox();
            Test.Companion.genrateBitMap(binding.stickerView.createBitmap(), this);
        } else if (view == binding.back) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.alert))
                    .setMessage(R.string.back_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.procees, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                binding.stickerView.removeAllStickers();
                Uri resultUri = result.getUri();
                binding.photoView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            //   profilePic.setImageURI(fileUri);

            if (requestCode == 1100){
                addFrontStickerFromGallery(fileUri);
            } else {
                addStickerFromGallery(fileUri);

            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    private void addFrontStickerFromGallery(Uri fileUri) {
        {
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(fileUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Drawable drawable = Drawable.createFromStream(inputStream, fileUri.toString());
            binding.stickerView.addSticker(new DrawableSticker(drawable));
            binding.stickerView.setConstrained(true);

            binding.stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
                @Override
                public void onStickerAdded(@NonNull @NotNull Sticker sticker) {

                }

                @Override
                public void onStickerClicked(@NonNull @NotNull Sticker sticker) {

                }

                @Override
                public void onStickerDeleted(@NonNull @NotNull Sticker sticker) {

                }

                @Override
                public void onStickerDragFinished(@NonNull @NotNull Sticker sticker) {

                }

                @Override
                public void onStickerTouchedDown(@NonNull @NotNull Sticker sticker) {

                }

                @Override
                public void onStickerZoomFinished(@NonNull @NotNull Sticker sticker) {

                }

                @Override
                public void onStickerFlipped(@NonNull @NotNull Sticker sticker) {

                }

                @Override
                public void onStickerDoubleTapped(@NonNull @NotNull Sticker sticker) {

                }

                @Override
                public void onStickerTouchFinish(@NonNull @NotNull Sticker sticker) {

                }
            });
        }
    }

    private void addStickerFromGallery(Uri fileUri) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(fileUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Drawable drawable = Drawable.createFromStream(inputStream, fileUri.toString());
        binding.stickerView1.addSticker(new DrawableSticker(drawable));
        binding.stickerView1.setConstrained(true);

        binding.stickerView1.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull @NotNull Sticker sticker) {

            }

            @Override
            public void onStickerClicked(@NonNull @NotNull Sticker sticker) {

            }

            @Override
            public void onStickerDeleted(@NonNull @NotNull Sticker sticker) {

            }

            @Override
            public void onStickerDragFinished(@NonNull @NotNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchedDown(@NonNull @NotNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull @NotNull Sticker sticker) {

            }

            @Override
            public void onStickerFlipped(@NonNull @NotNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull @NotNull Sticker sticker) {

            }

            @Override
            public void onStickerTouchFinish(@NonNull @NotNull Sticker sticker) {

            }
        });
    }

    public static void onStickerSelected(String comingFrom, String imageurl) {
        DesignStudio.binding.warning.setVisibility(View.VISIBLE);
        DesignStudio.binding.btnApply.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {


                Drawable drawableImage = getImageDrawable(imageurl);


                binding.stickerView.addSticker(new DrawableSticker(drawableImage));

                binding.stickerView.setConstrained(true);
                position = 1;
                binding.stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
                    @Override
                    public void onStickerAdded(@NonNull Sticker sticker) {


                    }

                    @Override
                    public void onStickerClicked(@NonNull Sticker sticker) {
                        // Log.d("Sticker_overide","onStickerClicked called");
                    }

                    @Override
                    public void onStickerDeleted(@NonNull Sticker sticker) {
                        // Log.d("Sticker_overide","onStickerDeleted called");
                        binding.clientmaterial.setClickable(true);

                        binding.text.setClickable(true);
                        position = 000;
                    }

                    @Override
                    public void onStickerDragFinished(@NonNull Sticker sticker) {
                        // Log.d("Sticker_overide","onStickerDragFinished called");


                    }

                    @Override
                    public void onStickerTouchedDown(@NonNull Sticker sticker) {
                        //   Log.d("Sticker_overide","onStickerTouchedDown called");
                    }

                    @Override
                    public void onStickerZoomFinished(@NonNull Sticker sticker) {


                    }

                    @Override
                    public void onStickerFlipped(@NonNull Sticker sticker) {
                        // Log.d("Sticker_overide","onStickerFlipped called");
                    }

                    @Override
                    public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                        // Log.d("Sticker_overide","onStickerDoubleTapped called");
                    }

                    @Override
                    public void onStickerTouchFinish(@NonNull Sticker sticker) {

                        if (sticker != null) {
                            drawable = sticker.getDrawable();
                        }

                    }
                });

            }
        }).start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        Log.d("fontpoup444--", "onDismiss called");
    }

    public static Drawable getImageDrawable(String url) {
        try {
            Log.d("TAG", "Getting Drawable for StringUrl: " + url);
            return getImageDrawable(new URL(url));
        } catch (MalformedURLException e) {
            Log.e("TAG", e.getMessage(), e);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        }

        return null;
    }

    public static Drawable getImageDrawable(URL url) {
        return getImageDrawable(url, null, null);
    }

    public static Drawable getImageDrawable(URL url, String username, String password) {
        Drawable d = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url.toURI());
            if (username != null && username.length() > 0 && password != null && password.length() > 0) {
                client.getCredentialsProvider().setCredentials(new AuthScope(url.getHost(), url.getPort()), new UsernamePasswordCredentials(username, password));
            }
            HttpResponse response = client.execute(get);
            InputStream is = response.getEntity().getContent();
            BufferedInputStream bis = new BufferedInputStream(is);
            String full = url.getFile();
            int start = full.lastIndexOf("/") + 1;
            if (full.length() > 0) {
                full = full.substring(start);
            }
            d = Drawable.createFromStream(bis, full);
            if (d == null) {
                Log.w("TAG", "Drawable was null: " + full + ": " + url.toString());
            }
            bis.close();
            is.close();
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        }
        return d;
    }

    public static File commonDocumentDirPath(String FolderName) {
        File dir = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + FolderName);
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/" + FolderName);
        }


        return dir;

    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
       /* TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener((inputText, newColorCode) -> {

           // mTxtCurrentTool.setText(R.string.label_text);
        });*/


        showBottomSheetDialog(text, colorCode, rootView);
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onTouchSourceImage(MotionEvent event) {

    }

    @SuppressLint("NewApi")
    private void showBottomSheetDialog(String text, final int Code, View rootView) {
        final TextStyleBuilder styleBuilder = new TextStyleBuilder();
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        editorTextColorCode = Code;
        final View view;

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setCancelable(false);
        view = getLayoutInflater().inflate(R.layout.bottomsheet_texteditor, null);
        mBottomSheetDialog.getWindow().setDimAmount(0f);
        mBottomSheetDialog.setContentView(view);

        EditText txtTitle = view.findViewById(R.id.editText);
        RecyclerView rvFont = view.findViewById(R.id.rvFont);
        ColorSeekBar colorSlider = view.findViewById(R.id.colorSlider);
        SeekBar sizeSlider = view.findViewById(R.id.seekBar7);
        sizeSlider.setProgress(30);
        txtTitle.setText(text);
        LinearLayout liTextSetting = view.findViewById(R.id.textSetting);
        LinearLayout liTextStyle = view.findViewById(R.id.textStyle);
        ImageView alignLeft = view.findViewById(R.id.alignLeft);
        ImageView alignRight = view.findViewById(R.id.alignRight);
        ImageView alignCenter = view.findViewById(R.id.alignCenter);


        alignCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styleBuilder.withTextColor(editorTextColorCode);
                String inputText = txtTitle.getText().toString();
                styleBuilder.withTextSize(editorTextSize);
                styleBuilder.withTextShadow(textShadow);
                styleBuilder.withGravity(Gravity.CENTER);
                mPhotoEditor.editText(rootView, inputText, styleBuilder);
            }
        });
        alignRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styleBuilder.withTextColor(editorTextColorCode);
                String inputText = txtTitle.getText().toString();
                styleBuilder.withTextSize(editorTextSize);
                styleBuilder.withTextShadow(textShadow);
                styleBuilder.withGravity(Gravity.RIGHT);
                mPhotoEditor.editText(rootView, inputText, styleBuilder);
            }
        });
        alignLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styleBuilder.withTextColor(editorTextColorCode);
                String inputText = txtTitle.getText().toString();
                styleBuilder.withTextSize(editorTextSize);
                styleBuilder.withTextShadow(textShadow);
                styleBuilder.withGravity(Gravity.LEFT);
                mPhotoEditor.editText(rootView, inputText, styleBuilder);
            }
        });

        view.findViewById(R.id.tabSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liTextSetting.setVisibility(View.VISIBLE);
                liTextStyle.setVisibility(View.GONE);
            }
        });
        view.findViewById(R.id.tabStyle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liTextSetting.setVisibility(View.GONE);
                liTextStyle.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtTitle.getWindowToken(), 0);
            }
        });

        rvFont.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @NotNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(DesignStudio.this).inflate(R.layout.font_list_layout, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
                ViewHolder viewHolder = (ViewHolder) holder;
                String fontName = fonts.get(position);

                viewHolder.textView.setText(fontName);
                viewHolder.textView.setTypeface(fontProvider.getTypeface(fontName));
                viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        styleBuilder.withTextColor(editorTextColorCode);
                        String inputText = txtTitle.getText().toString();
                        styleBuilder.withTextSize(editorTextSize);
                        styleBuilder.withTextShadow(textShadow);
                        styleBuilder.withTextFont(fontProvider.getTypeface(fontName));
                        mPhotoEditor.editText(rootView, inputText, styleBuilder);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return fonts.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {

                final TextView textView;

                public ViewHolder(@NonNull @NotNull View itemView) {
                    super(itemView);
                    textView = itemView.findViewById(R.id.fontname);
                }
            }
        });


        sizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                styleBuilder.withTextColor(editorTextColorCode);
                String inputText = txtTitle.getText().toString();
                editorTextSize = seekBar.getProgress();
                styleBuilder.withTextSize(editorTextSize);
                styleBuilder.withTextShadow(textShadow);
                mPhotoEditor.editText(rootView, inputText, styleBuilder);
            }
        });


        colorSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ");
            }
        });

        colorSlider.setOnColorChangeListener(new OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int progress, int color) {
                editorTextColorCode = color;
                styleBuilder.withTextColor(editorTextColorCode);
                styleBuilder.withTextShadow(textShadow);
                String inputText = txtTitle.getText().toString();
                mPhotoEditor.editText(rootView, inputText, styleBuilder);
            }
        });


        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             //   updateEditor(editorTextColorCode, txtTitle, rootView);

                styleBuilder.withTextColor(editorTextColorCode);
                styleBuilder.withTextShadow(textShadow);
                String inputText = txtTitle.getText().toString();
                mPhotoEditor.editText(rootView, inputText, styleBuilder);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
    }



    private void showBottomSheetDialogFooter() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_footer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            bottomSheetDialog.getWindow().setDimAmount(0f);
            bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getBehavior().setPeekHeight(800,true);


        ImageView btnCancel = bottomSheetDialog.findViewById(R.id.btnCancel);
        RecyclerView rvFooter = bottomSheetDialog.findViewById(R.id.rvFooter);
        ImageView btnClose = bottomSheetDialog.findViewById(R.id.btnClose);
        EditText edtSearch = bottomSheetDialog.findViewById(R.id.edtSearch);

        btnClose.setOnClickListener(view -> {
            edtSearch.getText().clear();
        });

        FooterAdapter footerAdapter = new FooterAdapter(this,footerList);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used, but you must override it
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                footerAdapter.filter(edtSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used, but you must override it
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        rvFooter.setAdapter(footerAdapter);

        bottomSheetDialog.show();
    }

}