package concurrent;

import java.util.concurrent.Semaphore;

/**
 * @Author: Adamo_chen
 * @Date: 2019/10/27 1:02
 * @Version 1.0
 */
public class SemaphoreTest extends Thread{

    private static SemaphoreSources semaphoreSources = new SemaphoreSources(new Semaphore(2));

    public static void main(String[] args) {
        SemaphoreTest ss = new  SemaphoreTest();
        SemaphoreTest ss1 = new SemaphoreTest();
        SemaphoreTest ss2 = new SemaphoreTest();
        SemaphoreTest ss3 = new SemaphoreTest();

        ss.start();
//        ss1.start();
//        ss2.start();
//        ss3.start();


    }

    @Override
    public void run() {
        try {
            semaphoreSources.getSources();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class SemaphoreSources{
    private Semaphore semaphore;

    public SemaphoreSources(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public void getSources() throws InterruptedException {
        semaphore.acquire();
        System.out.println(Thread.currentThread());
        Thread.sleep(2000);
        //semaphore.release();
    }
}
