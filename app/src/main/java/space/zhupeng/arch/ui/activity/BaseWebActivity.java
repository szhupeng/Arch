package space.zhupeng.arch.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import space.zhupeng.arch.R;
import space.zhupeng.arch.ui.fragment.BaseWebFragment;

import static space.zhupeng.arch.ui.fragment.BaseWebFragment.EXTRA_JS_FILE;
import static space.zhupeng.arch.ui.fragment.BaseWebFragment.EXTRA_URL;

/**
 * Created by zhupeng on 2018/1/5.
 */

public class BaseWebActivity extends BaseActivity {

    public static void toHere(@NonNull Activity activity, @NonNull String url, String jsFile, int requestCode) {
        Intent intent = new Intent(activity, BaseWebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        if (!TextUtils.isEmpty(jsFile)) {
            intent.putExtra(EXTRA_JS_FILE, jsFile);
        }

        if (requestCode > 0) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_base_web;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        Intent intent = getIntent();
        if (null == intent) return;

        pushFragment(BaseWebFragment.newInstance(intent.getStringExtra(EXTRA_URL), intent.getStringExtra(EXTRA_JS_FILE)));
    }
}
