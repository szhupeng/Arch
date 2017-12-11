package space.zhupeng.fxbase.widget.adapter.manager;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * 所有平滑滚动布局管理器的通用类.
 */
public class TopSnappedSmoothScroller extends LinearSmoothScroller {

    /**
     * 修改此值会影响所有布局管理器的创建.
     * 注意：每当你改变这个值，你必须重新创建LayoutManager实例并将其重新分配给RecyclerView！
     * <p>默认值是{code 100f}。 Android的默认值是{code 25f}。</ p>
     */
    public static float MILLISECONDS_PER_INCH = 100f;

    private PointF vectorPosition = new PointF(0, 0);
    private FlexibleLayoutManager layoutManager;

    @SuppressWarnings("WeakerAccess")
    public TopSnappedSmoothScroller(Context context, RecyclerView.LayoutManager layoutManager) {
        super(context);
        this.layoutManager = new FlexibleLayoutManager(layoutManager);
    }

    /**
     * 控制SmoothScroller查找视图的方向
     *
     * @return the vector position
     */
    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        final int firstChildPos = layoutManager.findFirstCompletelyVisibleItemPosition();
        final int direction = targetPosition < firstChildPos ? -1 : 1;

        if (layoutManager.getOrientation() == OrientationHelper.HORIZONTAL) {
            vectorPosition.set(direction, 0);
            return vectorPosition;
        } else {
            vectorPosition.set(0, direction);
            return vectorPosition;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }

}