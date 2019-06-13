package bird.free.demo;

import bird.free.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhouziyan
 * @review:
 * @Date: 2019/6/13 17:33
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        ThreadFactory threadFactory = ThreadUtils.createThreadFactory("my-thread");
        ExecutorService executorService = ThreadUtils.createThreadPool(5, 10, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(5), threadFactory);
        executorService.execute(()-> System.out.println(Thread.currentThread().getName()));
    }
}
