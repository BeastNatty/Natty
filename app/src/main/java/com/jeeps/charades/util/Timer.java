package com.jeeps.charades.util;

public class Timer {

    private Thread timerThread;
    private TimerListener timerListener;
    private int seconds;
    private boolean isInterrupted;

    public Timer(TimerListener timerListener, int seconds) {
        this.timerListener = timerListener;
        this.seconds = seconds;
        isInterrupted = false;
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
                    break;
                }
            }
            if (!isInterrupted)
                timerListener.onFinish();
        });
        timerThread.setName("TIMER THREAD");
        timerThread.setDaemon(true);
        timerThread.start();
    }

    public void stop() {
        isInterrupted = true;
        timerThread.interrupt();
    }
}
