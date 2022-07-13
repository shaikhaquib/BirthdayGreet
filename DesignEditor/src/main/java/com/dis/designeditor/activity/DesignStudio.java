package com.dis.designeditor.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.dis.designeditor.R;
import com.dis.designeditor.Test;
import com.dis.designeditor.TextEditorDialogFragment;
import com.dis.designeditor.api.DBHelper;
import com.dis.designeditor.api.RetrofitClient;
import com.dis.designeditor.databinding.ActivityDesignStudioBinding;
import com.dis.designeditor.fragment.FontDialogFragment;
import com.dis.designeditor.model.FooterImagesItem;
import com.dis.designeditor.model.FooterResponse;
import com.dis.designeditor.model.HeaderImage;
import com.dis.designeditor.model.HeaderModel;
import com.dis.designeditor.stickerview.BitmapStickerIcon;
import com.dis.designeditor.stickerview.DeleteIconEvent;
import com.dis.designeditor.stickerview.DrawableSticker;
import com.dis.designeditor.stickerview.Sticker;
import com.dis.designeditor.stickerview.StickerView;
import com.dis.designeditor.stickerview.TextSticker;
import com.dis.designeditor.stickerview.ZoomIconEvent;
import com.dis.designeditor.util.FontProvider;
import com.dis.designeditor.util.WrappedDrawable;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DesignStudio extends AppCompatActivity implements View.OnClickListener, FontDialogFragment.OnCompleteListener, DialogInterface.OnDismissListener, OnPhotoEditorListener {
    public static ActivityDesignStudioBinding binding;
    public static String userProfile;
    public static String userName;
    public static String phone;
    public static String userID;
    public static String Position;
    public static String location;
    public static String partyLogo;
    public static String instaID;
    public static String fbID;
    public static String tweetID;
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

    Bitmap b = null;

    private void check(int x, int y) {
        if (b != null && Color.alpha(b.getPixel(x, y)) >= 0)
            onMovedOutOfShape();
    }

    private void onMovedOutOfShape() {
        Toast.makeText(this, "You're out", Toast.LENGTH_SHORT).show();
    }

    LocalBroadcastManager mLocalBroadcastManager;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.keights.mipudhari.DeleteUser")) {
                finish();
            }
        }
    };
    public static final String PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE";
    private List<FooterImagesItem> footerList = new ArrayList<>();
    private List<FooterImagesItem> customFooterIimages = new ArrayList<>();
    private List<HeaderImage> headerImages = new ArrayList<>();

    private DBHelper mydb ;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        binding = ActivityDesignStudioBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mydb = new DBHelper(this);

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

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.keights.mipudhari.DeleteUser");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);

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
        binding.btnheader.setOnClickListener(this);

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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String image = getIntent().getStringExtra("image");
            Glide.with(getApplicationContext())
                    .load(image)
                    // .transition(GenericTransitionOptions.with(animationObject))
                    .placeholder(R.color.white)

                    .into(binding.photoView);

            binding.photoBack.setBackgroundColor(Color.parseColor("#000000"));

            if (getIntent().hasExtra("type")) {
                if (getIntent().getStringExtra("type").equals("1")) {
                    binding.gallery.setVisibility(View.VISIBLE);
                } else {
                    binding.gallery.setVisibility(View.GONE);
                }
            } else {
                binding.gallery.setVisibility(View.GONE);
            }

            if (getIntent().hasExtra("showImage")){
                if (getIntent().getIntExtra("showImage",1) == 0 ){
                    showImage = false;
                    binding.imageView.setVisibility(View.GONE);
                    binding.guideline3.setGuidelinePercent(0.16f);
                }
            }

            binding.gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePicker.with(DesignStudio.this)
                            .cropSquare() //Crop image(Optional), Check Customization for more option
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


        }
        if (!userProfile.isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(userProfile)
                    // .transition(GenericTransitionOptions.with(animationObject))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                    .into(binding.imageView);

        } else {
            Glide.with(getApplicationContext()).load("userProfile").placeholder(R.color.white).into(binding.imageView);
        }

        if (tweetID != null && !tweetID.isEmpty()) {
            binding.tweet.setText(tweetID);
        }else {
            tweetID = userName;
            binding.tweet.setText(tweetID);
        }
        if (instaID != null && !instaID.isEmpty()) {
            binding.whatsapp.setText(instaID);
        }else {
            instaID = userName;
            binding.whatsapp.setText(instaID);
        }
        if (fbID != null && !fbID.isEmpty()) {
            binding.fb.setText(fbID);
        }else {
            fbID = userName;
            binding.fb.setText(fbID);
        }

        if (partyLogo != null && !partyLogo.isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(partyLogo)
                    // .transition(GenericTransitionOptions.with(animationObject))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                    .into(binding.partyLogo);
        }
        if (!userName.isEmpty()) {
            binding.textView.setText(camelCase(userName));
        }
        if (!phone.isEmpty())
            //  binding.whatsapp.setText("+91-"+phone);

            if (!Position.isEmpty())
                binding.position.setText(camelCase(Position));
            else
                binding.politicalContainer.setVisibility(View.GONE);
        if (!location.isEmpty())
            binding.locattion.setText( camelCase(location).trim() );


        getFooters();
        getHeader();

    }


    public static String camelCase(String s) {
        if (s.length() == 0) {
            return s;
        }
        String[] parts = s.split(" ");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part) + " ";
        }
        return camelCaseString;
    }

    @SuppressLint("NewApi")
    public static String toProperCase(String s) {
        if (!s.isEmpty()&&!s.equals("")) {
            return s.substring(0, 1).toUpperCase() +
                    s.substring(1).toLowerCase();
        }else {
            return s;
        }
    }

    private void getFooters() {
        Call<FooterResponse> call = RetrofitClient
                .getInstance().getApi().getFooter(userID);
        call.enqueue(new Callback<FooterResponse>() {
            @Override
            public void onResponse(Call<FooterResponse> call, Response<FooterResponse> response) {

                try {
                    if (response.body().getFooterImages() != null) {
                        List<FooterImagesItem> footerImages = response.body().getFooterImages();
                        footerList = footerImages;
                        customFooterIimages = response.body().getCustomFooterIimages();
                        if (customFooterIimages.size() == 0) {
                            binding.customFooter.setVisibility(View.GONE);
                        }

                        if (footerList.size() > 0) {
                            if (customFooterIimages.size() > 0) {
                                Glide.with(getApplicationContext())
                                        .load(customFooterIimages.get(0).getImgLink())
                                        // .transition(GenericTransitionOptions.with(animationObject))
                                        .placeholder(R.color.white)
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                                        .into(binding.footer);
                                binding.imageView.setVisibility(View.GONE);
                                binding.footerGroup.setVisibility(View.GONE);

                            }
                            else {
                                Glide.with(getApplicationContext())
                                        .load(footerList.get(0).getImgLink())
                                        // .transition(GenericTransitionOptions.with(animationObject))
                                        .placeholder(R.color.white)
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                                        .into(binding.footer);
                                if (showImage) {
                                    binding.imageView.setVisibility(View.VISIBLE);
                                } else {
                                    binding.imageView.setVisibility(View.GONE);
                                }
                            }

                        }

                        FooterImagesItem footerImagesItem = new FooterImagesItem();
                        footerImagesItem.setId("-1");
                        footerImagesItem.setImgLink("");
                        if (!showImage)
                        footerList.add(0, footerImagesItem);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Exception", "onResponse: " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<FooterResponse> call, Throwable t) {

            }
        });


    }
    private void getHeader() {


        Call<HeaderModel> call = RetrofitClient
                .getInstance().getApi().getHeader(userID);
        call.enqueue(new Callback<HeaderModel>() {
            @Override
            public void onResponse(Call<HeaderModel> call, Response<HeaderModel> response) {

                try {
                    if (response.body().getHeaderImages() != null) {
                        List<HeaderImage> images = response.body().getHeaderImages();
                        headerImages = images;
                        HeaderImage headerImage = new HeaderImage();
                        headerImage.setId("-1");
                        headerImage.setImgLink("");
                        if (!showImage)
                        headerImages.add(0,headerImage);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Exception", "onResponse: " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<HeaderModel> call, Throwable t) {

            }
        });


    }





    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        if (view == binding.clientmaterial) {
            showBottomSheetDialogFooter();
        } else if (view == binding.btnheader) {
            showBottomSheetDialogHeader();
        } else if (view == binding.customFooter) {
            showCustomBottomSheetDialogFooter();
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
            addStickerFromGallery(fileUri);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
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
    public void onComplete(String text, String font, String color, boolean isbold, boolean isitalic, String textsize) {
        if (!isUserNameEdit) {
            Log.d("fontpoup444--", "onComplete called--" + text + "--" + font + "--" + color + "--" + isbold + "--" + isitalic);
            binding.clientmaterial.setClickable(true);


            if (!text.equals("")) {


                TextSticker sticker = new TextSticker(contextMain);

                Drawable drawa = ContextCompat.getDrawable(this, R.drawable.sticker_transparent_background);
                WrappedDrawable wrappedDrawable = new WrappedDrawable(drawa);
// set bounds on wrapper

                int width = 150;
                int height = 8;
                if (text.length() < 22) {
                    width = 6 * text.length();
                } else {
                    float chunkSize = text.length() / 8;
                    int linecount = (int) Math.ceil(chunkSize);
                    if (linecount > 6) {
                        linecount = 6;
                    }
                    height = linecount * 8;
                }

                wrappedDrawable.setBounds(0, 0, width, height);

                sticker.setDrawable(wrappedDrawable);
                sticker.setText(text);
                if (font.equals("")) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        // binding.previewtext.setTypeface(null);
                        if (isbold && isitalic) {
                            sticker.setTypeface(null, Typeface.BOLD_ITALIC);

                        } else if (isbold) {
                            sticker.setTypeface(null, Typeface.BOLD);
                        } else if (isitalic) {
                            sticker.setTypeface(null, Typeface.ITALIC);
                        } else {
                            //  sticker.setTypeface(null);
                        }
                    }
                } else {

                    Typeface face = fontProvider.getTypeface(font);


//             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                 binding.previewtext.setTypeface(face);
//             }
                    if (isbold && isitalic) {
                        sticker.setTypeface(face, Typeface.BOLD_ITALIC);

                    } else if (isbold) {
                        sticker.setTypeface(face, Typeface.BOLD);
                    } else if (isitalic) {
                        sticker.setTypeface(face, Typeface.ITALIC);
                    } else {
                        sticker.setTypeface(face, Typeface.NORMAL);
                    }
                }
                // sticker.setTypeface()
                sticker.setTextColor(Color.parseColor(color));
                sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                int size = Integer.parseInt(textsize);
                sticker.setMaxTextSize(size);
                sticker.setMinTextSize(size);
                sticker.resizeText();

                binding.stickerView.addSticker(sticker);
                position = 1;
                DesignStudio.binding.warning.setVisibility(View.VISIBLE);
                DesignStudio.binding.btnApply.setVisibility(View.VISIBLE);


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
        } else {
            if (!text.equals("")) {
                isUserNameEdit = false;

                binding.textView.setText(text);
                binding.textView.setTextColor(Color.parseColor(color));

                TextView sticker = binding.textView;

                sticker.setText(text);
                if (font.equals("")) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        // binding.previewtext.setTypeface(null);
                        if (isbold && isitalic) {
                            sticker.setTypeface(null, Typeface.BOLD_ITALIC);

                        } else if (isbold) {
                            sticker.setTypeface(null, Typeface.BOLD);
                        } else if (isitalic) {
                            sticker.setTypeface(null, Typeface.ITALIC);
                        } else {
                            //  sticker.setTypeface(null);
                        }
                    }
                } else {

                    Typeface face = Typeface.createFromAsset(getAssets(),
                            "fonts/" + font + ".ttf");

//             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                 binding.previewtext.setTypeface(face);
//             }
                    if (isbold && isitalic) {
                        sticker.setTypeface(face, Typeface.BOLD_ITALIC);

                    } else if (isbold) {
                        sticker.setTypeface(face, Typeface.BOLD);
                    } else if (isitalic) {
                        sticker.setTypeface(face, Typeface.ITALIC);
                    } else {
                        sticker.setTypeface(face, Typeface.NORMAL);
                    }
                }
                // sticker.setTypeface()
                sticker.setTextColor(Color.parseColor(color));
            }

        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        Log.d("fontpoup444--", "onDismiss called");
    }

    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
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
        bottomSheetDialog.setCancelable(false);

        ImageView btnCancel = bottomSheetDialog.findViewById(R.id.btnCancel);
        RecyclerView rvFooter = bottomSheetDialog.findViewById(R.id.rvFooter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        rvFooter.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @NotNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(DesignStudio.this).inflate(R.layout.item_footer, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
                ViewHolder viewHolder = (ViewHolder) holder;


                if (footerList.get(position).getId() == null || Integer.parseInt(footerList.get(position).getId() ) != -1)
                    Glide.with(getApplicationContext())
                            .load(footerList.get(position).getImgLink())
                            // .transition(GenericTransitionOptions.with(animationObject))
                            .placeholder(R.color.white)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                            .into(viewHolder.footer);
                else
                    viewHolder.footer.setImageResource(R.drawable.hidden);


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (footerList.get(position).getId() == null){
                            Glide.with(getApplicationContext())
                                    .load(footerList.get(position).getImgLink())
                                    // .transition(GenericTransitionOptions.with(animationObject))
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                                    .into(binding.footer);


                            if (binding.footerGroup.getVisibility() == View.GONE){
                                binding.footerGroup.setVisibility(View.VISIBLE);
                                binding.footer.setVisibility(View.VISIBLE);
                                if (showImage)
                                    binding.imageView.setVisibility(View.VISIBLE);
                                else
                                    binding.imageView.setVisibility(View.GONE);

                            }

                        }else {
                            if (binding.footerGroup.getVisibility() == View.VISIBLE){
                                binding.footerGroup.setVisibility(View.GONE);
                                binding.footer.setVisibility(View.GONE);
                                binding.imageView.setVisibility(View.GONE);
                            }

                        }
                    }
                });

            }

            @Override
            public int getItemCount() {
                return footerList.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                final ImageView footer;

                public ViewHolder(@NonNull @NotNull View itemView) {
                    super(itemView);
                    footer = itemView.findViewById(R.id.footer);
                }
            }
        });


        bottomSheetDialog.show();
    }
    private void showCustomBottomSheetDialogFooter() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_footer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            bottomSheetDialog.getWindow().setDimAmount(0f);
            bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        bottomSheetDialog.setCancelable(false);

        ImageView btnCancel = bottomSheetDialog.findViewById(R.id.btnCancel);
        RecyclerView rvFooter = bottomSheetDialog.findViewById(R.id.rvFooter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        rvFooter.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @NotNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(DesignStudio.this).inflate(R.layout.item_footer, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
                ViewHolder viewHolder = (ViewHolder) holder;


                if (customFooterIimages.get(position).getId() == null || Integer.parseInt(customFooterIimages.get(position).getId() ) != -1)
                    Glide.with(getApplicationContext())
                            .load(customFooterIimages.get(position).getImgLink())
                            // .transition(GenericTransitionOptions.with(animationObject))
                            .placeholder(R.color.white)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                            .into(viewHolder.footer);
                else
                    viewHolder.footer.setImageResource(R.drawable.hidden);


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customFooterIimages.get(position).getId() == null){
                            Glide.with(getApplicationContext())
                                    .load(customFooterIimages.get(position).getImgLink())
                                    // .transition(GenericTransitionOptions.with(animationObject))
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                                    .into(binding.footer);
                            
                            if (binding.footer.getVisibility() == View.VISIBLE){
                                binding.footerGroup.setVisibility(View.GONE);
                                binding.imageView.setVisibility(View.GONE);

                                if (binding.footer.getVisibility() == View.GONE){
                                    binding.footer.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    }
                });

            }

            @Override
            public int getItemCount() {
                return customFooterIimages.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                final ImageView footer;

                public ViewHolder(@NonNull @NotNull View itemView) {
                    super(itemView);
                    footer = itemView.findViewById(R.id.footer);
                }
            }
        });


        bottomSheetDialog.show();
    }
    private void showBottomSheetDialogHeader() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_footer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            bottomSheetDialog.getWindow().setDimAmount(0f);
            bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        bottomSheetDialog.setCancelable(false);

        ImageView btnCancel = bottomSheetDialog.findViewById(R.id.btnCancel);
        RecyclerView rvFooter = bottomSheetDialog.findViewById(R.id.rvFooter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        rvFooter.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @NotNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(DesignStudio.this).inflate(R.layout.item_footer, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
                ViewHolder viewHolder = (ViewHolder) holder;

                if (Integer.parseInt(headerImages.get(position).getId() ) != -1)
                 Glide.with(getApplicationContext())
                        .load(headerImages.get(position).getImgLink())
                        // .transition(GenericTransitionOptions.with(animationObject))
                        .placeholder(R.color.white)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                        .into(viewHolder.footer);
                else 
                    viewHolder.footer.setImageResource(R.drawable.hidden);
                    

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if (Integer.parseInt(headerImages.get(position).getId() ) == -1){
                                if (binding.header.getVisibility() == View.VISIBLE){
                                    binding.header.setVisibility(View.GONE);
                                }
                            }else {
                                Glide.with(getApplicationContext())
                                        .load(headerImages.get(position).getImgLink())
                                        // .transition(GenericTransitionOptions.with(animationObject))
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                                        .into(binding.header);

                                if (binding.header.getVisibility() == View.GONE){
                                    binding.header.setVisibility(View.VISIBLE);
                                }

                            }
                    }
                });

            }

            @Override
            public int getItemCount() {
                return headerImages.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                final ImageView footer;

                public ViewHolder(@NonNull @NotNull View itemView) {
                    super(itemView);
                    footer = itemView.findViewById(R.id.footer);
                }
            }
        });


        bottomSheetDialog.show();
    }

}