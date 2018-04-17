package space.zhupeng.arch.route;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;

/**
 * 利用空的Fragment接收结果并回调
 *
 * @author zhupeng
 * @date 2016/12/11
 */
public class SupportActivityResultFragment extends Fragment implements ResultFragmentCompat {

    public static final String TAG = "SupportActivityResultFragment";

    private final SparseArrayCompat<Router.Callback> mCallbacks;

    public SupportActivityResultFragment() {
        mCallbacks = new SparseArrayCompat();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void startActivityForResult(Intent intent, Router.Callback callback) {
        int requestCode = Math.abs(callback.hashCode() & 0xFFFF);
        requestCode = checkRequestCode(requestCode);
        mCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    private int checkRequestCode(int requestCode) {
        if (mCallbacks.indexOfKey(requestCode) <= 0) {
            return requestCode;
        }
        return checkRequestCode((requestCode + mCallbacks.size()) & 0xFFFF);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Router.Callback callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);

        if (callback != null) {
            callback.onActivityResult(resultCode, data);
        }
    }
}
