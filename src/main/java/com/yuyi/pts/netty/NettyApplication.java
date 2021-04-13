package com.yuyi.pts.netty;
import com.yuyi.pts.config.ProtocolConfig;
import com.yuyi.pts.service.impl.NettyMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 测试启动NettyClient，代替的是web页面点击连接的功能
 *
 * @author greyson
 * @since 2021/4/11
 */
@Component
public class NettyApplication implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private ProtocolConfig protocolConfig;

    @Autowired
    public NettyMessageServiceImpl messageService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if(contextRefreshedEvent.getApplicationContext().getParent() == null){
            try {
                //开启WebSocket服务
                int i = messageService.hashCode();
                System.out.println(i);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

