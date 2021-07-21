package com.yuyi.pts.service.impl;

import com.yuyi.pts.common.util.ReflectionUtil;
import com.yuyi.pts.entity.CodecEntity;
import com.yuyi.pts.repository.CodecRepository;
import com.yuyi.pts.service.CodecService;
import io.netty.channel.ChannelHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/21
 */
@Component
public class CodecServiceImpl implements CodecService {

    @Autowired
    CodecRepository codecRepository;

    @Override
    public ChannelHandlerAdapter getOne(String codecId) {
        CodecEntity encoder = codecRepository.getOne(codecId);
        Class<ChannelHandlerAdapter> codec = ReflectionUtil.getCodec(encoder.getCodecName());
        try {
            return codec.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
