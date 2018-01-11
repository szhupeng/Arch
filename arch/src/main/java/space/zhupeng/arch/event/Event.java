package space.zhupeng.arch.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zhupeng
 * @date 2016/12/21
 */

public class Event<T extends Parcelable> implements Parcelable {
    private T data;
    private int code;

    public Event(T data) {
        this.data = data;
    }

    public Event(T data, int code) {
        this.data = data;
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeInt(code);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel source) {
        data = source.readParcelable(Parcelable.class.getClassLoader());
        code = source.readInt();
    }
}