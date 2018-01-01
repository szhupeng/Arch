package space.zhupeng.fxbase.manager;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * @author zhupeng
 * @date 2018/1/1
 */

public class RemoteDataLoader<D> extends AsyncTaskLoader<D> {

    public RemoteDataLoader(Context context) {
        super(context);
    }

    @Override
    public D loadInBackground() {
        return null;
    }
}
