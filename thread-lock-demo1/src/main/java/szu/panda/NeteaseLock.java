package szu.panda;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

// 自己实现一个lock锁
public class NeteaseLock implements Lock {
    // CAS 锁的持有者
    AtomicReference<Thread> owner = new AtomicReference<>();
    // 集合 存储等待的线程
    public LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    // 尝试获取锁
    @Override
    public boolean tryLock() {
        return owner.compareAndSet(null, Thread.currentThread());
    }

    @Override
    public void lock() { // 没有锁，等待
        boolean addQ = true;
        while (!tryLock()) {
            if (addQ) {
                waiters.add(Thread.currentThread()); // 将当前线程对象存放集合
                addQ = false;
            } else {
                // 进入等待,挂起线程，代码不继续往下跑，等待唤醒(unlock)
                LockSupport.park(); // 挂起这个线程
            }
        }
        // 如果抢到锁，从waiter 删除当前线程
        waiters.remove(Thread.currentThread());
    }

    @Override
    public void unlock() {
        if (owner.compareAndSet(Thread.currentThread(), null)) {
            // 通知其他正在等待锁的线程
            Thread next = null;
            while (true) {
                next = waiters.peek();
                if (next == null) {
                    break;
                }
                // 唤醒
                LockSupport.unpark(next);
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }
}
