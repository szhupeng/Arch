package space.zhupeng.fxbase.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import space.zhupeng.fxbase.Api;
import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.vo.Gank;
import space.zhupeng.fxbase.widget.adapter.BaseAdapter;
import space.zhupeng.fxbase.widget.adapter.BaseViewHolder;
import space.zhupeng.fxbase.widget.adapter.FlexibleItemDecoration;

/**
 * @author zhupeng
 * @date 2017/9/11
 */

public class MainListFragment extends BaseListFragment<Gank.GankData> {

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

    @Override
    protected List<Gank.GankData> toLoadData(int pageIndex) {
        Request request = new Request.Builder().url(Api.GANK).build();
        Call call = new OkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                Gson gson = new Gson();
                Gank data = gson.fromJson(json, new TypeToken<Gank>() {
                }.getType());
                return data.results;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
    }

    public static class GankAdapter extends BaseAdapter<Gank.GankData, BaseViewHolder> {

        public GankAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutResID(int viewType) {
            return R.layout.item_gank;
        }

        @Override
        protected void convert(BaseViewHolder holder, Gank.GankData item) {
            holder.setText(R.id.tv_desc, item.desc);
            holder.setText(R.id.tv_author, new StringBuilder("作者：").append(item.who));
            holder.setText(R.id.tv_create_time, new StringBuilder("发布时间：").append(item.publishedAt.substring(0, 10)));
        }
    }
}
