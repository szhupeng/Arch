package space.zhupeng.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import space.zhupeng.arch.activity.BaseToolbarActivity;
import space.zhupeng.demo.AppContext;
import space.zhupeng.demo.R;
import space.zhupeng.demo.fragment.TypedMenusFragment;
import space.zhupeng.demo.vo.MenuTypeVo;

/**
 * Created by zhupeng on 2018/1/15.
 */

public class MenuTypedActivity extends BaseToolbarActivity {

    public static void toHere(Activity activity, MenuTypeVo vo) {
        Intent intent = new Intent(activity, MenuTypedActivity.class);
        intent.putExtra("vo", vo);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_menu_typed;
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setCenterTitle("分类菜谱");

        MenuTypeVo vo = getIntent().getParcelableExtra("vo");
        replaceFragment(TypedMenusFragment.class, vo.classid);
    }
}
