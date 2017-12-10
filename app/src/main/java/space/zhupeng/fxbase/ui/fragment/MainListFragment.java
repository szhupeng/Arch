package space.zhupeng.fxbase.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import space.zhupeng.fxbase.Api;
import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.mvp.presenter.BasePresenter;
import space.zhupeng.fxbase.vo.Gank;
import space.zhupeng.fxbase.vo.GankVo;
import space.zhupeng.fxbase.widget.adapter.BaseAdapter;
import space.zhupeng.fxbase.widget.adapter.BaseSectionAdapter;
import space.zhupeng.fxbase.widget.adapter.BaseViewHolder;
import space.zhupeng.fxbase.widget.adapter.FlexibleItemDecoration;
import space.zhupeng.fxbase.widget.adapter.entity.SectionEntity;

/**
 * @author zhupeng
 * @date 2017/9/11
 */

public class MainListFragment extends BaseListFragment<SectionEntity<Gank>> {

    @Override
    protected RecyclerView.ItemDecoration onCreateDecoration() {
        return new FlexibleItemDecoration(getActivity())
                .withOffset(8) // This helps when top items are removed!!
                .withEdge(true);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        GankAdapter adapter = new GankAdapter(getActivity());
        adapter.openLoadAnimation(BaseAdapter.ALPHAIN);
        setAdapter(adapter);
    }


//    @Override
//    protected List<GankVo.GankData> toLoadData(int pageIndex) {
//        Request request = new Request.Builder().url(Api.GANK2).build();
//        Call call = new OkHttpClient().newCall(request);
//        try {
//            Response response = call.execute();
//            if (response.isSuccessful()) {
//                String json = response.body().string();
//                Gson gson = new Gson();
//                GankVo data = gson.fromJson(json, new TypeToken<GankVo>() {
//                }.getType());
//                return Collections.emptyList();
//            } else {
//                throw new IOException("Unexpected code " + response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    @Override
    protected BasePresenter createPresenter() {
        return super.createPresenter();
    }

    @Override
    protected List<SectionEntity<Gank>> toLoadData(int pageIndex) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 7, 6);
        calendar.add(Calendar.DAY_OF_MONTH, pageIndex - 1);
        String url = new StringBuilder(Api.GANK2)
                .append(calendar.get(Calendar.YEAR))
                .append("/")
                .append(calendar.get(Calendar.MONTH) + 1)
                .append("/")
                .append(calendar.get(Calendar.DAY_OF_MONTH))
                .toString();
        Request request = new Request.Builder().url(url).build();
        Call call = new OkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                Gson gson = new Gson();
                GankVo<JsonObject> data = gson.fromJson(json, new TypeToken<GankVo<JsonObject>>() {
                }.getType());
                List<SectionEntity<Gank>> result = new ArrayList<>();
                for (String key : data.category) {
                    result.add(new SectionEntity<Gank>(true, key));
                    JsonArray array = data.results.getAsJsonArray(key);
                    Iterator iterator = array.iterator();
                    while (iterator.hasNext()) {
                        JsonElement je = (JsonElement) iterator.next();
                        Gank gank = gson.fromJson(je, Gank.class);
                        result.add(new SectionEntity<Gank>(gank));
                    }
                }
                return result;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static class GankAdapter extends BaseAdapter<Gank.GankData, BaseViewHolder> {
//
//        public GankAdapter(Context context) {
//            super(context);
//        }
//
//        @Override
//        protected int getItemLayoutResID(int viewType) {
//            return R.layout.item_gank;
//        }
//
//        @Override
//        protected void convert(BaseViewHolder holder, Gank.GankData item) {
//            holder.setText(R.id.tv_desc, item.desc);
//            holder.setText(R.id.tv_author, new StringBuilder("作者：").append(item.who));
//            holder.setText(R.id.tv_create_time, new StringBuilder("发布时间：").append(item.publishedAt.substring(0, 10)));
//        }
//    }

    public static class GankAdapter extends BaseSectionAdapter<SectionEntity<Gank>, BaseViewHolder> {

        public GankAdapter(Context context) {
            super(context);
        }

        @Override
        protected void convertHeader(BaseViewHolder holder, SectionEntity<Gank> item) {
            holder.setText(R.id.tv_head, item.header);
        }

        @Override
        protected int getSectionHeadLayoutResID() {
            return R.layout.layout_header;
        }

        @Override
        protected int getItemLayoutResID(int viewType) {
            return R.layout.item_gank;
        }

        @Override
        protected void convert(BaseViewHolder holder, SectionEntity<Gank> item) {
            holder.setText(R.id.tv_desc, item.data.desc);
            holder.setText(R.id.tv_author, new StringBuilder("作者：").append(item.data.who));
            holder.setText(R.id.tv_create_time, new StringBuilder("发布时间：").append(item.data.publishedAt.substring(0, 10)));
        }
    }
}
