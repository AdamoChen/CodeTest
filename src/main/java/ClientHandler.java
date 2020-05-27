import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ClientHandler implements Runnable{

    private static String ip;
    private static int port;
    private static Selector selector;
    private static SocketChannel socketChannel;
    private volatile static boolean started = false;

    public ClientHandler(String ip, int port){
        this.ip = ip;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(ip, port));
            //不能用此方法
//            socketChannel.bind(new InetSocketAddress(ip, port));
//            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void stop() {
        started = false;
    }

    public void run() {
//        try {
//            doConnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        while(started){
            try {
                selector.select(1000);
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> it = keySet.iterator();
                while (it.hasNext()){
                    SelectionKey key = it.next();
                    it.remove();
                    handler(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handler(SelectionKey key) throws IOException {
        if(key.isValid()){
            SocketChannel sc = (SocketChannel)key.channel();
            // ##
//            if(key.isConnectable()){
//                if(sc.finishConnect());
//                else System.exit(1);
//            }
            if(key.isReadable()){
                ByteBuffer buffer = ByteBuffer.allocate(EnumSize.size_1024.size);
                int readBytes = sc.read(buffer);
                if(readBytes > 0){
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String requestStr = new String(bytes, "UTF-8");
                    System.out.println("客户端收到消息：" + requestStr);
                }else{
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

//    private void doConnect() throws IOException {
        // ##
//        if(socketChannel.connect(new InetSocketAddress(ip, port))){
//            System.out.println("socketChannel.connect(...)");
//        }
//        else {

//        }
//    }

    public void sendMsg(String msg) throws Exception {
//        socketChannel.register(selector, SelectionKey.OP_READ);
        dowrite(socketChannel, msg);
    }

    private void dowrite(SocketChannel socketChannel, String msg) throws IOException {
        byte[] bytes = msg.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        socketChannel.write(buffer);
    }
}
