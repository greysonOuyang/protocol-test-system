package com.yuyi.pts.testServer;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.yuyi.pts.common.util.FtpUtils;

import java.io.File;

/**
 * description
 *
 * @author greyson
 * @since 2021/9/22
 */
public class UploadExcel {


    public static void main(String[] args) {
        File file = new File("D:/2DevProject/4_yuyi/pis_central/iscs-interface-server/target/iscs-interface-server-V300R001C00B28SP03.jar");
        File file2 = new File("D:/2DevProject/4_yuyi/pis_central/iscs-interface-server/src/main/resources/static/playctrl_map_code.xlsx");
        String[] ipPool = new String[] {"10.75.48.146", "10.75.52.146", "10.75.53.146","10.75.54.146","10.75.55.146",
                "10.75.56.146", "10.75.57.146", "10.91.48.146", "10.91.50.146", "10.91.51.146","10.75.1.171", "10.91.2.171"};
        for (int i = 0; i < ipPool.length; i++) {
            uploadIscs(ipPool[i], file, file2);
        }
    }

    public static void uploadIscs(String ip, File file, File file3) {
        FtpUtils ft = new FtpUtils();
        String password = "GZdt@2021";
        if ("171".equals(ip.substring(ip.length() - 3))) {
            password = "Aa@123456";
        }
        Session s = ft.getSession(ip,
                22, "root",
                password);
        ChannelSftp sftp = ft.getSftp(s);
//        System.out.println("开始上传到: " + ip);
//        String upload = ft.uploadFile(sftp,"/root/integrated_monitor/server",file, "iscs-interface-server.jar");
//        System.out.println("上传完毕:" + upload);
        String upload2 = ft.uploadFile(sftp,"/opt", file3, "playctrl_map_code.xlsx");
        System.out.println("上传完毕:" + upload2);
        if (upload2.contains("上传成功")) {
            ft.exec(s, "sh /root/integrated_monitor/server/shutdown.sh");
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ft.exec(s, "sh /root/integrated_monitor/server/startup.sh");
        }
        ft.closeAll(sftp, s);
    }
}
