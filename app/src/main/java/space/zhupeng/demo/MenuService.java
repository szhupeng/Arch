package space.zhupeng.demo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import space.zhupeng.arch.net.response.BaseResp;
import space.zhupeng.demo.vo.MenuTypeVo;
import space.zhupeng.demo.vo.TypeSearchVo;

/**
 * Created by zhupeng on 2018/1/15.
 */

public interface MenuService {

    @GET(Api.MENU_TYPE)
    Call<BaseResp<List<MenuTypeVo>>> getMenuTypes(@Query(Api.APP_KEY) String appKey);

    @GET(Api.GET_MENUS_BY_TYPE)
    Call<BaseResp<TypeSearchVo>> getMenusByType(@Query(Api.CLASS_ID) String classId, @Query(Api.START) int start, @Query(Api.NUM) int num, @Query(Api.APP_KEY) String appKey);

    @GET(Api.GET_DETAIL_BY_ID)
    Call<BaseResp<TypeSearchVo.TypeSearchItem>> getDetailById(@Query(Api.ID) String id, @Query(Api.APP_KEY) String appKey);
}
