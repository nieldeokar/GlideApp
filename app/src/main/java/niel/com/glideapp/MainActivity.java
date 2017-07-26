package niel.com.glideapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import javax.microedition.khronos.opengles.GL;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName().toString();
    private int count ;
    private long lastModified ;
    private EditText etUrl;
    private ImageView imageView;
    private TextView tvStatus;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastModified = Calendar.getInstance().getTimeInMillis()-100;

        imageView = (ImageView) findViewById(R.id.imageView);

        tvStatus = (TextView) findViewById(R.id.tvStatus);

        etUrl = (EditText) findViewById(R.id.editText);

        imageView.setTag(R.id.imageView,33);

        etUrl.setText("https://cdn-images-1.medium.com/max/1200/1*hcfIq_37pabmAOnw3rhvGA.png");
    }

    private void loadImage(String url) {
        Glide.with(this)
            .load(url)

            /*

            Use this to set the expiry time of 1 day

            .signature(new StringSignature(
                System.currentTimeMillis() / (24 * 60 * 60 * 1000)))
            */

            .signature(new StringSignature(String.valueOf(lastModified)))
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .error(R.color.chart_grey)
            .listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                    boolean isFirstResource) {
                    return false;
                }

                @Override public boolean onResourceReady(GlideDrawable resource, String model,
                    Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                    tvStatus.setText("Count :"+ count +" isFromCache : "+isFromMemoryCache);

                    return false;
                }
            })
            .placeholder(R.color.colorPrimary)
            .into(imageView);





/*
    Use this code to avoid grey bg in the images

    Glide.with(this)
        .load(url)
        .asBitmap()
        .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,100))
        .placeholder(R.color.colorPrimary)
        .error(R.color.chart_grey)
        .format(PREFER_ARGB_8888)
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .animate(android.R.anim.fade_in)
         .signature(new StringSignature(String.valueOf(lastModified)))
         .listener(new RequestListener<String, GlideDrawable>() {
          @Override
          public boolean onException(Exception e, String model, Target<GlideDrawable> target,
              boolean isFirstResource) {
            return false;
          }

          @Override public boolean onResourceReady(GlideDrawable resource, String model,
              Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

            tvStatus.setText("Count :"+ count +" isFromCache : "+isFromMemoryCache);
            return false;
          }
        })
        .into(imageView);*/
    }

  /*
  *   We are keeping track of refresh count in count variable
  *   on 1st call if no image url is specified we are using default img url specified in strings.xml
  *
  *   on 3rd refresh we are updating the lastModified time which in result loads image from server
  *
  * */

    public void reload(View view) {

        String url = etUrl.getText().toString().trim();
        if(count==0 && url.isEmpty()) {
            etUrl.setText(getString(R.string.img_url_default));
            loadImage(getString(R.string.img_url_default));
        }
        if(count==3){
            // reload new image modify update time
            count=0;
            lastModified = Calendar.getInstance().getTimeInMillis()-100;
        }

        if(!url.isEmpty()){
            loadImage(url);
        }

        count++;
    }


     /*
      * get Tag from imageView
      *
      */
    public void getTag(View view){
        Toast.makeText(this,"Tag is : " + view.getTag(R.id.imageView),Toast.LENGTH_SHORT).show();
    }

     /*
      *  save image to external / internal storage using Glide
      *
      */
     public void saveImage(View view){
         String url = etUrl.getText().toString().trim();

         Glide.with(this)
                 .load(url)
                 .asBitmap()
                 .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                 .into(new SimpleTarget<Bitmap>() {
                     @Override
                     public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                         Log.d("Size ", "width :"+resource.getWidth() + " height :"+resource.getHeight());
                         imageView.setImageBitmap(resource);
                         storeImage(resource);
                     }
                 });

     }

    private void storeImage(Bitmap image) {
        mBitmap = image;
        File pictureFile = getOutputMediaFile();
        try {
        if (pictureFile == null) {

            if(checkWriteExternalPermission()) {

                FileOutputStream fos = getBaseContext().openFileOutput("file_name" + ".jps", Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
            return;
        }

            if(checkWriteExternalPermission()) {

                FileOutputStream fos = new FileOutputStream(pictureFile);
                image.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
            Toast.makeText(this, "Image stored in : " + pictureFile, Toast.LENGTH_SHORT).show();

            // image stored now set the url to the image path so we can test it using LOAD IMAGE Button
            etUrl.setText(pictureFile.getPath());
            Log.d(TAG, "img dir: " + pictureFile);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }


    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        if(isSdPresent()) {
            File   mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/Files");


            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }

            File mediaFile;
            Random generator = new Random();
            int n = 1000;
            n = generator.nextInt(n);
            String mImageName = "Image-" + n + ".jpg";

            mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
            return mediaFile;
        }else {
            Toast.makeText(this, "SD Card not present", Toast.LENGTH_SHORT).show();
        }
        return null;

    }


    private boolean checkWriteExternalPermission()
    {
        int res = this.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean isSdPresent() {

        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storeImage(mBitmap);

                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to write your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


   /* User RequestManager Object in Recyclerview
    Download Bitmaps using 
    ref :http://stackoverflow.com/a/27394484/3746306
    User Listner for error logging
    https://github.com/bumptech/glide/wiki/Debugging-and-Error-Handling#requestlistener
    Progress bar while loading imageView
    http://stackoverflow.com/a/35306315/3746306*/

}
