package space.zhupeng.demo.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhupeng on 2018/1/16.
 */

public class TypeSearchVo implements Parcelable {
    public String num;
    public List<TypeSearchItem> list;

    public static class TypeSearchItem implements Parcelable {
        public String id;
        public String classid;
        public String name;
        public String peoplenum;
        public String preparetime;
        public String cookingtime;
        public String content;
        public String pic;
        public String tag;
        public List<Material> material;
        public List<Process> process;

        public static class Material implements Parcelable {
            public String mname;
            public String type;
            public String amount;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.mname);
                dest.writeString(this.type);
                dest.writeString(this.amount);
            }

            public Material() {
            }

            protected Material(Parcel in) {
                this.mname = in.readString();
                this.type = in.readString();
                this.amount = in.readString();
            }

            public static final Creator<Material> CREATOR = new Creator<Material>() {
                @Override
                public Material createFromParcel(Parcel source) {
                    return new Material(source);
                }

                @Override
                public Material[] newArray(int size) {
                    return new Material[size];
                }
            };
        }

        public static class Process implements Parcelable {
            public String pcontent;
            public String pic;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.pcontent);
                dest.writeString(this.pic);
            }

            public Process() {
            }

            protected Process(Parcel in) {
                this.pcontent = in.readString();
                this.pic = in.readString();
            }

            public static final Creator<Process> CREATOR = new Creator<Process>() {
                @Override
                public Process createFromParcel(Parcel source) {
                    return new Process(source);
                }

                @Override
                public Process[] newArray(int size) {
                    return new Process[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.classid);
            dest.writeString(this.name);
            dest.writeString(this.peoplenum);
            dest.writeString(this.preparetime);
            dest.writeString(this.cookingtime);
            dest.writeString(this.content);
            dest.writeString(this.pic);
            dest.writeString(this.tag);
            if (null == material) material = new ArrayList<>();
            dest.writeTypedList(this.material);
            if (null == process) process = new ArrayList<>();
            dest.writeTypedList(this.process);
        }

        public TypeSearchItem() {
        }

        protected TypeSearchItem(Parcel in) {
            this.id = in.readString();
            this.classid = in.readString();
            this.name = in.readString();
            this.peoplenum = in.readString();
            this.preparetime = in.readString();
            this.cookingtime = in.readString();
            this.content = in.readString();
            this.pic = in.readString();
            this.tag = in.readString();
            this.material = in.createTypedArrayList(Material.CREATOR);
            this.process = in.createTypedArrayList(Process.CREATOR);
        }

        public static final Creator<TypeSearchItem> CREATOR = new Creator<TypeSearchItem>() {
            @Override
            public TypeSearchItem createFromParcel(Parcel source) {
                return new TypeSearchItem(source);
            }

            @Override
            public TypeSearchItem[] newArray(int size) {
                return new TypeSearchItem[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.num);
        if (null == list) list = new ArrayList<>();
        dest.writeTypedList(this.list);
    }

    public TypeSearchVo() {
    }

    protected TypeSearchVo(Parcel in) {
        this.num = in.readString();
        this.list = in.createTypedArrayList(TypeSearchItem.CREATOR);
    }

    public static final Creator<TypeSearchVo> CREATOR = new Creator<TypeSearchVo>() {
        @Override
        public TypeSearchVo createFromParcel(Parcel source) {
            return new TypeSearchVo(source);
        }

        @Override
        public TypeSearchVo[] newArray(int size) {
            return new TypeSearchVo[size];
        }
    };
}
