package space.zhupeng.fxbase.event;

import android.content.Context;

/**
 * 事件发送器
 *
 * @author zhupeng
 * @date 2016/12/21
 */

public class EventDispatcher {

    private EventDispatcher() {
    }

    private static class DispatcherHolder {
        public static EventDispatcher sInstance = new EventDispatcher();
    }


    public static EventDispatcher get() {
        return DispatcherHolder.sInstance;
    }

    public void dispatch(Context context, Event event) {
//        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
//        Intent intent = new Intent();
//        intent.putExtra("code", event.getCode());
//        intent.putExtra("data", event.getData());
//        manager.sendBroadcast(intent);
    }
}
