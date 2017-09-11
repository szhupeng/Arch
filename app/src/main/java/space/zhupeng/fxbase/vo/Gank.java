package space.zhupeng.fxbase.vo;

import java.util.List;

/**
 * @author zhupeng
 * @date 2017/9/10
 */

public class Gank implements BaseVo {
    public boolean error;
    public List<GankData> results;

    public static class GankData {
        public String _id;
        public String createdAt;
        public String desc;
        public String publishedAt;
        public String source;
        public String type;
        public String url;
        public boolean used;
        public String who;

        @Override
        public String toString() {
            return "GankData{" +
                    "_id='" + _id + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", desc='" + desc + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", source='" + source + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", used=" + used +
                    ", who='" + who + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Gank{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
