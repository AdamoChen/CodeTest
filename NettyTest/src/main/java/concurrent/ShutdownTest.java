package concurrent;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Adamo_chen
 * @Date: 2020/5/27 23:06
 * @Version 1.0
 */
public class ShutdownTest {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(1);
        Work work = new Work(es);

        es.execute(work);

        Thread.sleep(3000);
        // 队列里的任务没有执行完就直接关闭
        //es.shutdownNow();
        // 队列里任务会执行完后才会关闭
        es.shutdown();

        System.out.println("关闭操作后： " + new Date());
        // java.util.concurrent.RejectedExecutionException 关闭后添加任务会抛异常
        // es.execute(new Task(new Date()));
    }
}

class Work implements Runnable{
    ExecutorService es;

    public Work(ExecutorService es) {
        this.es = es;
    }

    public void run() {
        int i =0;
        while(i<10){
            try {
                //Thread.sleep(1000);
                i++;
                es.execute(new Task(new Date()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class Task implements Runnable{

    Date date;

    public Task(Date date) {
        this.date = date;
    }

    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(date);
    }
}