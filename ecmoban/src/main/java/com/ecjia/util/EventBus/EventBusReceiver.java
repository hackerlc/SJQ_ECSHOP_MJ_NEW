package com.ecjia.util.EventBus;

/**
 * Created by Adam on 2016-03-17.
 */
public interface EventBusReceiver {

    void onEvent(Event event);
}
