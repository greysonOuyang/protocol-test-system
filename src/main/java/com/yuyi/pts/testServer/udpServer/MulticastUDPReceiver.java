////package com.yuyi.pts.testServer.udpServer;
//
//import java.net.DatagramPacket;
//import java.net.InetAddress;
//import java.net.MulticastSocket;
//
///**
// * 接收组播
// *
// * @author greyson
// * @since 2021/6/9
// */
//public class MulticastUDPReceiver {
//    public static void main(String[] args) throws Exception {
//        // System.setProperty("java.net.preferIPv4Stack", "true");
//
//        int mcPort = 4321;
//        String mcIPStr = "238.1.1.1";
//        MulticastSocket mcSocket = null;
//        InetAddress mcIPAddress = null;
//        mcIPAddress = InetAddress.getByName(mcIPStr);
//        mcSocket = new MulticastSocket(mcPort);
//        System.out.println("Multicast Receiver running at:" + mcSocket.getLocalSocketAddress());
//        mcSocket.joinGroup(mcIPAddress);
//
//        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
//
//        System.out.println("Waiting for a  multicast message...");
//        for (int i = 0; i < 5000; i++) {
//            mcSocket.receive(packet);
//            String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
//            System.out.println("[Multicast  Receiver] Received:" + msg);
//        }
//        mcSocket.leaveGroup(mcIPAddress);
//        mcSocket.close();
//    }
//}
