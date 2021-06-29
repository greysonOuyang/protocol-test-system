package com.yuyi.pts.testServer.udpServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 单播接收
 *
 * @author greyson
 * @since 2021/6/28
 */
public class UdpSingleReceive {
    public static void main(String[] args)throws IOException{
        //新建一个socket绑定8888端口
        DatagramSocket  server = new DatagramSocket(10010);
        System.out.println("启动在10010端口，等待接收数据。。。");
        //接收消息
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket  = new DatagramPacket(recvBuf , recvBuf.length);
        server.receive(recvPacket);
        String recvStr = new String(recvPacket.getData() , 0 , recvPacket.getLength());
        System.out.println("接收到数据：" + recvStr);
    }
}
