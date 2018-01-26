package space.zhupeng.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import space.zhupeng.arch.manager.StatusBarTinter;
import space.zhupeng.arch.ui.activity.BaseToolbarActivity;
import space.zhupeng.arch.widget.MultiStyleTextView;
import space.zhupeng.demo.Api;
import space.zhupeng.demo.R;
import space.zhupeng.demo.presenter.MenuDetailPresenter;
import space.zhupeng.demo.repository.MenuDetailRepository;
import space.zhupeng.demo.view.MenuDetailView;
import space.zhupeng.demo.vo.TypeSearchVo;
import space.zhupeng.demo.widget.TagGroup;

/**
 * Created by zhupeng on 2018/1/16.
 */

public class MenuDetailsActivity extends BaseToolbarActivity<MenuDetailRepository, MenuDetailView, MenuDetailPresenter> implements MenuDetailView {

    public static void toHere(Activity activity, String id) {
        Intent intent = new Intent(activity, MenuDetailsActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.iv_pic)
    ImageView ivPic;

    @BindView(R.id.tv_content)
    TextView tvContent;

    @BindView(R.id.tv_prepare_time)
    MultiStyleTextView tvPrepareTime;

    @BindView(R.id.tv_cooking_time)
    MultiStyleTextView tvCookingTime;

    @BindView(R.id.tag_group)
    TagGroup mTagGroup;

    @BindView(R.id.tv_material)
    TextView tvMaterial;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_menu_details;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        StatusBarTinter.setStatusBarColorForCollapsingToolbar(this, mAppBarLayout, mCollapsingToolbarLayout, mToolbar, ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    @Override
    protected void onPresenterLoaded() {
        loadData();
    }

    @Override
    public void loadData() {
        if (mPresenter != null) {
            mPresenter.loadData(getIntent().getStringExtra("id"), Api.API_KEY);
        }
    }

    @Override
    protected MenuDetailPresenter createPresenter() {
        return new MenuDetailPresenter(new MenuDetailRepository());
    }

    @Override
    protected boolean isStatusBarTintEnabled() {
        return false;
    }

    @Override
    public void bindData(TypeSearchVo.TypeSearchItem data) {
        setCenterTitle(data.name);
        setCenterSubtitle(new StringBuilder("（").append(data.peoplenum).append("）"));
        Glide.with(getGenericContext()).load(data.pic).into(ivPic);
        tvContent.setText(Html.fromHtml(data.content));
        tvPrepareTime.clear()
                .segment("准备时长：", Color.GRAY)
                .segment(data.preparetime, Color.BLACK)
                .joint();

        tvCookingTime.clear()
                .segment("烹饪时长：", Color.GRAY)
                .segment(data.cookingtime, Color.BLACK)
                .joint();

        mTagGroup.setTags(data.tag.split(","));

        if (data.material != null) {
            StringBuilder material = new StringBuilder();
            for (TypeSearchVo.TypeSearchItem.Material m : data.material) {
                material.append(m.mname).append(m.amount).append("\n");
            }
            tvMaterial.setText(material);
        }
    }

    public static class ProcessViewHolder extends RecyclerView.ViewHolder {

        public ProcessViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
