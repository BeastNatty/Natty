package com.jeeps.charades.util;

public class Timer {

    private Thread timerThread;
    private TimerListener timerListener;
    private int seconds;

    public Timer(TimerListener timerListener, int seconds) {
        this.timerListener = timerListener;
        this.seconds = seconds;
    }

    public interface TimerListener {
        void onTick(int seconds);
        void onFinish();
    }

    public void start() {
        timerThread = new Thread(() -> {
            while (seconds > 0) {
                try {
                    Thread.sleep(1000);
                    seconds--;
                    timerListener.onTick(seconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            timerListener.onFinish();
        });
        timerThread.start();
    }

    public void stop() {
        timerThread.interrupt();
    }
}
