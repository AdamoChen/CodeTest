import java.util.Scanner;

public class Client {
    private final static String IP = "127.0.0.1";
    private final static int DEFAULT_PORT = 10086;

    private static ClientHandler clientHandler;

    public static void start(){
        start(IP, DEFAULT_PORT);
    }

    private static void start(String ip, int port){
        if(clientHandler != null){
            clientHandler.stop();
        }else{
            clientHandler = new ClientHandler(ip, port);
            new Thread(clientHandler, "client").start();
        }
    }

    public static boolean sendMsg(String msg) throws Exception {
        if("q".equalsIgnoreCase(msg)) return false;
        clientHandler.sendMsg(msg);
        return true;
    }

    public static void main(String[] args) throws Exception {
        start();
        while(Client.sendMsg(new Scanner(System.in).nextLine()));
    }
}
