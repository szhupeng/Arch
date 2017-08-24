package space.zhupeng.base.anim;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Fade;
import android.view.View;

import java.util.ArrayList;

/**
 * fragment转场动画实体
 *
 * @author zhupeng
 * @date 2017/8/16
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public final class Transition {

    public Object sharedElementEnter;
    public Object exit = new Fade();
    public Object enter = new Fade();
    public Object sharedElementReturn;
    public ArrayList<SharedElement> sharedElements;

    public static class SharedElement {
        public View sharedElement;
        public String name;

        public SharedElement(View sharedElement, String name) {
            this.sharedElement = sharedElement;
            this.name = name;
        }
    }
}
