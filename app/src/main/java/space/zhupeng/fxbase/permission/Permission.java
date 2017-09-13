package space.zhupeng.fxbase.permission;

/**
 * @author zhupeng
 * @date 2017/9/10
 */

public class Permission {

    public final String mName;
    public final boolean isGranted;
    public final boolean shouldShowRequestPermissionRationale;

    public Permission(String name, boolean granted) {
        this(name, granted, false);
    }

    public Permission(String name, boolean granted, boolean shouldShowRequestPermissionRationale) {
        this.mName = name;
        this.isGranted = granted;
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Permission that = (Permission) o;

        if (isGranted != that.isGranted) return false;
        if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale)
            return false;
        return mName.equals(that.mName);
    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + (isGranted ? 1 : 0);
        result = 31 * result + (shouldShowRequestPermissionRationale ? 1 : 0);
        return result;
    }
}
