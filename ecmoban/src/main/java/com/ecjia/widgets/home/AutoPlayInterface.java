package com.ecjia.widgets.home;


public interface AutoPlayInterface {
    void onResume();

    void onDestroy();

    void onRefresh();

    void onDetach();

    void startPlay();
}