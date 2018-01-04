package space.zhupeng.arch.event;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 事件发送器
 *
 * @author zhupeng
 * @date 2016/12/21
 */

public class EventDispatcher {

    public static final String ACTION_EVENT = "intent.action.event";

    private EventDispatcher() {
    }

    public static void dispatch(Context context, Event event) {
        Intent intent = new Intent(ACTION_EVENT);
        intent.putExtra("event", event);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
