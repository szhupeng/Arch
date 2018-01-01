package space.zhupeng.fxbase.manager;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * @author zhupeng
 * @date 2018/1/1
 */

public class LocalDataLoader<D> extends AsyncTaskLoader<D> {

    public LocalDataLoader(Context context) {
        super(context);
    }

    @Override
    public D loadInBackground() {
        return null;
    }
}
