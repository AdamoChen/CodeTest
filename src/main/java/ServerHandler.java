import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class ServerHandler implements Runnable{

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean started = false;

    private static Map<SelectionKey, String> catchMap = Collections.synchronizedMap(new HashMap<>());

    public ServerHandler(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            started = true;
            System.out.println("服务器已启动，端口号：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        while (started){
            try {
                selector.select(1000);
                Set<SelectionKey>  keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey selectionKey = null;
                while(it.hasNext()){
                    selectionKey = it.next();
                    it.remove();
                    try {
                        handleInput(selectionKey);
                    } catch (Exception e) {
                        if(selectionKey != null){
                            selectionKey.cancel();
                            if (selectionKey.channel() != null){
                                selectionKey.channel().close();
                            }
                        }
                        System.out.println(e);
                    }
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

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if(selectionKey.isValid()){
            if(selectionKey.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }
            if(selectionKey.isReadable()){
                SocketChannel sc = (SocketChannel) selectionKey.channel();
                ByteBuffer buffer = ByteBuffer.allocate(EnumSize.size_10.size);
                int readByte = sc.read(buffer);
                if(readByte > 0){
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String requestStr = new String(bytes, "UTF-8");
                    String mapStr = catchMap.get(selectionKey);
                    if(mapStr != null){
                        requestStr = mapStr + requestStr;
                    }
                    catchMap.put(selectionKey, requestStr);

                    int index = requestStr.lastIndexOf("`");
                    if(index != -1){
                        String fullStr = requestStr.substring(0,index);
                        if(index+1 < requestStr.length()){
                            String noFullStr = requestStr.substring(index+1);
                            catchMap.put(selectionKey, noFullStr);
                        }else{
                            catchMap.put(selectionKey, null);
                        }
                        String[] strings = fullStr.split("`");
                        for (int i = 0; i < strings.length; i++) {
                            System.out.println("服务器接收到完整消息：" + strings[i]);
                            doWrite(sc, strings[i]);
                        }
                    }

                    //String responseStr = requestStr; // + " 是吧， 服务器端已收到。";
                    //doWrite(sc, responseStr);
                }else{
                    selectionKey.channel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String responseStr) throws IOException {
        byte[] bytes = responseStr.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        sc.write(buffer);
    }

    public void stop() {
        started = false;
    }

}
