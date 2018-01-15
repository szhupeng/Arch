package space.zhupeng.demo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import space.zhupeng.arch.net.response.BaseResp;
import space.zhupeng.demo.vo.MenuTypeVo;

/**
 * Created by zhupeng on 2018/1/15.
 */

public interface MenuService {

    @GET(Api.MENU_TYPE)
    Call<BaseResp<List<MenuTypeVo>>> getMenuTypes(@Query(Api.KEY_APP_KEY) String appkey);

    @GET(Api.GET_BY_TYPE)
    Call<BaseResp> getByType();
}
