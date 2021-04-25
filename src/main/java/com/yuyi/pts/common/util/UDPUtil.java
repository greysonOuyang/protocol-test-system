package com.yuyi.pts.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;

@Component
public class UDPUtil {
    private static Logger logger = LoggerFactory.getLogger("UDPUtil");


    public static void multicastSend(byte[] data) {
        try {
            MulticastSocket socket = new MulticastSocket(9090);
            // 设置组播组的地址为localhost
            InetAddress group = InetAddress.getByName("127.0.0.1");
            //加入 组播组
            socket.joinGroup(group);
            // 存储在数组中
            // 初始化DatagramPacket
            DatagramPacket packet = new DatagramPacket(data, data.length, group,9090);
            // 通过MulticastSocket实例端口向组播组发送数据
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLogger(Logger logger) {
        UDPUtil.logger = logger;
    }
}
