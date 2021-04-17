//package com.yuyi.pts.common.util;
//
//
//import com.yuyi.pis.syslogapi.enums.ActionEnum;
//import com.yuyi.pis.syslogapi.enums.LOGLevel;
//import com.yuyi.pis.syslogapi.enums.MsgTypeEnum;
//import com.yuyi.pis.syslogapi.model.ServiceInfo;
//import com.yuyi.pis.syslogapi.model.SyslogFilter;
//import com.yuyi.pis.syslogapi.model.SyslogInfo;
//import com.yuyi.pis.syslogapi.util.SshUtils;
//import org.graylog2.syslog4j.Syslog;
//import org.graylog2.syslog4j.SyslogConfigIF;
//import org.graylog2.syslog4j.SyslogConstants;
//import org.graylog2.syslog4j.SyslogIF;
//
//import java.lang.management.ManagementFactory;
//import java.net.*;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.atomic.AtomicInteger;
//
//;
//
//
//public class SyslogAPI {
//
// //   private static SyslogIF SYSLOG_IF;
//    private static final String SEPARATOR = "\\u0019";
//    private static final String FORMATTER_PATTERN = "yyyy-MM-dd HH:mm:ss";
////    private static final String FORMATTER_PATTERN = "yyyyMMddHHmmss";
//
//    private static final ExecutorService THREAD_EXECUTOR = Executors.newSingleThreadExecutor(new ThreadFactory() {
//        private final AtomicInteger poolNumber = new AtomicInteger(1);//原子类，线程池编号
//
//        @Override
//        public Thread newThread(Runnable r) {
//            String namePrefix = "Syslog-thread-pool-" +
//                    poolNumber.getAndIncrement();
//            Thread t = new Thread(r, namePrefix);//真正创建线程的地方
//            return t;
//        }
//    });
//
//    static {
//        try {
//     //       String sysLogInfoStr = SystemApi.findSyslogInfo();
//    //        String syslogIp = sysLogInfoStr != null ? sysLogInfoStr.trim().split(",")[0].trim() : "";
//            SYSLOG_IF = initSyslogIF(syslogIp);
//        } catch (Exception e) {
//
//        }
//    }
//
//    private static SyslogIF initSyslogIF(String syslogIp) {
//        SyslogIF syslogIF = Syslog.getInstance(SyslogConstants.UDP);
//        //设置syslog服务器端地址
//        SyslogConfigIF config = syslogIF.getConfig();
//        config.setHost(syslogIp);
//        //设置syslog接收端口，默认514
//        config.setPort(514);
//        //Facility统一使用16，该数值为系统保留，这里用来表示是PIS的自研部分
//        config.setFacility(16);
//        //设置发送消息不切片
//        config.setTruncateMessage(false);
//        //发送消息最大长度
//        config.setMaxMessageLength(63488);
//        return syslogIF;
//    }
//
//   /* public static void main(String[] args) {
//        int i=0;
//        for (LOGLevel value : LOGLevel.values()) {
//            SyslogAPI.log("测试进程",null,value, MsgTypeEnum.OPT_LOG, "测试操作" + i, "SyslogProcess" + i,new Date(), ActionEnum.ADD, "opt1|opt2|opt3", "p1" + i, "p2" + i);
//            i++;
//        }
//        THREAD_EXECUTOR.shutdown();
//
//        Calendar instance = Calendar.getInstance();
//        instance.add(Calendar.DAY_OF_MONTH, -30);
//        Date stime = instance.getTime();
//
//        SyslogFilter syslogFilter = new SyslogFilter();
//        syslogFilter.setStartTime(stime);
//        syslogFilter.setPageNum(1);
//        syslogFilter.setPageSize(5000);
//        syslogFilter.setLogLevel(LOGLevel.INFO);
//        List<SyslogInfo> query = query(null, null, null, null, syslogFilter);
//        System.out.println(query);
//    }*/
//
//    /**
//     * 查询api
//     *
//     * @param syslogServerIp syslog服务端所在ip
//     * @param sshUser        syslog服务端ssh账户名
//     * @param sshPassword    syslog服务端ssh密码
//     * @param filter         过滤对象
//     * @return
//     */
//    public static List<SyslogInfo> query(String syslogServerIp, String sshUser, String sshPassword
//            , String logFileName, SyslogFilter filter) {
//        List<SyslogInfo> res = new ArrayList<>();
//        try {
//            if (isBlank(syslogServerIp)) {
//                Map<String, ServiceInfo> serviceInfo = SystemApi.getServiceInfo();
//                if (serviceInfo != null) {
//                    ServiceInfo syslog = serviceInfo.get("syslog");
//                    if (syslog != null) {
//                        syslogServerIp = syslog.getVmIp();
//                    }
//                }
//            }
//            if (isBlank(syslogServerIp)) {
//                return res;
//            }
//            if (isBlank(sshUser)) {
//                sshUser = "root";
//            }
//            if (isBlank(sshPassword)) {
//                sshPassword = "Admin@yuyi";
//            }
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATTER_PATTERN);
//            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(FORMATTER_PATTERN);
//            String startStr = simpleDateFormat.format(filter.getStartTime());
//            String endStr = simpleDateFormat.format(filter.getEndTime());
//
//            if (isBlank(logFileName)) {
//                logFileName = "*";
//            }
//
//            // linux上将文本行倒序输出
//            // PERL方法：
//            //perl -e 'print reverse <>' filename
//            //还是perl好用，perl处理文本真的很强悍。
//            String cmd = String.format("perl -e 'print reverse <>' /var/logs/%s | awk -F'\\\\\\\\u0019' '{if($2>=\"%s\" && $2<=\"%s\"){print $0}}'  ", logFileName, startStr, endStr);
//
//            if (isNotBlank(filter.getKeyword())) {
//                cmd += " | grep -E '" + filter.getKeyword() + "' ";
//            }
//
//            if (filter.getLogLevel() != null) {
//                cmd += " | grep '" + filter.getLogLevel().getStr() + "' ";
//            }
//
//            if (filter.getMsgTypeEnum() != null) {
//                cmd += " | grep '" + filter.getMsgTypeEnum().name() + "' ";
//            }
//            if (filter.getActionEnum() != null) {
//                cmd += " | grep '" + filter.getActionEnum().name() + "' ";
//            }
//
//            int pageNum = filter.getPageNum();
//            int pageSize = filter.getPageSize();
//            // 1 11 21
//            int offsetNum = 1 + ((pageNum - 1) * pageSize);
//            int endNum = offsetNum + pageSize - 1;
//            cmd += String.format(" | sed -n '%d,%dp' ", offsetNum, endNum);
//
//            List<String> resStrList = SshUtils.execCmd2List(syslogServerIp, sshUser, sshPassword, cmd);
//            for (String line : resStrList) {
//                String[] fields = line.split("\\" + SEPARATOR,9);
//                int len = fields.length;
//                if (len < 6) {
//                    continue;
//                }
//                //兼容少部分错误数据
//                if (len < 9) {
//                    int bu = 9 - len;
//                    for (int i = 0; i < bu; i++) {
//                        line += SEPARATOR;
//                    }
//                    fields = line.split("\\" + SEPARATOR, 9);
//                }
//                String[] levelAndFirstField = fields[0].split(" ",2);
//                String level = "INFO";
//                String tag = "";
//                if (levelAndFirstField.length == 2) {
//                    level = levelAndFirstField[0];
//                    tag = levelAndFirstField[1];
//                }
//                LOGLevel logLevel = LOGLevel.getLevelByString(level);
//                String time = fields[1];
//                String ip = fields[2];
//                String msgType = fields[3];
//                MsgTypeEnum msgTypeEnum = MsgTypeEnum.getMsgTypeByString(msgType);
//                String operator = fields[4];
//                String action = fields[5];
//                ActionEnum actionEnum = ActionEnum.getActionEnumByString(action);
//                String message = fields[6];
//                String optObj = fields[7];
//                List<String> optObjList = new ArrayList<>();
//                if (isNotBlank(optObj)) {
//                    optObjList.addAll(Arrays.asList(optObj.split("\\|")));
//                }
//                String param = fields[8];
//                List<String> paramList = new ArrayList<>();
//                if (isNotBlank(param)) {
//                    paramList.addAll(Arrays.asList(param.split(",")));
//                }
//                SyslogInfo syslogInfo = new SyslogInfo(
//                        logLevel, tag, simpleDateFormat2.parse(time)
//                        , ip, msgTypeEnum, operator, actionEnum, message, optObjList, paramList
//                );
//                res.add(syslogInfo);
//            }
//            Collections.sort(res, new Comparator<SyslogInfo>() {
//                @Override
//                public int compare(SyslogInfo o1, SyslogInfo o2) {
//                    Date time1 = o1.getTime();
//                    Date time2 = o2.getTime();
//                    if (time1 != null && time2 != null) {
//                        return time2.compareTo(time1);
//                    }
//                    return 0;
//                }
//            });
//            int i = 1;
//            for (SyslogInfo re : res) {
//                re.setId(String.valueOf(i++));
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return res;
//    }
//
//
//    private static void doRecord(SyslogIF syslog, String tag, String ip, LOGLevel level
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        try {
//            StringBuilder logString = new StringBuilder();
//            //月 日 时:分:秒 操作主机主机名 cpis/from_ip[操作主机ip]:日志级别:日志内容 [备注信息]
//
//            /*
//             *
//             * 日志文件，存储格式：
//             * [TAG] [TIME] [IP] [msgType] [operator] [action] [message] [optObj] [param]
//             * 字段值与字段值之间使用“\u0019”分割，param多个使用“,”分割，如
//             * security\u00192020-11-09 14:00:10\u0019192.168.2.18\u0019OPT_LOG\u00191001\u0019ADD\u00191001新增用户\u0019optObj\u0019param1,param2,param3,…
//             *
//             *
//             * */
//            SimpleDateFormat format = new SimpleDateFormat(FORMATTER_PATTERN);
//            String time = format.format(date);
//            if (isNotBlank(tag)) {
//                logString.append(tag).append(SEPARATOR);
//            } else {
//                String hostname = ManagementFactory.getRuntimeMXBean().getName().split("@")[1];
//                logString.append(hostname).append(SEPARATOR);
//            }
//            logString.append(isNotBlank(time) ? time : "").append(SEPARATOR);
//            if (isNotBlank(ip)) {
//                logString.append(ip).append(SEPARATOR);
//            } else {
//                logString.append(getInet4Address()).append(SEPARATOR);
//            }
//            logString.append(msgType != null ? msgType.name() : "").append(SEPARATOR);
//            logString.append(isNotBlank(operator) ? operator : "").append(SEPARATOR);
//            logString.append(action != null ? action.name() : "").append(SEPARATOR);
//            logString.append(isNotBlank(message) ? message : "").append(SEPARATOR);
//            logString.append(isNotBlank(optObj) ? optObj : "").append(SEPARATOR);
//            if (null != param && param.length > 0) {
//                if (!logString.toString().endsWith(SEPARATOR)) {
//                    logString.append(SEPARATOR);
//                }
//                //防止android端报错
//                for (String para : param) {
//                    logString.append(para).append(",");
//                }
//                logString.deleteCharAt(logString.lastIndexOf(","));
//
//            }
//            String log = logString.toString().replaceAll(":", "\\:");
//            syslog.log(level.getCode(), URLDecoder.decode(log, "utf-8"));
//            System.out.println(">>>>>>>>>>>>Syslog 日志记录成功！<<<<<<<<<<<< [" + log + "]");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(">>>>>>>>>>>>Syslog 日志记录失败！<<<<<<<<<<<<");
//        }
//    }
//
//    /**
//     * 日志记录
//     *
//     * @param tag      产生信息的程序名称
//     * @param ip       该日志产生的IP
//     * @param level    操作日志级别
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param date     该日志记录时间
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void log(String tag, String ip, LOGLevel level
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        THREAD_EXECUTOR.execute(() -> doRecord(SYSLOG_IF, tag, ip, level, msgType, message, operator, date, action, optObj, param));
//    }
//
//    /**
//     * 日志记录
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param ip             该日志产生的IP
//     * @param level          操作日志级别
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param date           该日志记录时间
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void log(String syslogServerIP, String tag, String ip, LOGLevel level
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        SyslogIF syslogIF = initSyslogIF(syslogServerIP);
//        THREAD_EXECUTOR.execute(() -> doRecord(syslogIF, tag, ip, level, msgType, message, operator, date, action, optObj, param));
//    }
//
//
//    /**
//     * 紧急日志
//     *
//     * @param tag      产生信息的程序名称
//     * @param ip       该日志产生的IP
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param date     该日志记录时间
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void emerg(String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(tag, ip, LOGLevel.EMERG, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 紧急日志
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param ip             该日志产生的IP
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param date           该日志记录时间
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void emerg(String syslogServerIP, String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(syslogServerIP, tag, ip, LOGLevel.EMERG, msgType, message, operator, date, action, optObj, param);
//    }
//
//
//    /**
//     * 日志默认级别为INFO
//     * ip自动获取，有可能不准
//     *
//     * @param tag      产生信息的程序名称
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void log(String tag
//            , MsgTypeEnum msgType, String message, String operator, ActionEnum action
//            , String optObj, String... param) {
//        info(tag, getInet4Address(), msgType, message, operator, new Date(), action, optObj, param);
//    }
//
//    /**
//     * 日志默认级别为INFO
//     * ip自动获取，有可能不准
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void log(String syslogServerIP, String tag
//            , MsgTypeEnum msgType, String message, String operator, ActionEnum action
//            , String optObj, String... param) {
//        info(syslogServerIP, tag, getInet4Address(), msgType, message, operator, new Date(), action, optObj, param);
//    }
//
//    /**
//     * 警告信息日志，必须及时改正
//     * LOG_ALERT:应该被立即改正的问题，如系统数据库被破坏，ISP连接丢失
//     *
//     * @param tag      产生信息的程序名称
//     * @param ip       该日志产生的IP
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param date     该日志记录时间
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void alert(String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(tag, ip, LOGLevel.ALERT, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 警告信息日志，必须及时改正
//     * LOG_ALERT:应该被立即改正的问题，如系统数据库被破坏，ISP连接丢失
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param ip             该日志产生的IP
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param date           该日志记录时间
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void alert(String syslogServerIP, String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(syslogServerIP, tag, ip, LOGLevel.ALERT, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 严重警告日志
//     * LOG_CRIT:重要情况，如硬盘错误，备用连接丢失。
//     *
//     * @param tag      产生信息的程序名称
//     * @param ip       该日志产生的IP
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param date     该日志记录时间
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void criti(String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(tag, ip, LOGLevel.CRITICAL, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 严重警告日志
//     * LOG_CRIT:重要情况，如硬盘错误，备用连接丢失。
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param ip             该日志产生的IP
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param date           该日志记录时间
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void criti(String syslogServerIP, String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(syslogServerIP, tag, ip, LOGLevel.CRITICAL, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 错误日志
//     * LOG_ERROR:错误，不是非常紧急，在一定时间内修复即可。
//     *
//     * @param tag      产生信息的程序名称
//     * @param ip       该日志产生的IP
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param date     该日志记录时间
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void error(String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(tag, ip, LOGLevel.ERROR, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 错误日志
//     * LOG_ERROR:错误，不是非常紧急，在一定时间内修复即可。
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param ip             该日志产生的IP
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param date           该日志记录时间
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void error(String syslogServerIP, String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(syslogServerIP, tag, ip, LOGLevel.ERROR, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 提示警告日志，需注意不一定要修改
//     * LOG_WARNING:警告信息，不是错误，比如系统磁盘使用了85%等。
//     *
//     * @param tag      产生信息的程序名称
//     * @param ip       该日志产生的IP
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param date     该日志记录时间
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void warning(String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(tag, ip, LOGLevel.WARNING, msgType, message, operator, date, action, optObj, param);
//    }
//
//
//    /**
//     * 提示警告日志，需注意不一定要修改
//     * LOG_WARNING:警告信息，不是错误，比如系统磁盘使用了85%等。
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param ip             该日志产生的IP
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param date           该日志记录时间
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void warning(String syslogServerIP, String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(syslogServerIP, tag, ip, LOGLevel.WARNING, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 通知日志
//     * LOG_NOTICE:不是错误情况，也不需要立即处理。
//     *
//     * @param tag      产生信息的程序名称
//     * @param ip       该日志产生的IP
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param date     该日志记录时间
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void notice(String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(tag, ip, LOGLevel.NOTICE, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 通知日志
//     * LOG_NOTICE:不是错误情况，也不需要立即处理。
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param ip             该日志产生的IP
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param date           该日志记录时间
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void notice(String syslogServerIP, String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(syslogServerIP, tag, ip, LOGLevel.NOTICE, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 普通信息日志
//     * LOG_INFO:情报信息，正常的系统消息，比如骚扰报告，带宽数据等，不需要处理。
//     *
//     * @param tag      产生信息的程序名称
//     * @param ip       该日志产生的IP
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param date     该日志记录时间
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void info(String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(tag, ip, LOGLevel.INFO, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 普通信息日志
//     * LOG_INFO:情报信息，正常的系统消息，比如骚扰报告，带宽数据等，不需要处理。
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param ip             该日志产生的IP
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param date           该日志记录时间
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void info(String syslogServerIP, String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(syslogServerIP, tag, ip, LOGLevel.INFO, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 调试日志
//     * LOG_DEBUG:包含详细的开发情报的信息，通常只在调试一个程序时使用。
//     *
//     * @param tag      产生信息的程序名称
//     * @param ip       该日志产生的IP
//     * @param msgType  消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message  操作描述
//     * @param operator 若没有具体用户，就是进程自身
//     * @param date     该日志记录时间
//     * @param action   就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj   操作对象(可能没有）
//     * @param param    其他参数
//     */
//    public static void debug(String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(tag, ip, LOGLevel.DEBUG, msgType, message, operator, date, action, optObj, param);
//    }
//
//    /**
//     * 调试日志
//     * LOG_DEBUG:包含详细的开发情报的信息，通常只在调试一个程序时使用。
//     *
//     * @param syslogServerIP syslog服务所在ip
//     * @param tag            产生信息的程序名称
//     * @param ip             该日志产生的IP
//     * @param msgType        消息种类（操作日志/播放日志/运行信息） OPT_LOG/PLAY_LOG/RUN_LOG
//     * @param message        操作描述
//     * @param operator       若没有具体用户，就是进程自身
//     * @param date           该日志记录时间
//     * @param action         就是进程自身动作 QUERY/ADD/DEL/UPDATE/OTHER
//     * @param optObj         操作对象(可能没有）
//     * @param param          其他参数
//     */
//    public static void debug(String syslogServerIP, String tag, String ip
//            , MsgTypeEnum msgType, String message, String operator, Date date, ActionEnum action
//            , String optObj, String... param) {
//        log(syslogServerIP, tag, ip, LOGLevel.DEBUG, msgType, message, operator, date, action, optObj, param);
//    }
//
//    public static String getInet4Address() {
//        Enumeration<NetworkInterface> nis;
//        String ip = null;
//        try {
//            nis = NetworkInterface.getNetworkInterfaces();
//            for (; nis.hasMoreElements(); ) {
//                NetworkInterface ni = nis.nextElement();
//                Enumeration<InetAddress> ias = ni.getInetAddresses();
//                for (; ias.hasMoreElements(); ) {
//                    InetAddress ia = ias.nextElement();
//                    if (ia instanceof Inet4Address && !"127.0.0.1".equals(ia.getHostAddress())) {
//                        ip = ia.getHostAddress();
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return ip;
//    }
//
//    private static boolean isNotBlank(String str) {
//        return str != null && !"".equals(str.trim());
//    }
//
//    private static boolean isBlank(String str) {
//        return str == null || "".equals(str.trim());
//    }
//}
