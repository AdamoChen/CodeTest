package concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Adamo_chen
 * @Date: 2020/5/27 22:03
 * @Version 1.0
 */
public class ThreadPoolTest extends Thread{

    public static void main(String[] args) {
//        ExecutorService es = Executors.newFixedThreadPool(3);
//        es.execute(new task(es));
        ThreadPoolTest tpt = new ThreadPoolTest();
        tpt.init();
        tpt.start();
    }

    @Override
    public void run() {
        super.run();
        init();
    }

    private void init(){
        ExecutorService es = Executors.newFixedThreadPool(3);
        es.execute(new Task(es));
        System.out.println(Thread.currentThread().getName() + " init done ---");
        return;
    }


    class Task implements Runnable{

        ExecutorService executorService;

        public Task(ExecutorService executorService) {
            this.executorService = executorService;
        }

        public void run() {
            System.out.println("111111");
            CountDownLatch countDownLatch = new CountDownLatch(2);
            Work work = new Work(countDownLatch);
            work.threadLocal.set(executorService);

            executorService.execute(work);
            executorService.execute(work);

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println(Thread.currentThread().getName() + " work : " + work.threadLocal.get());

            CountDownLatch countDownLatch2 = new CountDownLatch(2);
            Work work1 = new Work(countDownLatch2);
            executorService.execute(work1);
            executorService.execute(work1);

            System.out.println(Thread.currentThread().getName() + " work1 : " + work1.threadLocal.get());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("2222222");
            work.threadLocal.get().shutdown();
            System.out.println(Thread.currentThread().getName() + " shutdown after : " + work1.threadLocal.get());
        }
    }

    static class Work implements Runnable{

        static ThreadLocal<ExecutorService> threadLocal = new ThreadLocal<ExecutorService>();

        CountDownLatch countDownLatch;

        public Work(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " execute ");
            countDownLatch.countDown();
        }
    }

}


