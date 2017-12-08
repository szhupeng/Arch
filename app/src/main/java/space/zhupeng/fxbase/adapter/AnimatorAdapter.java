package space.zhupeng.fxbase.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * This class is responsible to animate items. Bounded items are animated initially and also
 * when user starts to scroll the list.
 * <p>Animations can be customized for each items applying different logic based on item position
 * and beyond.</p>
 * This class is extended by {@link FlexibleAdapter}.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AnimatorAdapter extends SelectableAdapter {

    private Interpolator mInterpolator = new LinearInterpolator();
    private AnimatorAdapterDataObserver mAnimatorNotifierObserver;
    private boolean mEntryStep = true;

    private enum AnimatorEnum {
        ALPHA, SLIDE_IN_LEFT, SLIDE_IN_RIGHT, SLIDE_IN_BOTTOM, SLIDE_IN_TOP, SCALE
    }

    /**
     * The active Animators. Keys are hash codes of the Views that are animated.
     */
    private final SparseArray<Animator> mAnimators = new SparseArray<>();

    /**
     * The position of the last item that was animated.
     */
    private int mLastAnimatedPosition = -1;

    /**
     * Max items RecyclerView displays
     */
    private int mMaxChildViews = -1;

    /**
     * Contains type of animators already added
     */
    private EnumSet<AnimatorEnum> animatorsUsed = EnumSet.noneOf(AnimatorEnum.class);

    private boolean isReverseEnabled = false, shouldAnimate = false,
            onlyEntryAnimation = false, animateFromObserver = false;

    private static long DEFAULT_DURATION = 300L;
    private long mInitialDelay = 0L,
            mStepDelay = 100L,
            mDuration = DEFAULT_DURATION;

	/*--------------*/
    /* CONSTRUCTORS */
    /*--------------*/

    /**
     * Simple Constructor for Animator Adapter.
     */
    AnimatorAdapter(boolean hasStableIds) {
        super();
        setHasStableIds(hasStableIds);

        //Get notified when an item is changed (should skip animation)
        mAnimatorNotifierObserver = new AnimatorAdapterDataObserver();
        registerAdapterDataObserver(mAnimatorNotifierObserver);
    }

	/*-----------------------*/
    /* CONFIGURATION SETTERS */
    /*-----------------------*/

    /**
     * @param animate true to notify this Adapter that initialization is started and so
     *                animate items, false to inform that the operation is complete
     */
    void setScrollAnimate(boolean animate) {
        this.animateFromObserver = animate;
    }

    /**
     * Sets the initial delay for the first item animation.
     * <p>Default value is {@code 0ms}.</p>
     *
     * @param initialDelay any non negative delay
     * @return this AnimatorAdapter, so the call can be chained
     */
    public AnimatorAdapter setAnimationInitialDelay(long initialDelay) {
        mInitialDelay = initialDelay;
        return this;
    }

    /**
     * Sets the step delay between an animation and the next to be added to the initial delay.
     * <p>The delay is added on top of the previous delay.</p>
     * Default value is {@code 100ms}.
     *
     * @param delay any positive delay
     * @return this AnimatorAdapter, so the call can be chained
     */
    public AnimatorAdapter setAnimationDelay(@IntRange(from = 0) long delay) {
        mStepDelay = delay;
        return this;
    }

    /**
     * 初始加载动画是否要在项与项之间使用步骤延迟，当设置为false的时候，所有列表项都会无延迟执行动画
     * <p>使用网格布局时最好禁用</p>
     * 默认值为 {@code true}.
     *
     * @param entryStep true to enable step delay, false otherwise
     * @return this AnimatorAdapter, so the call can be chained
     */
    public AnimatorAdapter setAnimationEntryStep(boolean entryStep) {
        this.mEntryStep = entryStep;
        return this;
    }

    /**
     * Sets the duration of the animation for ALL items.
     * <p>Default value is {@code 300ms}.</p>
     *
     * @param duration any positive time
     * @return this AnimatorAdapter, so the call can be chained
     */
    public AnimatorAdapter setAnimationDuration(@IntRange(from = 1) long duration) {
        mDuration = duration;
        return this;
    }

    /**
     * Sets a custom interpolator for ALL items.
     * <p>Default value is {@link LinearInterpolator}.</p>
     *
     * @param interpolator any valid non null interpolator
     * @return this AnimatorAdapter, so the call can be chained
     */
    public AnimatorAdapter setAnimationInterpolator(@NonNull Interpolator interpolator) {
        mInterpolator = interpolator;
        return this;
    }

    /**
     * Enables/Disables item animation while scrolling and on loading.
     * <p>Enabling scrolling will disable onlyEntryAnimation.<br>
     * Disabling scrolling will disable also reverse scrolling.</p>
     * Default value is {@code false}.
     * <p><b>Note:</b> Loading animation can only be performed if the Adapter is initialized
     * with some items using the constructor.</p>
     *
     * @param enabled true to enable item animation, false to disable them all.
     * @return this AnimatorAdapter, so the call can be chained
     * @see #setOnlyEntryAnimation(boolean)
     * @see #setAnimationOnReverseScrolling(boolean)
     */
    public AnimatorAdapter setAnimationOnScrolling(boolean enabled) {
        if (enabled) this.onlyEntryAnimation = false;
        shouldAnimate = enabled;
        return this;
    }

    public boolean isAnimationOnScrollingEnabled() {
        return shouldAnimate;
    }

    /**
     * Enables reverse scrolling animation if AnimationOnScrolling is also enabled!
     * <p>Value is ignored if basic animation on scrolling is disabled.</p>
     * Default value is {@code false} (only forward).
     *
     * @param enabled false to animate items only forward, true to also reverse animate
     * @return this AnimatorAdapter, so the call can be chained
     * @see #setAnimationOnScrolling(boolean)
     */
    public AnimatorAdapter setAnimationOnReverseScrolling(boolean enabled) {
        isReverseEnabled = enabled;
        return this;
    }

    /**
     * @return true if items are animated also on reverse scrolling, false only forward
     */
    public boolean isAnimationOnReverseScrollingEnabled() {
        return isReverseEnabled;
    }

    /**
     * Performs only entry animation during the initial loading. Stops the animation after
     * the last visible item in the RecyclerView has been animated.
     * <p><b>Note:</b> Loading animation can only be performed if the Adapter is initialized
     * with some items using the Constructor.</p>
     * Default value is {@code false}.
     *
     * @param enabled true to perform only entry animation, false otherwise
     * @return this AnimatorAdapter, so the call can be chained
     * @see #setAnimationOnScrolling(boolean)
     */
    public AnimatorAdapter setOnlyEntryAnimation(boolean enabled) {
        if (enabled) this.shouldAnimate = true;
        this.onlyEntryAnimation = enabled;
        return this;
    }

    /**
     * @return true if the scrolling animation will occur only at startup until the screen is
     * filled with the items, false animation will be performed when scrolling too.
     */
    public boolean isOnlyEntryAnimation() {
        return onlyEntryAnimation;
    }

	/*--------------*/
    /* MAIN METHODS */
    /*--------------*/

    /**
     * 取消指定View的动画，当fling的时候有用
     */
    private void cancelExistingAnimation(final int hashCode) {
        Animator animator = mAnimators.get(hashCode);
        if (animator != null) animator.end();
    }

    /**
     * 检查给定位置的项是否Header或Footer
     *
     * @param position
     * @return true if it's a scrollable item
     */
    public abstract boolean isScrollableHeaderOrFooter(int position);

    /**
     * Performs checks to scroll animate the itemView and in case, it animates the view.
     * <p><b>Note:</b> If you have to change at runtime the LayoutManager <i>and</i> add
     * Scrollable Headers too, consider to add them in post, using a {@code delay >= 0},
     * otherwise scroll animations on all items will not start correctly.</p>
     *
     * @param holder   the ViewHolder just bound
     * @param position the current item position
     */
    protected final void animateView(final RecyclerView.ViewHolder holder, final int position) {
        if (mRecyclerView == null) return;

        // Use always the max child count reached
        if (mMaxChildViews < mRecyclerView.getChildCount()) {
            mMaxChildViews = mRecyclerView.getChildCount();
        }
        // Animate only during initial loading?
        if (onlyEntryAnimation && mLastAnimatedPosition >= mMaxChildViews) {
            shouldAnimate = false;
        }
        int lastVisiblePosition = getFlexibleLayoutManager().findLastVisibleItemPosition();
        if (holder instanceof FlexibleViewHolder && shouldAnimate && !isFastScroll &&
                !mAnimatorNotifierObserver.isPositionNotified() &&
                (position > lastVisiblePosition || isReverseEnabled || isScrollableHeaderOrFooter(position) || (position == 0 && mMaxChildViews == 0))) {

            // Cancel animation is necessary when fling
            int hashCode = holder.itemView.hashCode();
            cancelExistingAnimation(hashCode);

            // User animators
            List<Animator> animators = new ArrayList<>();
            FlexibleViewHolder flexibleViewHolder = (FlexibleViewHolder) holder;
            flexibleViewHolder.scrollAnimators(animators, position, position >= lastVisiblePosition);

            // Execute the animations together
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animators);
            set.setInterpolator(mInterpolator);
            // Single view duration
            long duration = mDuration;
            for (Animator animator : animators) {
                if (animator.getDuration() != DEFAULT_DURATION) {
                    duration = animator.getDuration();
                }
            }
            //log.v("duration=%s", duration);
            set.setDuration(duration);
            set.addListener(new HelperAnimatorListener(hashCode));
            if (mEntryStep) {
                // Stop stepDelay when screen is filled
                set.setStartDelay(calculateAnimationDelay(position));
            }
            set.start();
            mAnimators.put(hashCode, set);
            //log.v("animateView    Scroll animation on position %s", position);
        }
        mAnimatorNotifierObserver.clearNotified();
        // Update last animated position
        mLastAnimatedPosition = position;
    }

    /**
     * @param position the position just bound
     * @return the delay in milliseconds after which, the animation for next ItemView should start.
     */
    private long calculateAnimationDelay(int position) {
        long delay;
        int firstVisiblePosition = getFlexibleLayoutManager().findFirstCompletelyVisibleItemPosition();
        int lastVisiblePosition = getFlexibleLayoutManager().findLastCompletelyVisibleItemPosition();

        // Fix for high delay on the first visible item on rotation
        if (firstVisiblePosition < 0 && position >= 0)
            firstVisiblePosition = position - 1;

        // Last visible position is the last animated when initially loading
        if (position - 1 > lastVisiblePosition)
            lastVisiblePosition = position - 1;

        int visibleItems = lastVisiblePosition - firstVisiblePosition;
        int numberOfAnimatedItems = position - 1;

        if (mMaxChildViews == 0 || visibleItems < numberOfAnimatedItems || //Normal Forward scrolling after max itemOnScreen is reached
                (firstVisiblePosition > 1 && firstVisiblePosition <= mMaxChildViews) || //Reverse scrolling
                (position > mMaxChildViews && firstVisiblePosition == -1 && mRecyclerView.getChildCount() == 0)) { //Reverse scrolling and click on FastScroller

            // Base delay is step delay
            delay = mStepDelay;
            if (visibleItems <= 1) {
                // When RecyclerView is initially loading no items are present
                // Use InitialDelay only for the first item
                delay += mInitialDelay;
            } else {
                // Reset InitialDelay only when first item is already animated
                mInitialDelay = 0L;
            }
            int numColumns = getFlexibleLayoutManager().getSpanCount();
            if (numColumns > 1) {
                delay = mInitialDelay + mStepDelay * (position % numColumns);
            }

        } else { //forward scrolling before max itemOnScreen is reached
            delay = mInitialDelay + (position * mStepDelay);
        }

        return delay;
    }

	/*---------------*/
    /* INNER CLASSES */
    /*---------------*/

    /**
     * Observer Class responsible to skip animation when items are notified to avoid
     * double animation with {@link android.support.v7.widget.RecyclerView.ItemAnimator}.
     * <p>Also, some items at the edge, are rebound by Android and should not be animated.</p>
     */
    private class AnimatorAdapterDataObserver extends RecyclerView.AdapterDataObserver {
        private boolean notified;
        private Handler mAnimatorHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            public boolean handleMessage(Message message) {
                //log.v("Clear notified for scrolling Animations");
                notified = false;
                return true;
            }
        });

        public boolean isPositionNotified() {
            return notified;
        }

        public void clearNotified() {
            if (notified) {
                mAnimatorHandler.removeCallbacksAndMessages(null);
                mAnimatorHandler.sendMessageDelayed(Message.obtain(mAnimatorHandler), 200L);
            }
        }

        private void markNotified() {
            notified = !animateFromObserver;
        }

        @Override
        public void onChanged() {
            markNotified();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            markNotified();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            markNotified();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            markNotified();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            markNotified();
        }
    }

    /**
     * Helper Class to clear Animators List used to avoid multiple Item animation on same
     * position when fling.
     */
    private class HelperAnimatorListener implements Animator.AnimatorListener {
        int key;

        HelperAnimatorListener(int key) {
            this.key = key;
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mAnimators.remove(key);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}