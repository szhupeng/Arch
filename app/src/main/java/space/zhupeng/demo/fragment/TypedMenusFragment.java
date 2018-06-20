package space.zhupeng.demo.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import space.zhupeng.arch.fragment.BaseListFragment;
import space.zhupeng.arch.manager.DataManager;
import space.zhupeng.arch.manager.HttpHelper;
import space.zhupeng.arch.net.response.BaseResp;
import space.zhupeng.arch.widget.MultiStyleTextView;
import space.zhupeng.arch.widget.adapter.BaseAdapter;
import space.zhupeng.arch.widget.adapter.BaseViewHolder;
import space.zhupeng.arch.widget.adapter.FlexibleItemDecoration;
import space.zhupeng.demo.Api;
import space.zhupeng.demo.MenuService;
import space.zhupeng.demo.R;
import space.zhupeng.demo.activity.MenuDetailsActivity;
import space.zhupeng.demo.vo.TypeSearchVo;
import space.zhupeng.demo.widget.TagGroup;

/**
 * Created by zhupeng on 2018/1/16.
 */

public class TypedMenusFragment extends BaseListFragment<TypeSearchVo.TypeSearchItem> {

    private String mClassId;

    @Override
    protected RecyclerView.ItemDecoration onCreateDecoration() {
        return new FlexibleItemDecoration(getActivity())
                .withOffset(8)
                .withEdge(true);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        mClassId = getArguments().getString("classId");

        TypedMenuAdapter adapter = new TypedMenuAdapter(getActivity());
        adapter.openLoadAnimation(BaseAdapter.ALPHAIN);
        setAdapter(adapter);

        setLoadMoreEnabled(false);
    }

    @Override
    protected List<TypeSearchVo.TypeSearchItem> toLoadData(int pageIndex) {
        DataManager.getInstance().setDebuggable(true);
        HttpHelper helper = DataManager.getInstance().getHttpHelper();
        MenuService service = helper.createApi(MenuService.class);
        Call<BaseResp<TypeSearchVo>> call = service.getMenusByType(mClassId, pageIndex, getPageSize(), Api.API_KEY);
        Response<BaseResp<TypeSearchVo>> response = HttpHelper.syncCall(call);
        TypeSearchVo vo = response.body().result;
        if (null == vo) {
            return Collections.emptyList();
        }
        return vo.list;
    }

    @Override
    protected int getPageSize() {
        return 10;
    }

    @Override
    public void onItemClick(BaseAdapter adapter, View view, int position) {
        TypeSearchVo.TypeSearchItem item = (TypeSearchVo.TypeSearchItem) (adapter.getItem(position));
        MenuDetailsActivity.toHere(getActivity(), item.id);
    }

    public static class TypedMenuAdapter extends BaseAdapter<TypeSearchVo.TypeSearchItem, BaseViewHolder> {

        public TypedMenuAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutResId(int viewType) {
            return R.layout.item_typed_menu;
        }

        @Override
        protected void convert(BaseViewHolder holder, TypeSearchVo.TypeSearchItem item) {
            holder.setText(R.id.tv_menu_name, item.name);
            holder.setText(R.id.tv_people_num, item.peoplenum);
            MultiStyleTextView tvPrepareTime = holder.findViewById(R.id.tv_prepare_time);
            MultiStyleTextView tvCookingTime = holder.findViewById(R.id.tv_cooking_time);
            tvPrepareTime.clear()
                    .segment("准备时长：", Color.GRAY)
                    .segment(item.preparetime, Color.BLACK)
                    .joint();

            tvCookingTime.clear()
                    .segment("烹饪时长：", Color.GRAY)
                    .segment(item.cookingtime, Color.BLACK)
                    .joint();

            TagGroup tagGroup = holder.findViewById(R.id.tag_group);
            tagGroup.setTags(item.tag.split(","));

            ImageView ivPic = holder.findViewById(R.id.iv_pic);
            Glide.with(context)
                    .load(item.pic)
                    .into(ivPic);

            holder.setHtml(R.id.tv_content, item.content);
        }
    }
}
