package space.zhupeng.demo.repository;

import retrofit2.Call;
import retrofit2.Response;
import space.zhupeng.arch.manager.DataManager;
import space.zhupeng.arch.manager.HttpHelper;
import space.zhupeng.arch.mvp.model.Callback;
import space.zhupeng.arch.mvp.model.Repository;
import space.zhupeng.arch.net.response.BaseResp;
import space.zhupeng.demo.MenuService;
import space.zhupeng.demo.vo.TypeSearchVo;

/**
 * Created by zhupeng on 2018/1/15.
 */

public class MenuDetailRepository extends Repository<TypeSearchVo.TypeSearchItem> {

    public void loadData(String id, String appkey, final Callback<TypeSearchVo.TypeSearchItem> callback) {
        if (null == callback) return;

        if (isCachedDataAvailable()) {
            callback.onSuccess(mCachedData);
            return;
        }

        if (mCacheIsDirty) {
            DataManager.getInstance().setDebuggable(true);
            HttpHelper helper = DataManager.getInstance().getHttpHelper();
            MenuService service = helper.createApi(MenuService.class);
            Call<BaseResp<TypeSearchVo.TypeSearchItem>> call = service.getDetailById(id, appkey);
            call.enqueue(new retrofit2.Callback<BaseResp<TypeSearchVo.TypeSearchItem>>() {
                @Override
                public void onResponse(Call<BaseResp<TypeSearchVo.TypeSearchItem>> call, Response<BaseResp<TypeSearchVo.TypeSearchItem>> response) {
                    mCachedData = response.body().result;
                    mCacheIsDirty = false;
                    saveToLocal(mCachedData);
                    callback.onSuccess(mCachedData);
                }

                @Override
                public void onFailure(Call<BaseResp<TypeSearchVo.TypeSearchItem>> call, Throwable t) {
                    callback.onFailure(t);
                }
            });
//            loadFromRemote(new Callback<TypeSearchVo.TypeSearchItem>() {
//                @Override
//                public void onSuccess(TypeSearchVo.TypeSearchItem data) {
//                    mCachedData = data;
//                    mCacheIsDirty = false;
//                    saveToLocal(data);
//                    callback.onSuccess(data);
//                }
//
//                @Override
//                public void onFailure(Throwable throwable) {
//                    callback.onFailure(throwable);
//                }
//            });
        } else {
            loadFromLocal(new Callback<TypeSearchVo.TypeSearchItem>() {
                @Override
                public void onSuccess(TypeSearchVo.TypeSearchItem data) {
                    mCachedData = data;
                    mCacheIsDirty = false;
                    callback.onSuccess(data);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }
    }

    @Override
    protected TypeSearchVo.TypeSearchItem loadFromRemote() {
        return null;
    }

    @Override
    protected void loadFromRemote(Callback callback) {

    }
}
