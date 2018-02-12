package space.zhupeng.demo.presenter;

import space.zhupeng.arch.mvp.model.RepoCallback;
import space.zhupeng.arch.mvp.presenter.BasePresenter;
import space.zhupeng.demo.repository.MenuDetailRepository;
import space.zhupeng.demo.view.MenuDetailView;
import space.zhupeng.demo.vo.TypeSearchVo;

/**
 * Created by zhupeng on 2018/1/15.
 */

public class MenuDetailPresenter extends BasePresenter<MenuDetailRepository, MenuDetailView> {

    public MenuDetailPresenter(MenuDetailRepository repository) {
        super(repository);
    }

    public void loadData(String id, String appkey) {
        this.mView.showSimpleProgress();
        try {
            this.mRepository.loadData(id, appkey, new RepoCallback<TypeSearchVo.TypeSearchItem>() {
                @Override
                public void onSuccess(TypeSearchVo.TypeSearchItem data) {
                    checkViewAttached();

                    mView.closeDialog();
                    mView.bindData(data);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    checkViewAttached();

                    mView.closeDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
