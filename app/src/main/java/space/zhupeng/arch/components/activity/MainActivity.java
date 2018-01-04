package space.zhupeng.arch.components.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import space.zhupeng.arch.R;
import space.zhupeng.arch.widget.dialog.BottomSheet;

public class MainActivity extends BaseToolbarActivity {

    @Override
    protected int getLayoutResId() {
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
//      hideRightIcon();
    }

    @Override
    protected void onRightIconClick() {
//        BottomSheet dialog = new SimpleDialog();
//        dialog.show(getSupportFragmentManager());
//        BaseWebActivity.toHere(getActivity(), "http://www.baidu.com/", null, 0);
        startActivity(new Intent(this, TabActivity.class));
    }

    public static class SimpleDialog extends BottomSheet {
        @Override
        protected int getLayoutResId() {
            return R.layout.dialog_simple;
        }

        @Override
        protected void initViews(@Nullable Bundle savedInstanceState) {
            super.initViews(savedInstanceState);

            TextView tvTitle = findView(R.id.tv_title);
            tvTitle.setText("底部标题");
            TextView tvMessage = findView(R.id.tv_message);
            tvMessage.setText("这是信息布冯");
        }
    }
}
