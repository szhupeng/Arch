package space.zhupeng.demo.activity;

import java.util.ArrayList;
import java.util.List;

import space.zhupeng.arch.ui.activity.BaseTabActivity;
import space.zhupeng.demo.fragment.MainListFragment;

/**
 * Created by zhupeng on 2018/1/3.
 */

public class TabActivity extends BaseTabActivity {

    @Override
    protected List<Tab> buildUpTabs() {
        List<Tab> tabs = new ArrayList<>();
        tabs.add(new Tab("军事", MainListFragment.class));
        tabs.add(new Tab("娱乐", MainListFragment.class));
        tabs.add(new Tab("社会", MainListFragment.class));
        tabs.add(new Tab("生活", MainListFragment.class));
        tabs.add(new Tab("视频", MainListFragment.class));
        tabs.add(new Tab("热点", MainListFragment.class));
        tabs.add(new Tab("新鲜", MainListFragment.class));
        tabs.add(new Tab("互动", MainListFragment.class));
        return tabs;
    }
}
