public class Server {
    private static final int DEFAULT_PORT = 10086;
    private static ServerHandler serverHandler;

    public static void start(){
        start(DEFAULT_PORT);
    }

    private static void start(int port){
        if(serverHandler != null){
            serverHandler.stop();
        }else{
            serverHandler = new ServerHandler(port);
            new Thread(serverHandler, "server").start();
        }
    }

    public static void main(String[] args) {
        start();
    }
}
