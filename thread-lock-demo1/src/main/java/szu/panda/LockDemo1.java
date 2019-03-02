package szu.panda;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

// 解决方案-1-CAS
public class LockDemo1 {
    volatile int value = 0;// 多线程操作

    // 我们要直接操作内存
    static Unsafe unsafe; // 直接内存操作 修改对象，修改属性
    static long valueOffset; // 属性的偏移量（用它来定位 对象内具体属性的内存地址）

    static {
        try {
            // 1、 拿到一个unsafe实例
            Field declaredField = Unsafe.class.getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            unsafe = (Unsafe) declaredField.get(null);

            // 2、 CAS操作，直接操作内存。 获取 属性的偏移量（用它来定位 对象内具体属性的内存地址）
            valueOffset = unsafe.objectFieldOffset(LockDemo1.class.getDeclaredField("value"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add() {
        // cas 比较和替换
        int current;
        do {
            current = unsafe.getIntVolatile(this, valueOffset); // 获取内存中最新的值
        } while (!unsafe.compareAndSwapInt(this, valueOffset, current, current + 1));
        // value++;
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo1 ld = new LockDemo1();

        // 期望20000
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    ld.add();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(ld.value);
    }
}
