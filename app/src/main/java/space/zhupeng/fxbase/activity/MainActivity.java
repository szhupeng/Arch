package space.zhupeng.fxbase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.Bind;
import space.zhupeng.base.R;
import space.zhupeng.fxbase.utils.MemoryUtils;

public class MainActivity extends BaseToolbarActivity {

    @Bind(R.id.tv_message)
    TextView tvMessage;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        tvMessage.setText(MemoryUtils.formatSize(MemoryUtils.getSDTotalSize()) + "\n" +
                MemoryUtils.formatSize(MemoryUtils.getSDAvailableSize()) + "\n" +
                MemoryUtils.formatSize(MemoryUtils.getSystemTotalSize()) + "\n" +
                MemoryUtils.formatSize(MemoryUtils.getSystemAvailableSize()));
    }
}
