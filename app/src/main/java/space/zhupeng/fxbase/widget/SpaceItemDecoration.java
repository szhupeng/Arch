package space.zhupeng.fxbase.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * RecyclerView网格间距
 *
 * @author zhupeng
 * @date 2016/12/11
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;
    private int mSpanCount;
    private boolean isIncludeEdge;
    private boolean isIncludeTB;

    public SpaceItemDecoration(int spanCount, int space, boolean includeEdge) {
        this.mSpanCount = spanCount;
        this.mSpace = space;
        this.isIncludeEdge = includeEdge;
        this.isIncludeTB = false;
    }

    public SpaceItemDecoration(int space, boolean includeTB) {
        this.mSpanCount = 1;
        this.mSpace = space;
        this.isIncludeEdge = false;
        this.isIncludeTB = includeTB;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (0 == mSpanCount) {
            mSpanCount = 1;
        }

        int position = parent.getChildAdapterPosition(view);
        int column = position % mSpanCount;
        if (isIncludeEdge) {
            outRect.left = mSpace - column * mSpace / mSpanCount;
            outRect.right = (column + 1) * mSpace / mSpanCount;

            if (position < mSpanCount) {
                outRect.top = mSpace;
            }
            outRect.bottom = mSpace;
        } else if (!isIncludeTB) {
            outRect.left = column * mSpace / mSpanCount;
            outRect.right = mSpace - (column + 1) * mSpace / mSpanCount;
            if (position >= mSpanCount) {
                outRect.top = mSpace;
            }
        } else {
            outRect.left = column * mSpace / mSpanCount;
            outRect.right = mSpace - (column + 1) * mSpace / mSpanCount;
            outRect.top = mSpace;
            if (position == parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = mSpace;
            }
        }
    }
}
