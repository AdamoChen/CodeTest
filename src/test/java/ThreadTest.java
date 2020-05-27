import com.sun.xml.internal.ws.util.ASCIIUtility;

/**
 * @Author: Adamo_chen
 * @Date: 2018/10/5 15:37
 * @Version 1.0
 */
public class ThreadTest extends Thread{

    private Object obj = new Object();
    private Object o = new Object();

    public static void main(String[] args) throws Exception {
        ThreadTest t = new ThreadTest();

        t.start();
        Thread.sleep(1000);
        t.p1();
    }


    @Override
    public void run() {
        try {
            p1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void p1() throws Exception {

        synchronized (obj) {
            for (int i = 1; i < 53; i++) {
                System.out.println(i);
                System.out.println(++i);
                obj.notify();
                obj.wait();

            }
        }
    }

    public void pp() throws Exception {
        synchronized (obj) {
            for (char i = 'a'; i <= 'z'; i++) {
                System.out.println(i);
                obj.notify();
                obj.wait();
            }
        }
    }
}
