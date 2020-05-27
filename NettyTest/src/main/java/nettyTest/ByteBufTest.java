package nettyTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * @Author: Adamo_chen
 * @Date: 2019/11/10 12:09
 * @Version 1.0
 */
public class ByteBufTest {

    public static void main(String[] args) {
        ByteBuf b = Unpooled.buffer();
        //b.writeInt(257);
        //b.writeInt(1);
        //b.writeInt(1);

        b.writeByte(15);



        System.out.println(ByteBufUtil.hexDump(b));
        System.out.println(b.getInt(1));
        for (int i = 0; i < 16; i++) {
            System.out.print(b.getUnsignedByte(i) + ",");
            if((i+1)%4 == 0){
                System.out.println();
            }
        }

       // System.out.println(b.readableBytes());



    }
}
