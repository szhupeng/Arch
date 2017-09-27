package space.zhupeng.fxbase.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.widget.TouchableOpacity;

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

        TouchableOpacity to = new TouchableOpacity() {
            @Override
            protected float activeOpacity() {
                return 0.3f;
            }
        };

        TextView text = (TextView) findViewById(R.id.text);
        ImageView image = (ImageView) findViewById(R.id.image);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("哈哈");
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("嘻嘻");
            }
        });

        to.wrap(image);
    }
}
