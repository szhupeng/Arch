package space.zhupeng.arch.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * 路由
 *
 * @author zhupeng
 * @date 2016/12/11
 */
public class Router {

    public interface Callback {
        void onActivityResult(int resultCode, Intent data);
    }

    private final ResultFragmentCompat mResultFragment;

    public Router() {
        mResultFragment = null;
    }

    public Router(android.app.Fragment fragment) {
        this(fragment.getActivity());
    }

    public Router(Fragment fragment) {
        this(fragment.getActivity());
    }

    public Router(Activity activity) {
        if (activity instanceof FragmentActivity) {
            mResultFragment = getResultFragment((FragmentActivity) activity);
        } else {
            mResultFragment = getResultFragment(activity);
        }
    }

    private SupportActivityResultFragment getResultFragment(FragmentActivity activity) {
        final FragmentManager manager = activity.getSupportFragmentManager();

        Fragment fragment = manager.findFragmentByTag(SupportActivityResultFragment.TAG);
        if (null == fragment) {
            fragment = new SupportActivityResultFragment();
            manager.beginTransaction()
                    .add(fragment, SupportActivityResultFragment.TAG)
                    .commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
        return (SupportActivityResultFragment) fragment;
    }

    private ActivityResultFragment getResultFragment(Activity activity) {
        final android.app.FragmentManager manager = activity.getFragmentManager();

        android.app.Fragment fragment = manager.findFragmentByTag(ActivityResultFragment.TAG);
        if (null == fragment) {
            fragment = new ActivityResultFragment();
            manager.beginTransaction()
                    .add(fragment, SupportActivityResultFragment.TAG)
                    .commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
        return (ActivityResultFragment) fragment;
    }

    public void startActivityForResult(Intent intent, @NonNull Router.Callback callback) {
        if (null == mResultFragment) {
            throw new RuntimeException("You must call a parameterized constructor instead of a parameterless constructor");
        }
        mResultFragment.startActivityForResult(intent, callback);
    }

    public void startActivity(Context context, @NonNull String action) {
        Intent intent = new Intent(action);
        startActivity(context, intent, null);
    }

    public void startActivity(Context context, @NonNull String action, @NonNull Bundle extras) {
        Intent intent = new Intent(action);
        startActivity(context, intent, extras);
    }

    public void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(context, intent, null);
    }

    public void startActivity(Context context, Class<?> cls, @NonNull Bundle extras) {
        Intent intent = new Intent(context, cls);
        startActivity(context, intent, extras);
    }

    private void startActivity(Context context, Intent intent, Bundle extras) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }
}
