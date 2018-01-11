package space.zhupeng.arch.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import space.zhupeng.arch.R;
import space.zhupeng.arch.utils.DensityUtils;

/**
 * @author zhupeng
 * @date 2018/1/8
 */

public class BottomNavigationBar extends BottomNavigationView {

    public static abstract class BadgeProvider {
        @DrawableRes
        public int getBadgeDrawableRes() {
            return 0;
        }

        public int getWidth() {
            return 16;
        }

        public int getPadding() {
            return 3;
        }

        public abstract int getMarginTop();

        public abstract float getFactorRelativeToTop();
    }

    //Value position for badge
    final SparseArray<Integer> mBadgePositions = new SparseArray<Integer>();

    private Drawable mDrawableBadge;
    private BadgeProvider mBadgeProvider;

    //see guideline https://material.io/guidelines/components/bottom-navigation.html#bottom-navigation-specs
    private final int PX_MAX_ITEM_WIDTH = dp2px(168); //check  if 169 is better in despite of guideline, getWidth() return 169

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationBar);
        mDrawableBadge = a.getDrawable(R.styleable.BottomNavigationBar_badge);
        a.recycle();

        mBadgeProvider = new BadgeProvider() {
            @Override
            public int getMarginTop() {
                return 6;
            }

            @Override
            public float getFactorRelativeToTop() {
                return 3.2f;
            }
        };
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initBadge();
    }

    private void initBadge() {
        // Adding badges to each Item
        BottomNavigationMenuView menuView = getBottomMenuView();
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            item.removeView(item.findViewById(android.R.id.text1));

            int width = dp2px(mBadgeProvider.getWidth());
            int height = width;

            //Inflate TextView badge
            TextView textView = new TextView(getContext());
            textView.setId(android.R.id.text1);
            textView.setVisibility(GONE);
            textView.setMinimumWidth(width);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            int padding = dp2px(mBadgeProvider.getPadding());
            textView.setPadding(padding, 0, padding, 0);
            //Build Layout Param of badge
            FrameLayout.LayoutParams layoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height);
            layoutParam.gravity = Gravity.END;
            int screenWidth = DensityUtils.getScreenWidth(getContext());
            screenWidth = screenWidth > PX_MAX_ITEM_WIDTH * menuView.getChildCount() ? PX_MAX_ITEM_WIDTH * menuView.getChildCount() : screenWidth;
            int marginTop = dp2px(mBadgeProvider.getMarginTop());
            int marginRight = (int) (((screenWidth / menuView.getChildCount()) * 0.5f) - marginTop * mBadgeProvider.getFactorRelativeToTop());
            layoutParam.setMargins(0, marginTop, marginRight, 0);
            //Add it to the item
            item.addView(textView, layoutParam);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        disableShiftingMode();
        calculateBadgesPosition();
    }

    @SuppressLint("RestrictedApi")
    public void disableShiftingMode() {
        //This disables the shifting mode (which makes tab grow on select)
        BottomNavigationMenuView menuView = getBottomMenuView();
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    // allow to get the BottomMenuView
    private BottomNavigationMenuView getBottomMenuView() {
        Object menuView = null;
        try {
            Field field = BottomNavigationView.class.getDeclaredField("mMenuView");
            field.setAccessible(true);
            menuView = field.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (BottomNavigationMenuView) menuView;
    }

    /**
     * This method place dynamically badge on the bottom bar
     */
    private void calculateBadgesPosition() {
        //Manage display of badges
        BottomNavigationMenuView menuView = getBottomMenuView();
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            //noinspection RestrictedApi
            Integer value = mBadgePositions.get(i);
            TextView badge = (TextView) item.findViewById(android.R.id.text1);
            if (badge != null) {
                setBadge(badge, value);
                if (mDrawableBadge != null) {
                    Drawable drawable = mDrawableBadge.getConstantState() != null ? mDrawableBadge.getConstantState().newDrawable() : null;
                    ViewCompat.setBackground(badge, drawable);
                }
            }
        }
    }

    public void setBadgeProvider(@NonNull BadgeProvider provider) {
        if (null == provider) return;

        this.mBadgeProvider = provider;

        if (provider.getBadgeDrawableRes() != 0) {
            mDrawableBadge = ContextCompat.getDrawable(getContext(), provider.getBadgeDrawableRes());
        }

        initBadge();
        calculateBadgesPosition();
    }

    private void setBadge(TextView badge, Integer count) {
        //set Value
        if (count != null && count > 0) {
            badge.setText(String.valueOf(count));
            badge.setVisibility(VISIBLE);
        } else {
            badge.setVisibility(GONE);
        }
    }

    public void setBadgePositionValue(int position, int count) {
        mBadgePositions.put(position, count);
        //refresh badges
        calculateBadgesPosition();
    }

    //this is used to programmatically select a tab.
    public boolean setSelected(int index) {
        boolean success = false;
        final BottomNavigationMenuView menu = getBottomMenuView();
        if (menu.getChildCount() > index) {
            final View item = menu.getChildAt(index);
            if (item != null && item instanceof BottomNavigationItemView) {
                success = item.performClick();
            }
        }
        return success;
    }

    private int dp2px(float value) {
        return DensityUtils.dp2px(getContext(), value);
    }
}
