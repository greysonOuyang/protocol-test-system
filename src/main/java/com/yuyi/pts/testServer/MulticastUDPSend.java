package com.yuyi.pts.testServer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

/**
 * 接收组播
 *
 * @author greyson
 * @since 2021/6/9
 */
public class MulticastUDPSend {
    public static void main(String[] args) throws Exception {
        // System.setProperty("java.net.preferIPv4Stack", "true");

        int mcPort = 4322;
        String mcIPStr = "238.1.1.128";
        MulticastSocket mcSocket = null;
        InetAddress mcIPAddress = null;
        mcIPAddress = InetAddress.getByName(mcIPStr);
        mcSocket = new MulticastSocket(mcPort);
        System.out.println("Multicast Receiver running at:" + mcSocket.getLocalSocketAddress());
        mcSocket.joinGroup(mcIPAddress);
        String message = "组播下发数据";
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

        mcSocket.leaveGroup(mcIPAddress);
        mcSocket.close();
    }
}
