package com.yuyi.pts.service;

import io.netty.channel.ChannelHandlerAdapter;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/21
 */
@Service
public interface CodecService {
    ChannelHandlerAdapter getOne(Integer codecId);
}
