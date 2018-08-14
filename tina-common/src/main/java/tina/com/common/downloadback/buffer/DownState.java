package tina.com.common.downloadback.buffer;

/**
 * 下载状态
 * by：小民修订，原版：WZG
 */

public enum DownState {
    NORMAL(0),   //正常
    WAIT(1),     //等待
    DOWN(2),
    PAUSE(3),
    STOP(4),
    ERROR(5),
    FINISH(6);
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
