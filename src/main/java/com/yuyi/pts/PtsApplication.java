package com.yuyi.pts;

import com.yuyi.pts.service.impl.NettyMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/9
 */
@SpringBootApplication
public class PtsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PtsApplication.class, args);
    }
}
