package nettyTest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: Adamo_chen
 * @Date: 2019/10/20 22:12
 * @Version 1.0
 */
public class TimeDecoder extends ByteToMessageDecoder {
   /* protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() < 4){
            return;
        }
        out.add(in.readBytes(4));
    } */

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() < 4){
            return;
        }
        // pojo
        out.add(new UnixTime(in.readUnsignedInt()));
    }
}
