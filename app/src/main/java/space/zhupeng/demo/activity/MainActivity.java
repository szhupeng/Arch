package space.zhupeng.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import space.zhupeng.arch.activity.BaseToolbarActivity;
import space.zhupeng.arch.widget.dialog.UpgradeDialog;
import space.zhupeng.demo.R;
import space.zhupeng.demo.fragment.MainListFragment;

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

        setCenterTitle("菜谱大全");
        hideLeft();

        replaceFragment(MainListFragment.class, null);

        UpgradeDialog.Builder builder = new UpgradeDialog.Builder(this);
        builder.setUpgradeTitle("有更新啦")
                .setUpgradeLog("修复了一些bug")
                .setAppPath("xxx.apk")
                .setDownloadInBack(false)
                .setForceUpgrade(false)
                .setDownloadUrl("http://onkibgj8e.bkt.clouddn.com/app-tzht-release.apk")
                .show(getSupportFragmentManager());
    }
}
