package space.zhupeng.fxbase.widget.adapter.entity;

import java.io.Serializable;

public class SectionEntity<T> implements Serializable {
    public boolean isHeader;
    public T data;
    public String header;

    public SectionEntity(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.data = null;
    }

    public SectionEntity(T data) {
        this.isHeader = false;
        this.header = null;
        this.data = data;
    }
}
