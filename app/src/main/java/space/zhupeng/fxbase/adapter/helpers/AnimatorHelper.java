package space.zhupeng.fxbase.adapter.helpers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class AnimatorHelper {

	/*-----------*/
    /* ANIMATORS */
    /*-----------*/

    /**
     * This is the default animator.
     *
     * @param animators user defined list of animators
     * @param view      itemView to animate
     * @param alphaFrom starting alpha value
     */
    public static void alphaAnimator(
            @NonNull List<Animator> animators, @NonNull View view, @FloatRange(from = 0.0, to = 1.0) float alphaFrom) {
        view.setAlpha(0);
        animators.add(ObjectAnimator.ofFloat(view, "alpha", alphaFrom, 1f));
    }

    /**
     * Item will slide from Left to Right.
     *
     * @param animators user defined list of animators
     * @param view      itemView to animate
     * @param percent   any % multiplier (between 0 and 1) of the LayoutManager Width
     */
    public static void slideInFromLeftAnimator(
            @NonNull List<Animator> animators, @NonNull View view,
            RecyclerView recyclerView, @FloatRange(from = 0.0, to = 1.0) float percent) {
        alphaAnimator(animators, view, 0f);
        animators.add(ObjectAnimator.ofFloat(view, "translationX", -recyclerView.getLayoutManager().getWidth() * percent, 0));
    }

    /**
     * Item will slide from Right to Left.
     *
     * @param animators user defined list of animators
     * @param view      ItemView to animate
     * @param percent   Any % multiplier (between 0 and 1) of the LayoutManager Width
     */
    public static void slideInFromRightAnimator(
            @NonNull List<Animator> animators, @NonNull View view,
            RecyclerView recyclerView, @FloatRange(from = 0.0, to = 1.0) float percent) {
        alphaAnimator(animators, view, 0f);
        animators.add(ObjectAnimator.ofFloat(view, "translationX", recyclerView.getLayoutManager().getWidth() * percent, 0));
    }

    /**
     * Item will slide from Top of the screen to its natural position.
     *
     * @param animators user defined list of animators
     * @param view      itemView to animate
     */
    public static void slideInFromTopAnimator(
            @NonNull List<Animator> animators, @NonNull View view,
            RecyclerView recyclerView) {
        alphaAnimator(animators, view, 0f);
        animators.add(ObjectAnimator.ofFloat(view, "translationY", -recyclerView.getMeasuredHeight() >> 1, 0));
    }

    /**
     * Item will slide from Bottom of the screen to its natural position.
     *
     * @param animators user defined list of animators
     * @param view      itemView to animate
     */
    public static void slideInFromBottomAnimator(
            @NonNull List<Animator> animators, @NonNull View view,
            RecyclerView recyclerView) {
        alphaAnimator(animators, view, 0f);
        animators.add(ObjectAnimator.ofFloat(view, "translationY", recyclerView.getMeasuredHeight() >> 1, 0));
    }

    /**
     * Item will scale to {@code 1.0f}.
     *
     * @param animators user defined list of animators
     * @param view      itemView to animate
     * @param scaleFrom initial scale value
     */
    public static void scaleAnimator(
            @NonNull List<Animator> animators, @NonNull View view, @FloatRange(from = 0.0, to = 1.0) float scaleFrom) {
        alphaAnimator(animators, view, 0f);
        animators.add(ObjectAnimator.ofFloat(view, "scaleX", scaleFrom, 1f));
        animators.add(ObjectAnimator.ofFloat(view, "scaleY", scaleFrom, 1f));
    }

    /**
     * Item will flip from {@code 0.0f} to {@code 1.0f}.
     *
     * @param animators user defined list of animators
     * @param view      itemView to animate
     */
    public static void flipAnimator(@NonNull List<Animator> animators, @NonNull View view) {
        alphaAnimator(animators, view, 0f);
        animators.add(ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f));
    }

    /**
     * Adds a custom duration to the current view.
     *
     * @param animators user defined list of animators
     * @param duration  duration in milliseconds
     */
    public static void setDuration(@NonNull List<Animator> animators, @IntRange(from = 0) long duration) {
        if (animators.size() > 0) {
            Animator animator = animators.get(animators.size() - 1);
            animator.setDuration(duration);
        }
    }

}