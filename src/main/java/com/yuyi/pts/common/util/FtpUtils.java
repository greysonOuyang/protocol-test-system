package com.yuyi.pts.common.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FtpUtils {


    public ChannelSftp getSftp(Session session) {
        Channel channel = null;
        try {
            channel = session.openChannel("sftp");
            channel.connect();
            log.info("get Channel success!");
        } catch (JSchException e) {
            log.info("get Channel fail!", e);
        }
        return (ChannelSftp) channel;
    }

    public ChannelExec getExec(Session session) {
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            channel.connect();
            log.info("get Channel success!");
        } catch (JSchException e) {
            log.info("get Channel fail!", e);
        }
        ChannelExec channelExec = (ChannelExec) channel;
        return channelExec;
    }


    public Session getSession(String host, int port, String username,
                              final String password) {
        Session session = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();
            log.info("Session connected!");
        } catch (JSchException e) {
            log.info("get Channel failed!", e);
        }
        return session;
    }

    public void exec(Session session, String cmd) {
        try {
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");

            channelExec.setCommand(cmd);
            // 获取执行脚本可能出现的错误日志
            channelExec.setErrStream(System.err);
            //脚本执行结果输出，对于程序来说是输入流
            InputStream in = channelExec.getInputStream();
            // 5 秒执行管道超时
            channelExec.connect();

            // 从远程主机读取输入流，获得脚本执行结果
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    //执行结果打印到程序控制台
                    log.info(new String(tmp, 0, i));
                }
                if (channelExec.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    //获取退出状态，状态0表示脚本被正确执行
                    log.info("exit-status: "
                            + channelExec.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }

            channelExec.disconnect();

        } catch (IOException | JSchException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * 创建文件夹
     *
     * @param sftp
     * @param dir  文件夹名称
     */
    public void mkdir(ChannelSftp sftp, String dir) {
        try {
            sftp.mkdir(dir);
            System.out.println("创建文件夹成功！");
        } catch (SftpException e) {
            System.out.println("创建文件夹失败！");
            e.printStackTrace();
        }
    }

    /**
     * @param sftp
     * @param dir  上传目录
     * @param file 上传文件
     * @return
     */
    public String uploadFile(ChannelSftp sftp, String dir, File file) {
        String result = "";
        try {
            sftp.cd(dir);
            // File file = new File("D://34.txt"); //要上传的本地文件
            if (file != null) {
                sftp.put(new FileInputStream(file), file.getName());
                result = "上传成功！";
            } else {
                result = "文件为空！不能上传！";
            }
        } catch (Exception e) {
            log.info("上传失败！", e);
            result = "上传失败！";
        }
        return result;
    }

    /**
     * @param sftp
     * @param dir  上传目录
     * @param
     * @return
     */
    public String uploadFile(ChannelSftp sftp, String dir, File file, String fileName) {
        String result = "";
        try {
            sftp.cd(dir);
            // File file = new File("D://34.txt"); //要上传的本地文件
            if (file != null) {
                sftp.put(new FileInputStream(file), fileName);
                result = "上传成功！";
            } else {
                result = "文件为空！不能上传！";
            }
        } catch (Exception e) {
            log.info("上传失败！", e);
            result = "上传失败！";
        }
        return result;
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @param sftp
     */
    public String download(String directory, String downloadFile,
                           String saveFile, ChannelSftp sftp) {
        String result = "";
        try {
            sftp.cd(directory);
            sftp.get(downloadFile, saveFile);
            result = "下载成功！";
        } catch (Exception e) {
            result = "下载失败！";
            log.info("下载失败！", e);
            ;
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftp
     */
    public String delete(String directory, String deleteFile, ChannelSftp sftp) {
        String result = "";
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
            result = "删除成功！";
        } catch (Exception e) {
            result = "删除失败！";
            log.info("删除失败！", e);
        }
        return result;
    }

    private void closeChannel(Channel channel) {
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    private void closeSession(Session session) {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    public void closeAll(ChannelSftp sftp, Session session) {
        try {
            closeChannel(sftp);
            closeSession(session);
        } catch (Exception e) {
            log.info("closeAll", e);
        }
    }
}