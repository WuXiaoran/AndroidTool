package com.tool.network.retrofit.download;

/**
 * @作者          吴孝然
 * @创建日期      2019/2/11 10:04
 * @描述          下载状态
 **/
public enum  DownState {
    START(0),
    DOWN(1),
    PAUSE(2),
    STOP(3),
    ERROR(4),
    FINISH(5);
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    DownState(int state) {
        this.state = state;
    }
}
