package space.zhupeng.arch.utils;

import android.util.SparseArray;

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
}
