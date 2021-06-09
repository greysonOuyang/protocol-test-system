package com.yuyi.pts.testServer.udpServer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

public class MulticastReceive {
    public static void main(String[] args) throws Exception {
        listener();
    }

    public static void listener() throws Exception{
        //组播地址
        InetAddress group = InetAddress.getByName("239.0.0.0");
        int port = 4321;
        //创建组播套接字
        MulticastSocket msr = null;
        try {
            msr = new MulticastSocket(port);
            //加入连接
            msr.joinGroup(group);
            byte[] buffer = new byte[8192];
            System.out.println("接收数据包启动！(启动时间: "+new Date()+")");
            while(true){
                //建立一个指定缓冲区大小的数据包
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                msr.receive(dp);
                String s = new String(dp.getData(),0,dp.getLength());
                //解码组播数据包
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(msr!=null){
                try {
                    msr.leaveGroup(group);
                    msr.close();
                } catch (Exception e2) {
                }
            }
        }
    }
}