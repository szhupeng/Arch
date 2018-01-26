package space.zhupeng.demo.view;

import space.zhupeng.arch.mvp.view.BaseView;
import space.zhupeng.demo.vo.TypeSearchVo;

/**
 * Created by zhupeng on 2018/1/15.
 */

public interface MenuDetailView extends BaseView {

    void loadData();

    void bindData(TypeSearchVo.TypeSearchItem data);
}
