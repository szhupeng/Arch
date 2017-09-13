package space.zhupeng.fxbase.event;

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

    private EventDispatcher() {
    }

    public static void dispatch(Context context, Event event) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent();
        intent.putExtra("code", event.getCode());
        intent.putExtra("data", event.getData());
        manager.sendBroadcast(intent);
    }
}
