package com.yuyi.pts.netty.initializer;

import com.yuyi.pts.common.codec.ModBus2Decoder;
import com.yuyi.pts.common.codec.ModBus2Encoder;
import com.yuyi.pts.common.enums.ProtocolType;
import com.yuyi.pts.netty.handler.AtsReceiveHandler;
import com.yuyi.pts.netty.handler.TcpServerHandler;
import io.netty.channel.socket.SocketChannel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * description
 *
 * @author greyson
 * @since 2021/6/25
 */
@Slf4j
@NoArgsConstructor
public class TcpServerInitializer extends AbstractNettyInitializer<SocketChannel> {

    /**
     * 基于TCP的应用层协议
     */
    private ProtocolType protocolType;

    private String business;

    public TcpServerInitializer(String business) {
        this.business = business;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        if ("ats_receive".equals(business)) {
            channel.pipeline().addLast(new AtsReceiveHandler());
        } else {
            channel.pipeline().addLast(
                    new ModBus2Decoder(),
                    new ModBus2Encoder(),
                    new TcpServerHandler()
            );
        }

    }
}
