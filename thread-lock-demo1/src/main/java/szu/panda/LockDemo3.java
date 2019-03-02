package szu.panda;

import java.util.concurrent.locks.Lock;

public class LockDemo3 {
    int i = 0;
    Lock lock = new NeteaseLock();

    public void add() {
        lock.lock(); // 多线程 同步 （只有一把锁）
        try {
            i++;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo3 ld = new LockDemo3();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    ld.add();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(ld.i);
    }
}
