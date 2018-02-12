package space.zhupeng.demo.repository;

import retrofit2.Call;
import space.zhupeng.arch.manager.DataManager;
import space.zhupeng.arch.manager.HttpHelper;
import space.zhupeng.arch.mvp.model.RepoCallback;
import space.zhupeng.arch.mvp.model.Repository;
import space.zhupeng.arch.net.response.BaseResp;
import space.zhupeng.demo.MenuService;
import space.zhupeng.demo.vo.TypeSearchVo;

/**
 * Created by zhupeng on 2018/1/15.
 */

public class MenuDetailRepository extends Repository<TypeSearchVo.TypeSearchItem> {

    public void loadData(final String id, final String appkey, final RepoCallback<TypeSearchVo.TypeSearchItem> callback) throws Exception {
        toLoadDataAsync(new CallFactory<BaseResp<TypeSearchVo.TypeSearchItem>>() {
            @Override
            public Call<BaseResp<TypeSearchVo.TypeSearchItem>> create() {
                DataManager.getInstance().setDebuggable(true);
                HttpHelper helper = DataManager.getInstance().getHttpHelper();
                MenuService service = helper.createApi(MenuService.class);
                Call<BaseResp<TypeSearchVo.TypeSearchItem>> call = service.getDetailById(id, appkey);
                return call;
            }
        }, callback);
    }
}
