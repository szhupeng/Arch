package space.zhupeng.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import space.zhupeng.arch.fragment.BaseListFragment;
import space.zhupeng.arch.manager.DataManager;
import space.zhupeng.arch.manager.HttpHelper;
import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.arch.net.response.BaseResp;
import space.zhupeng.arch.widget.adapter.BaseAdapter;
import space.zhupeng.arch.widget.adapter.BaseMultiItemAdapter;
import space.zhupeng.arch.widget.adapter.BaseViewHolder;
import space.zhupeng.arch.widget.adapter.FlexibleItemDecoration;
import space.zhupeng.arch.widget.adapter.entity.AbstractExpandableItem;
import space.zhupeng.arch.widget.adapter.entity.MultiItemEntity;
import space.zhupeng.demo.Api;
import space.zhupeng.demo.AppContext;
import space.zhupeng.demo.MenuService;
import space.zhupeng.demo.R;
import space.zhupeng.demo.activity.MenuTypedActivity;
import space.zhupeng.demo.presenter.MenuDetailPresenter;
import space.zhupeng.demo.repository.MenuDetailRepository;
import space.zhupeng.demo.vo.MenuTypeVo;

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

        MenuTypeAdapter adapter = new MenuTypeAdapter(getActivity());
        adapter.openLoadAnimation(BaseAdapter.ALPHAIN);
        setAdapter(adapter);
    }

    @Override
    protected BasePresenter createPresenter() {
        return new MenuDetailPresenter(new MenuDetailRepository());
    }

    @Override
    protected List<MultiItemEntity> toLoadData(int pageIndex) {
        DataManager.getInstance().setDebuggable(true);
        HttpHelper helper = DataManager.getInstance().getHttpHelper();
        MenuService service = helper.createApi(MenuService.class);
        Call<BaseResp<List<MenuTypeVo>>> call = service.getMenuTypes(Api.API_KEY);
        Response<BaseResp<List<MenuTypeVo>>> response = HttpHelper.syncCall(call);
        List<MenuTypeVo> list = response.body().result;
        List<MultiItemEntity> parents = new ArrayList<>();
        for (MenuTypeVo vo : list) {
            MenuTypeParent parent = new MenuTypeParent(vo.classid, vo.name);
            if (vo.list != null) {
                for (MenuTypeVo subVo : vo.list) {
                    parent.addSubItem(new MenuTypeList(subVo.classid, subVo.name, subVo.parentid));
                }
            }
            parents.add(parent);
        }
        return parents;
    }

    @Override
    public void onItemClick(BaseAdapter adapter, View view, int position) {
        if (MenuTypeAdapter.TYPE_MENU_LIST == adapter.getItemViewType(position)) {
            MenuTypeList child = (MenuTypeList) adapter.getItem(position);
            MenuTypeVo vo = new MenuTypeVo();
            vo.classid = child.classid;
            vo.name = child.name;
            MenuTypedActivity.toHere(getActivity(), vo);
        }
    }

    public static class MenuTypeAdapter extends BaseMultiItemAdapter<MultiItemEntity, BaseViewHolder> {

        public static final int TYPE_MENU_PARENT = 0;
        public static final int TYPE_MENU_LIST = 1;

        public MenuTypeAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutResId(int viewType) {
            if (TYPE_MENU_PARENT == viewType) {
                return R.layout.layout_header;
            } else if (TYPE_MENU_LIST == viewType) {
                return R.layout.layout_sub;
            }
            return 0;
        }

        @Override
        protected void convert(final BaseViewHolder holder, MultiItemEntity item) {
            if (TYPE_MENU_PARENT == holder.getItemViewType()) {
                final MenuTypeParent typeParent = (MenuTypeParent) item;
                holder.setText(R.id.tv_head, typeParent.name);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getAdapterPosition();
                        if (typeParent.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
            } else if (TYPE_MENU_LIST == holder.getItemViewType()) {
                final MenuTypeList typeList = (MenuTypeList) item;
                holder.setText(R.id.tv_subhead, typeList.name);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getAdapterPosition();
                        remove(pos);
                        return true;
                    }
                });
            }
        }
    }

    public static class MenuTypeParent extends AbstractExpandableItem<MenuTypeList> implements MultiItemEntity {
        public String classid;
        public String name;

        public MenuTypeParent(String classid, String name) {
            this.classid = classid;
            this.name = name;
        }

        @Override
        public int getItemType() {
            return MenuTypeAdapter.TYPE_MENU_PARENT;
        }

        @Override
        public int getLevel() {
            return 0;
        }
    }

    public static class MenuTypeList implements MultiItemEntity {
        public String classid;
        public String name;
        public String parentid;

        public MenuTypeList(String classid, String name, String parentid) {
            this.classid = classid;
            this.name = name;
            this.parentid = parentid;
        }

        @Override
        public int getItemType() {
            return MenuTypeAdapter.TYPE_MENU_LIST;
        }
    }

}
