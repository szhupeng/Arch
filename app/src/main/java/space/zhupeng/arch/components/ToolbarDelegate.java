package space.zhupeng.arch.components;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author zhupeng
 * @date 2017/12/10
 */

public interface ToolbarDelegate {

    void bindClickEvent(View.OnClickListener listener);

    void setLeft(AppCompatActivity activity, @DrawableRes int resId);

    void setLeft(CharSequence text);

    void setLeft(AppCompatActivity activity, @DrawableRes int resId, CharSequence text);

    void setCenterTitle(CharSequence title);

    void setCenterTitle(AppCompatActivity activity, @StringRes int resId);

    void setCenterSubtitle(CharSequence subtitle);

    void setCenterSubtitle(CharSequence subtitle, float size);

    void setCenterSubtitle(CharSequence subtitle, float size, int color);

    void setRightText(CharSequence text);

    void setRightIcon(@DrawableRes int resId);

    void showLeft();

    void hideLeft();

    void showCenter();

    void hideCenter();

    void showRightText();

    void hideRightText();

    void showRightIcon();

    void hideRightIcon();
}
