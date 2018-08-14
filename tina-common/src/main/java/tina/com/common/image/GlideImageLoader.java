package tina.com.common.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import tina.com.common.R;

public class GlideImageLoader {

    public static <T> void loadRoundCenterCropImage(Context context, ImageView imageView, T imagePath, int radius) {
        if (context == null || imagePath == null) return;
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.bg_live_item)
                .transform(new RoundCenterCropTransformation(radius))
                .override(imageView.getWidth(), imageView.getHeight())
                .error(R.drawable.bg_live_item)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(imagePath)
                .apply(options)
                .into(imageView);
    }





}
