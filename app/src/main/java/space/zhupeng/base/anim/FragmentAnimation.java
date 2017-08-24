package space.zhupeng.base.anim;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AnimRes;

/**
 * fragment切换动画实体
 *
 * @author zhupeng
 * @date 2017/8/16
 */
public class FragmentAnimation implements Parcelable {
    @AnimRes
    public int enter;
    @AnimRes
    public int exit;
    @AnimRes
    public int popEnter;
    @AnimRes
    public int popExit;

    public FragmentAnimation() {
    }

    public FragmentAnimation(int enter, int exit) {
        this.enter = enter;
        this.exit = exit;
    }

    public FragmentAnimation(int enter, int exit, int popEnter, int popExit) {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
    }

    protected FragmentAnimation(Parcel in) {
        enter = in.readInt();
        exit = in.readInt();
        popEnter = in.readInt();
        popExit = in.readInt();
    }

    public static final Creator<FragmentAnimation> CREATOR = new Creator<FragmentAnimation>() {
        @Override
        public FragmentAnimation createFromParcel(Parcel in) {
            return new FragmentAnimation(in);
        }

        @Override
        public FragmentAnimation[] newArray(int size) {
            return new FragmentAnimation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(enter);
        dest.writeInt(exit);
        dest.writeInt(popEnter);
        dest.writeInt(popExit);
    }
}
