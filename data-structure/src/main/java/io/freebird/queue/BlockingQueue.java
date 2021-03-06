package io.freebird.queue;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列容器的数组版简单实现
 *
 * @author zhouziyan
 * @date 2019/6/24 16:06
 */
public class BlockingQueue<T> {

    /**
     * 容器
     */
    private final Object[] items;
    /**
     * 容器的大小
     */
    private int size = 0;

    private final Lock lock = new ReentrantLock(true);
    /**
     * 队列不空的条件
     */
    private final Condition notEmpty = lock.newCondition();
    /**
     * 队列不满的条件
     */
    private final Condition notFull = lock.newCondition();

    public BlockingQueue(int capacity) {
        this.items = new Object[capacity];
    }

    /**
     * 保证进队的有序性
     * 以及整个队列元素的线程安全性
     * 多余的线程一直挂着占用资源,因此应该适当的时间将其释放
     *
     * @param t 进队元素
     */
    public void enter(T t) throws InterruptedException {
        if (t == null) {
            throw new NullPointerException("the enter element cannot be null.");
        }
        lock.lockInterruptibly();
        try {
            while (size == items.length) {
                System.out.println("队列满了...");
                notFull.await();
            }
            items[size++] = t;
            // 队列新增了元素,唤醒notEmpty中挂起的线程.
            System.out.println("我要入队...");
            notEmpty.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T poll() throws InterruptedException {
        lock.lockInterruptibly();
        T ret = null;
        final T tail = (T) items[items.length - 1];
        try {
            while (size == 0) {
                // 挂起当前线程.等待唤醒.一直挂起很浪费资源.所以应该挂起一定的时间就中断线程.
                System.out.println("队列空了...");
                notEmpty.await();
            }
            ret = (T) items[0];
            for (int i = 0; i < size; i++) {
                if (size == items.length) {
                    items[i] = i + 1 == size ? tail : items[i + 1];
                } else {
                    items[i] = items[i + 1];
                }
            }
            items[--size] = null;
            System.out.println("我要出队...");
            notFull.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return ret;
    }
}
