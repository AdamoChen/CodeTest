import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        try {
            Server.start();
            Thread.sleep(100);
            Client.start();
            while(Client.sendMsg(new Scanner(System.in).nextLine()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
