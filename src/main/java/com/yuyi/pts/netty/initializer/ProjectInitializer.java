package com.yuyi.pts.netty.initializer;

import com.yuyi.pts.common.constant.ConstantValue;
import com.yuyi.pts.controller.ProjectController;
import com.yuyi.pts.entity.ProjectEntity;
import com.yuyi.pts.model.vo.InterfaceVo;
import com.yuyi.pts.netty.NettyClient;
import com.yuyi.pts.netty.handler.ChannelInactiveHandler;
import com.yuyi.pts.netty.handler.ProjectConfigHandler;
import com.yuyi.pts.repository.ProjectRepository;
import com.yuyi.pts.service.CodecService;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
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
    private InterfaceVo interfaceVo;

    private NettyClient nettyClient;

    @Autowired
    CodecService codecService;

    @Autowired
    ProjectRepository projectRepository;

    public ProjectInitializer(InterfaceVo interfaceVo, NettyClient nettyClient) {
        this.interfaceVo = interfaceVo;
        this.nettyClient = nettyClient;
    }

    public ProjectInitializer(InterfaceVo interfaceVo) {
        this.interfaceVo = interfaceVo;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(
                ConstantValue.SERVER_READ_IDLE_TIME_OUT,
                ConstantValue.SERVER_WRITE_IDLE_TIME_OUT,
                ConstantValue.SERVER_ALL_IDLE_TIME_OUT,
                TimeUnit.SECONDS));
        ProjectEntity projectEntity = projectRepository.getOne(interfaceVo.getProjectId());
        Integer encoderId = projectEntity.getEncoderId();
        Integer decoderId = projectEntity.getDecoderId();
        pipeline.addLast(codecService.getOne(encoderId));
        pipeline.addLast(codecService.getOne(decoderId));
        if (ConstantValue.CLIENT.equals(interfaceVo.getMode())) {
            pipeline.addLast(new ChannelInactiveHandler(nettyClient));
        }
        pipeline.addLast(new ProjectConfigHandler(interfaceVo, interfaceVo.getMode()));
    }


}
