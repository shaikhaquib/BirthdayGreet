package com.image.process

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class SharingHelper {
    companion object{
        fun genrateBitMap(bitmap: Bitmap, context: Activity) {
                Dexter.withContext(context)
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                saveImageToStorage(
                                    context,
                                    bitmap,
                                    System.currentTimeMillis().toString() + ".jpg"
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "This functionality requires storage permission",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                            p1: PermissionToken?
                        ) {
                            /*TODO("Not yet implemented")*/
                        }
                    }).check()


        }
        fun createBitmapFromView(ctx: Context, v: View): Bitmap {

            val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(0, 0, v.width, v.height)
            v.draw(c)
            return b
        }

        fun saveImageToStorage(
            context: Activity,
            bitmap: Bitmap,
            filename: String = "screenshot.jpg",
            mimeType: String = "image/jpeg",
            directory: String = Environment.DIRECTORY_PICTURES,
            mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ) {
            val imageOutStream: OutputStream
            var fileUri:Uri

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                    put(MediaStore.Images.Media.RELATIVE_PATH, directory)
                }

                context.contentResolver.run {
                    val uri =
                        context.contentResolver.insert(mediaContentUri, values)
                            ?: return
                    imageOutStream = openOutputStream(uri) ?: return

                    fileUri = uri
                    share(uri,context)
                    imageOutStream.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }

                }
            } else {
              /*  val imagePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
                val image = File(imagePath, filename)
                imageOutStream = FileOutputStream(image)
                fileUri = Uri.fromFile(image)*/
              //  share(Uri.fromFile(image),context)
                val currentTimestamp = System.currentTimeMillis()
                val file_path = Environment.getExternalStorageDirectory().absolutePath +
                        "/miPudhari"
                val dir = File(file_path)
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, "miPudhari" + currentTimestamp + ".jpg")
                val fOut = FileOutputStream(file)

                bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
                fOut.flush()
                fOut.close()
                val imageUri = FileProvider.getUriForFile(
                    context,
                    "com.keights.mipudhari.provider",  //(use your app signature + ".provider" )
                    file
                )
                share(imageUri,context)

            }



        }
        public fun share(bmpUri: Uri?, context: Activity) {
            val share = Intent(Intent.ACTION_SEND)
            var mimeType = "image/*"
            share.type = mimeType
            share.putExtra(Intent.EXTRA_STREAM, bmpUri)
            share.putExtra(Intent.EXTRA_TEXT, "Created by MiPudhari")
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val chooser = Intent.createChooser(share, "Share Meme")
            context.startActivity(chooser)
        }


    }

}