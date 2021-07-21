package com.yuyi.pts.netty.initializer;

import com.yuyi.pts.common.constant.ConstantValue;
import com.yuyi.pts.common.util.ReflectionUtil;
import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.entity.InterfaceEntity;
import com.yuyi.pts.model.client.TInterfaceConfig;
import com.yuyi.pts.model.vo.InterfaceVo;
import com.yuyi.pts.model.vo.ProjectDto;
import com.yuyi.pts.netty.NettyClient;
import com.yuyi.pts.netty.codec.SmartCarDecoder14;
import com.yuyi.pts.netty.codec.SmartCarEncoder14;
import com.yuyi.pts.netty.handler.ChannelInactiveHandler;
import com.yuyi.pts.netty.handler.ProjectConfigHandler;
import com.yuyi.pts.netty.codec.ModBusDecoder;
import com.yuyi.pts.netty.codec.SmartCarEncoder;
import com.yuyi.pts.repository.CodecRepository;
import com.yuyi.pts.service.CodecService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

/**
 * 根据项目配置处理信息的处理器 可作为服务端、也可以作为客户端
 *
 * @author greyson
 * @since 2021/6/11
 */
@Component
@NoArgsConstructor
public class ProjectInitializer extends AbstractNettyInitializer<SocketChannel> {
    private ProjectDto projectDto;

    private NettyClient nettyClient;

    @Autowired
    CodecService codecService;

    public ProjectInitializer(ProjectDto projectDto, NettyClient nettyClient) {
        this.projectDto = projectDto;
        this.nettyClient = nettyClient;
    }

    public ProjectInitializer(ProjectDto projectDto) {
        this.projectDto = projectDto;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(
                ConstantValue.SERVER_READ_IDLE_TIME_OUT,
                ConstantValue.SERVER_WRITE_IDLE_TIME_OUT,
                ConstantValue.SERVER_ALL_IDLE_TIME_OUT,
                TimeUnit.SECONDS));
        String encoderId = projectDto.getProjectEntity().getEncoderId();
        String decoderId = projectDto.getProjectEntity().getDecoderId();
        pipeline.addLast(codecService.getOne(encoderId));
        pipeline.addLast(codecService.getOne(decoderId));
        if (ConstantValue.CLIENT.equals(projectDto.getMode())) {
            pipeline.addLast(new ChannelInactiveHandler(nettyClient));
        }
        pipeline.addLast(new ProjectConfigHandler(projectDto, projectDto.getMode()));
    }


}
