package space.zhupeng.arch.components.fragment;

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
import space.zhupeng.arch.Api;
import space.zhupeng.arch.R;
import space.zhupeng.arch.vo.GankVo;
import space.zhupeng.arch.widget.adapter.BaseAdapter;
import space.zhupeng.arch.widget.adapter.BaseMultiItemAdapter;
import space.zhupeng.arch.widget.adapter.BaseViewHolder;
import space.zhupeng.arch.widget.adapter.FlexibleItemDecoration;
import space.zhupeng.arch.widget.adapter.entity.AbstractExpandableItem;
import space.zhupeng.arch.widget.adapter.entity.MultiItemEntity;

/**
 * @author zhupeng
 * @date 2017/9/11
 */

public class MainListFragment extends BaseListFragment<MultiItemEntity> {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        bindSavedData(savedInstanceState, LevelCategory.class);
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


//    @Override
//    protected List<SectionEntity<Gank>> toLoadData(int pageIndex) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2015, 7, 6);
//        calendar.add(Calendar.DAY_OF_MONTH, pageIndex - 1);
//        String url = new StringBuilder(Api.GANK2)
//                .append(calendar.get(Calendar.YEAR))
//                .append("/")
//                .append(calendar.get(Calendar.MONTH) + 1)
//                .append("/")
//                .append(calendar.get(Calendar.DAY_OF_MONTH))
//                .toString();
//        Request request = new Request.Builder().url(url).build();
//        Call call = new OkHttpClient().newCall(request);
//        try {
//            Response response = call.execute();
//            if (response.isSuccessful()) {
//                String json = response.body().string();
//                Gson gson = new Gson();
//                GankVo<JsonObject> data = gson.fromJson(json, new TypeToken<GankVo<JsonObject>>() {
//                }.getType());
//                List<SectionEntity<Gank>> result = new ArrayList<>();
//                for (String key : data.category) {
//                    result.add(new SectionEntity<Gank>(true, key));
//                    JsonArray array = data.results.getAsJsonArray(key);
//                    Iterator iterator = array.iterator();
//                    while (iterator.hasNext()) {
//                        JsonElement je = (JsonElement) iterator.next();
//                        Gank gank = gson.fromJson(je, Gank.class);
//                        result.add(new SectionEntity<Gank>(gank));
//                    }
//                }
//                return result;
//            } else {
//                throw new IOException("Unexpected code " + response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    protected List<MultiItemEntity> toLoadData(int pageIndex) {
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
                List<MultiItemEntity> result = new ArrayList<>();
                for (String key : data.category) {
                    LevelCategory category = new LevelCategory(key);
                    JsonArray array = data.results.getAsJsonArray(key);
                    Iterator iterator = array.iterator();
                    while (iterator.hasNext()) {
                        JsonElement je = (JsonElement) iterator.next();
                        LevelDetail detail = gson.fromJson(je, LevelDetail.class);
                        LevelDesc desc = new LevelDesc(detail.desc);
                        desc.addSubItem(detail);
                        category.addSubItem(desc);
                    }
                    result.add(category);
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

//    public static class GankAdapter extends BaseSectionAdapter<SectionEntity<Gank>, BaseViewHolder> {
//
//        public GankAdapter(Context context) {
//            super(context);
//        }
//
//        @Override
//        protected void convertHeader(BaseViewHolder holder, SectionEntity<Gank> item) {
//            holder.setText(R.id.tv_head, item.header);
//        }
//
//        @Override
//        protected int getSectionHeadLayoutResID() {
//            return R.layout.layout_header;
//        }
//
//        @Override
//        protected int getItemLayoutResID(int viewType) {
//            return R.layout.item_gank;
//        }
//
//        @Override
//        protected void convert(BaseViewHolder holder, SectionEntity<Gank> item) {
//            holder.setText(R.id.tv_desc, item.data.desc);
//            holder.setText(R.id.tv_author, new StringBuilder("作者：").append(item.data.who));
//            holder.setText(R.id.tv_create_time, new StringBuilder("发布时间：").append(item.data.publishedAt.substring(0, 10)));
//        }
//    }

    public static class GankAdapter extends BaseMultiItemAdapter<MultiItemEntity, BaseViewHolder> {

        public static final int TYPE_CATEGORY_LEVEL = 0;
        public static final int TYPE_DESC_LEVEL = 1;
        public static final int TYPE_DETAIL_LEVEL = 2;

        public GankAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutResId(int viewType) {
            if (TYPE_CATEGORY_LEVEL == viewType) {
                return R.layout.layout_header;
            } else if (TYPE_DESC_LEVEL == viewType) {
                return R.layout.layout_sub;
            } else if (TYPE_DETAIL_LEVEL == viewType) {
                return R.layout.item_gank;
            }
            return 0;
        }

        @Override
        protected void convert(final BaseViewHolder holder, MultiItemEntity item) {
            if (TYPE_CATEGORY_LEVEL == holder.getItemViewType()) {
                final LevelCategory category = (LevelCategory) item;
                holder.setText(R.id.tv_head, category.title);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getAdapterPosition();
                        if (category.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
            } else if (TYPE_DESC_LEVEL == holder.getItemViewType()) {
                final LevelDesc desc = (LevelDesc) item;
                holder.setText(R.id.tv_subhead, desc.title);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (desc.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos, false);
                        }
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getAdapterPosition();
                        remove(pos);
                        return true;
                    }
                });
            } else {
                final LevelDetail detail = (LevelDetail) item;
                holder.setText(R.id.tv_desc, detail.desc);
                holder.setText(R.id.tv_author, new StringBuilder("作者：").append(detail.who));
                holder.setText(R.id.tv_create_time, new StringBuilder("发布时间：").append(detail.publishedAt.substring(0, 10)));
            }
        }
    }

    public static class LevelCategory extends AbstractExpandableItem<LevelDesc> implements MultiItemEntity {
        public String title;

        public LevelCategory(String title) {
            this.title = title;
        }

        @Override
        public int getItemType() {
            return GankAdapter.TYPE_CATEGORY_LEVEL;
        }

        @Override
        public int getLevel() {
            return 0;
        }
    }

    public static class LevelDesc extends AbstractExpandableItem<LevelDetail> implements MultiItemEntity {
        public String title;

        public LevelDesc(String title) {
            this.title = title;
        }

        @Override
        public int getItemType() {
            return GankAdapter.TYPE_DESC_LEVEL;
        }

        @Override
        public int getLevel() {
            return 1;
        }
    }

    public static class LevelDetail implements MultiItemEntity {
        public String _id;
        public String createdAt;
        public String desc;
        public String publishedAt;
        public String source;
        public String type;
        public String url;
        public boolean used;
        public String who;

        @Override
        public int getItemType() {
            return GankAdapter.TYPE_DETAIL_LEVEL;
        }
    }
}
