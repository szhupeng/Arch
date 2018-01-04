package space.zhupeng.arch.utils;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupeng
 * @date 2017/8/24
 */

public class DataUtils {

    private DataUtils() {
        throw new UnsupportedOperationException("this method can't be called");
    }

    /**
     * SparseArray转ArrayList
     *
     * @param array
     * @param <T>
     * @return
     */
    public static final <T> List<T> asList(final SparseArray<T> array) {
        if (array == null) return null;

        List<T> list = new ArrayList<T>(array.size());
        for (int i = 0; i < array.size(); i++) {
            list.add(array.valueAt(i));
        }
        return list;
    }

    /**
     * Json字符串转换成List
     *
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> getObjectList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
