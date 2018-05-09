package space.zhupeng.arch.manager;

/**
 * 更新管理
 *
 * @author zhupeng
 * @date 2018/2/22
 */

public class UpgradeManager {
    private UpgradeStrategy mStrategy;

    public UpgradeManager(UpgradeStrategy strategy) {
        this.mStrategy = strategy;
    }

    public void install() {
        this.mStrategy.checkAndInstallApk();
    }

    public void stop() {
        this.mStrategy.stop();
    }

    public void start() {
        this.mStrategy.start();
    }
}
