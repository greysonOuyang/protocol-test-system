package com.yuyi.pts.netty.initializer;

import com.yuyi.pts.common.constant.ConstantValue;
import com.yuyi.pts.entity.ProjectEntity;
import com.yuyi.pts.model.vo.InterfaceVo;
import com.yuyi.pts.model.vo.request.RequestVo;
import com.yuyi.pts.netty.NettyClient;
import com.yuyi.pts.netty.handler.ChannelInactiveHandler;
import com.yuyi.pts.netty.handler.ProjectConfigHandler;
import com.yuyi.pts.repository.ProjectRepository;
import com.yuyi.pts.service.CodecService;
import com.yuyi.pts.service.ProjectService;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private RequestVo requestVo;

    private NettyClient nettyClient;

    @Autowired
    CodecService codecService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectService projectService;

    public ProjectInitializer(RequestVo requestVo, NettyClient nettyClient) {
        this.requestVo = requestVo;
        this.nettyClient = nettyClient;
    }

    public ProjectInitializer(RequestVo requestVo) {
        this.requestVo = requestVo;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(
                ConstantValue.SERVER_READ_IDLE_TIME_OUT,
                ConstantValue.SERVER_WRITE_IDLE_TIME_OUT,
                ConstantValue.SERVER_ALL_IDLE_TIME_OUT,
                TimeUnit.SECONDS));
        if (requestVo.getDataMode() != null) {
            // 接口模式
            if (ConstantValue.INTERFACE.equals(requestVo.getDataMode())) {
                InterfaceVo interfaceVo = projectService.findBy(requestVo.getInterfaceId());
                interfaceVo.setStartMode(requestVo.getStartMode());
                ProjectEntity projectEntity = projectRepository.getOne(interfaceVo.getProjectId());
                // 选择编码、解码器
                Integer encoderId = projectEntity.getEncoderId();
                Integer decoderId = projectEntity.getDecoderId();
                pipeline.addLast(codecService.getOne(encoderId));
                pipeline.addLast(codecService.getOne(decoderId));
                if (ConstantValue.CLIENT.equals(interfaceVo.getStartMode())) {
                    pipeline.addLast(new ChannelInactiveHandler(nettyClient));
                }
                pipeline.addLast(new ProjectConfigHandler(interfaceVo, interfaceVo.getStartMode()));
            } else if (ConstantValue.CONTEXT.equals(requestVo.getDataMode())) {
                pipeline.addLast(new ProjectConfigHandler(interfaceVo, interfaceVo.getStartMode()));
            }
        }
    }


}
