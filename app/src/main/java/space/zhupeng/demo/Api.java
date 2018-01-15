package space.zhupeng.demo;

/**
 * @author zhupeng
 * @date 2017/9/10
 */

public interface Api {

    String BASE_URL = "http://api.jisuapi.com/";

    String APP_KEY = "bbcbd420c4f932a1";

    String KEY_APP_KEY = "appkey";

    //菜谱分类
    //http://api.jisuapi.com/recipe/class?appkey=yourappkey
    String MENU_TYPE = "recipe/class";
    //按分类检索
    //http://api.jisuapi.com/recipe/byclass?classid=2&start=0&num=10&appkey=bbcbd420c4f932a1
    String GET_BY_TYPE = "recipe/byclass";
    //根据ID查询详情
    //http://api.jisuapi.com/recipe/detail?id=5&appkey=yourappkey
    String GET_BY_ID = "recipe/detail";
    //菜谱搜索
    //http://api.jisuapi.com/recipe/search?keyword=白菜&num=10&appkey=yourappkey
    String SEARCH_MENU = "";
}
