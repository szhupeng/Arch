package space.zhupeng.fxbase.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * 屏蔽底层图像加载库
 *
 * @author zhupeng
 * @date 2017/5/30
 */

public interface ImageLoader {

    void displayImage(Context context,
                      String path,
                      ImageView imageView,
                      Drawable defaultDrawable,
                      Bitmap.Config config,
                      boolean resize,
                      boolean isGif,
                      int width,
                      int height,
                      int rotate);
}
