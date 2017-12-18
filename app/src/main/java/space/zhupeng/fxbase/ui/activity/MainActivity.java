package space.zhupeng.fxbase.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.widget.dialog.SimpleDialog;

public class MainActivity extends BaseToolbarActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setCenterTitle("干货集中营");
        hideLeft();
        hideRightText();
//        hideRightIcon();
    }

    @Override
    protected void onRightIconClick() {
        SimpleDialog dialog = new SimpleDialog(this);
        dialog.show();
    }
}
