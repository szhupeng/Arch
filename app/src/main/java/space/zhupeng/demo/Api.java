package space.zhupeng.demo;

/**
 * @author zhupeng
 * @date 2017/9/10
 */

public interface Api {

    String BASE_URL = "http://api.jisuapi.com/";

    String API_KEY = "bbcbd420c4f932a1";

    String ID = "id";
    String APP_KEY = "appkey";
    String CLASS_ID = "classid";
    String START = "start";
    String NUM = "num";

    //菜谱分类
    //http://api.jisuapi.com/recipe/class?appkey=yourappkey
    String MENU_TYPE = "recipe/class";
    //按分类检索
    //http://api.jisuapi.com/recipe/byclass?classid=2&start=0&num=10&appkey=bbcbd420c4f932a1
    String GET_MENUS_BY_TYPE = "recipe/byclass";
    //根据ID查询详情
    //http://api.jisuapi.com/recipe/detail?id=5&appkey=yourappkey
    String GET_DETAIL_BY_ID = "recipe/detail";
    //菜谱搜索
    //http://api.jisuapi.com/recipe/search?keyword=白菜&num=10&appkey=yourappkey
    String SEARCH_MENU = "";
}
