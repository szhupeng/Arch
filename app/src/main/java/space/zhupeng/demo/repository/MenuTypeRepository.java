package space.zhupeng.demo.repository;

import space.zhupeng.arch.mvp.model.Callback;
import space.zhupeng.arch.mvp.model.Repository;
import space.zhupeng.demo.vo.MenuTypeVo;

/**
 * Created by zhupeng on 2018/1/15.
 */

public class MenuTypeRepository extends Repository<MenuTypeVo> {

    @Override
    protected MenuTypeVo loadFromRemote() {
        return null;
    }

    @Override
    protected void loadFromRemote(Callback callback) {

    }
}
