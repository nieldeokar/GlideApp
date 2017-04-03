package niel.com.glideapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import java.util.Calendar;
import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity {

    private int count ;
    private long lastModified ;
    private EditText etUrl;
    private ImageView imageView;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastModified = Calendar.getInstance().getTimeInMillis()-100;

        imageView = (ImageView) findViewById(R.id.imageView);

        tvStatus = (TextView) findViewById(R.id.tvStatus);

        etUrl = (EditText) findViewById(R.id.editText);

        imageView.setTag(R.id.imageView,33);
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

}
