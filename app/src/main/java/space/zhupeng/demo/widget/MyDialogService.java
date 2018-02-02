package space.zhupeng.demo.widget;

import android.content.Context;
import android.widget.Toast;

import space.zhupeng.arch.widget.dialog.DialogService;
import space.zhupeng.compiler.AsDialogService;

/**
 * Created by zhupeng on 2018/2/2.
 */
@AsDialogService
public class MyDialogService implements DialogService {

    @Override
    public void showProgressDialog(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog(Context context) {
        Toast.makeText(context, "简单的加载进度框", Toast.LENGTH_LONG).show();
    }

    @Override
    public void closeDialog() {

    }
}
