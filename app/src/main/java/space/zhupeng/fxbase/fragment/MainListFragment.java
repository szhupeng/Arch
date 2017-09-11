package space.zhupeng.fxbase.fragment;

import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import space.zhupeng.fxbase.Api;
import space.zhupeng.fxbase.R;
import space.zhupeng.fxbase.adapter.BaseAdapter;
import space.zhupeng.fxbase.vo.Gank;

/**
 * @author zhupeng
 * @date 2017/9/11
 */

public class MainListFragment extends BaseListFragment<Gank.GankData, MainListFragment.Holder> {

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
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_gank;
    }

    @Override
    protected Holder onCreateItemViewHolder(View view, int viewType) {
        return new Holder(view);
    }

    @Override
    protected void onBindItemViewHolder(Holder holder, Gank.GankData vo, int position) {
        holder.tvTitle.setText(vo.who);
        holder.tvDesc.setText(vo.desc);
    }

    public class Holder extends BaseAdapter.BaseViewHolder {

        @Bind(R.id.tv_title)
        TextView tvTitle;

        @Bind(R.id.tv_desc)
        TextView tvDesc;

        public Holder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
