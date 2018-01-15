package space.zhupeng.demo.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupeng
 * @date 2018/1/15
 */

public class MenuTypeVo implements Parcelable {

    public String classid;
    public String name;
    public String parentid;
    public List<MenuTypeVo> list = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.classid);
        dest.writeString(this.name);
        dest.writeString(this.parentid);
        dest.writeTypedList(this.list);
    }

    public MenuTypeVo() {
    }

    protected MenuTypeVo(Parcel in) {
        this.classid = in.readString();
        this.name = in.readString();
        this.parentid = in.readString();
        this.list = in.createTypedArrayList(MenuTypeVo.CREATOR);
    }

    public static final Creator<MenuTypeVo> CREATOR = new Creator<MenuTypeVo>() {
        @Override
        public MenuTypeVo createFromParcel(Parcel source) {
            return new MenuTypeVo(source);
        }

        @Override
        public MenuTypeVo[] newArray(int size) {
            return new MenuTypeVo[size];
        }
    };
}
