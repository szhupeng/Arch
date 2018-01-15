package space.zhupeng.demo.presenter;

import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.demo.repository.MenuTypeRepository;
import space.zhupeng.demo.view.MenuTypeView;

/**
 * Created by zhupeng on 2018/1/15.
 */

public class MenuTypePresenter extends BasePresenter<MenuTypeRepository, MenuTypeView> {

    public MenuTypePresenter(MenuTypeRepository repository) {
        super(repository);
    }
}
