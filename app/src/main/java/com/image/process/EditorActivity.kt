package com.image.process

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.image.process.SharingHelper.Companion.genrateBitMap
import com.rtugeek.android.colorseekbar.ColorSeekBar
import ja.burhanrashid52.photoeditor.*
import kotlinx.android.synthetic.main.activity_editor.*


class EditorActivity : AppCompatActivity(), OnPhotoEditorListener {
    lateinit var mPhotoEditor: PhotoEditor
    lateinit var bagkroundImage: PhotoEditor
    var textShadow = TextShadow(2F, 2F, 2F, Color.parseColor("#000000"))
    private var mBehavior: BottomSheetBehavior<*>? = null
    lateinit var mBottomSheetDialog: BottomSheetDialog
    lateinit var bottom_sheet: View
   // lateinit var fontProvider: FontProvider
    var fonts: List<String> = ArrayList()
    var editorTextColorCode = 0
    var editorTextSize = 30f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)


       /* fontProvider = FontProvider(resources)
       fonts = fontProvider.getFontNames()*/

        bottom_sheet = vbottomsheet
        mBehavior = BottomSheetBehavior.from(bottom_sheet)

        mPhotoEditor = PhotoEditor.Builder(this, sticker_view)
            .setPinchTextScalable(true)
            .setClipSourceImage(true)
            .build()
        bagkroundImage = PhotoEditor.Builder(this, bgSticker)
            .setPinchTextScalable(true)
            .setClipSourceImage(true)
            .build()

        text.setOnClickListener {
            val textEditorDialogFragment = TextEditorDialogFragment.show(this)
            textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode ->
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                styleBuilder.withTextSize(30f)
                styleBuilder.withTextShadow(textShadow)
                mPhotoEditor.addText(inputText, styleBuilder)
                //  mTxtCurrentTool.setText(R.string.label_text);
            }
        }

        save.setOnClickListener(){
            mPhotoEditor.clearHelperBox()
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    getBitmapFromView(sharingView)?.let { it1 ->
                        genrateBitMap(
                            it1,
                            this
                        )
                    }
                },100
            )
        }
        gallery.setOnClickListener{
            ImagePicker.with(this)
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .start()
        }
        mPhotoEditor.setOnPhotoEditorListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data!!.data
            //   profilePic.setImageURI(fileUri);
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                    getContentResolver(),
                    fileUri
                )
                bagkroundImage.addImage(bitmap)

            } catch (e: Exception) {
                //handle exception
            }
        }
    }
    fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
        //Define a bitmap with the same size as the view
        val returnedBitmap = createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        //Get the view's background
        val bgDrawable: Drawable? = view.background
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        //return the bitmap
        return returnedBitmap
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {

        if (text != null && rootView != null) {
            showBottomSheetDialog(text, colorCode, rootView)
        }

    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
    }

    override fun onTouchSourceImage(event: MotionEvent?) {
    }

    private fun showBottomSheetDialog(text: String, Code: Int, rootView: View) {
        val styleBuilder = TextStyleBuilder()
        if (mBehavior!!.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        editorTextColorCode = Code
        val view: View
        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog.setCancelable(false)
        view = layoutInflater.inflate(R.layout.bottomsheet_texteditor, null)
        mBottomSheetDialog.getWindow()?.setDimAmount(0f)
        mBottomSheetDialog.setContentView(view)



        val txtTitle = view.findViewById<EditText>(R.id.editText)
        val rvFont = view.findViewById<RecyclerView>(R.id.rvFont)
        val colorSlider = view.findViewById<ColorSeekBar>(R.id.colorSlider)
        val sizeSlider = view.findViewById<SeekBar>(R.id.seekBar7)
        sizeSlider.progress = 30
        txtTitle.setText(text)
        val liTextSetting = view.findViewById<LinearLayout>(R.id.textSetting)
        val liTextStyle = view.findViewById<LinearLayout>(R.id.textStyle)
        val alignLeft = view.findViewById<ImageView>(R.id.alignLeft)
        val alignRight = view.findViewById<ImageView>(R.id.alignRight)
        val alignCenter = view.findViewById<ImageView>(R.id.alignCenter)
        alignCenter.setOnClickListener {
            styleBuilder.withTextColor(editorTextColorCode)
            val inputText = txtTitle.text.toString()
            styleBuilder.withTextSize(editorTextSize)
            styleBuilder.withTextShadow(textShadow)
            styleBuilder.withGravity(Gravity.CENTER)
            mPhotoEditor.editText(rootView, inputText, styleBuilder)
        }
        alignRight.setOnClickListener {
            styleBuilder.withTextColor(editorTextColorCode)
            val inputText = txtTitle.text.toString()
            styleBuilder.withTextSize(editorTextSize)
            styleBuilder.withTextShadow(textShadow)
            styleBuilder.withGravity(Gravity.RIGHT)
            mPhotoEditor.editText(rootView, inputText, styleBuilder)
        }
        alignLeft.setOnClickListener {
            styleBuilder.withTextColor(editorTextColorCode)
            val inputText = txtTitle.text.toString()
            styleBuilder.withTextSize(editorTextSize)
            styleBuilder.withTextShadow(textShadow)
            styleBuilder.withGravity(Gravity.LEFT)
            mPhotoEditor.editText(rootView, inputText, styleBuilder)
        }
        view.findViewById<View>(R.id.tabSetting).setOnClickListener {
            liTextSetting.visibility = View.VISIBLE
            liTextStyle.visibility = View.GONE
        }
        view.findViewById<View>(R.id.tabStyle).setOnClickListener {
            liTextSetting.visibility = View.GONE
            liTextStyle.visibility = View.VISIBLE
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(txtTitle.windowToken, 0)
        }
        rvFont.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(this@EditorActivity)
                        .inflate(R.layout.font_list_layout, parent, false)
                )
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val viewHolder = holder as ViewHolder
                val fontName: String =
                    fonts.get(position)
                viewHolder.textView.text = fontName
/*
                viewHolder.textView.setTypeface(
                    fontProvider.getTypeface(
                        fontName
                    )
                )
*/
                viewHolder.textView.setOnClickListener {
                    styleBuilder.withTextColor(editorTextColorCode)
                    val inputText = txtTitle.text.toString()
                    styleBuilder.withTextSize(editorTextSize)
                    styleBuilder.withTextShadow(textShadow)
/*
                    styleBuilder.withTextFont(
                        fontProvider.getTypeface(
                            fontName
                        )
                    )
*/
                    mPhotoEditor.editText(rootView, inputText, styleBuilder)
                }
            }

            override fun getItemCount(): Int {
                return fonts.size
            }

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val textView: TextView

                init {
                    textView = itemView.findViewById(R.id.fontname)
                }
            }
        }
        sizeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                styleBuilder.withTextColor(editorTextColorCode)
                val inputText = txtTitle.text.toString()
                editorTextSize = seekBar.progress.toFloat()
                styleBuilder.withTextSize(editorTextSize)
                styleBuilder.withTextShadow(textShadow)
                mPhotoEditor.editText(rootView, inputText, styleBuilder)
            }
        })
        colorSlider.setOnClickListener { Log.d("TAG", "onClick: ") }
        colorSlider.setOnColorChangeListener { progress, color ->
            editorTextColorCode = color
            styleBuilder.withTextColor(editorTextColorCode)
            styleBuilder.withTextShadow(textShadow)
            val inputText = txtTitle.text.toString()
            mPhotoEditor.editText(rootView, inputText, styleBuilder)
        }
        txtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //   updateEditor(editorTextColorCode, txtTitle, rootView);
                styleBuilder.withTextColor(editorTextColorCode)
                styleBuilder.withTextShadow(textShadow)
                val inputText = txtTitle.text.toString()
                mPhotoEditor.editText(rootView, inputText, styleBuilder)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow()
                ?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            mBottomSheetDialog.getWindow()?.setBackgroundDrawable(
                ColorDrawable(
                    Color.TRANSPARENT
                )
            )
        }
        mBottomSheetDialog.show()
        mBottomSheetDialog.setOnDismissListener(DialogInterface.OnDismissListener {
        })
        view.findViewById<View>(R.id.btnCancel).setOnClickListener { mBottomSheetDialog.dismiss() }
    }

}