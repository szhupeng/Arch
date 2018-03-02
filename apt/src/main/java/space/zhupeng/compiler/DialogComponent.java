package space.zhupeng.compiler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhupeng
 * @date 2018/2/2
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface DialogComponent {
}
