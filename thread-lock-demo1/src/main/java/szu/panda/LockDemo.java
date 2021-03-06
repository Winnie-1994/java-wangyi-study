package szu.panda;

import java.util.concurrent.atomic.AtomicInteger;

// 多线程 原子性
public class LockDemo {
    // volatile int i = 0;
    AtomicInteger i = new AtomicInteger(0);
    public void add() {
       //  i++;
        i.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo ld = new LockDemo();

        // 期望20000
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
