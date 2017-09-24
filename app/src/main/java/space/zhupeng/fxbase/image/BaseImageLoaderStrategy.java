package space.zhupeng.fxbase.image;

import android.content.Context;

/**
 * @author zhupeng
 * @date 2017/9/16
 */

public interface BaseImageLoaderStrategy<T extends ImageConfig> {
    void loadImage(Context context, T config);

    void clear(Context context, T config);
}
