package com.yuyi.pts.model.protocol;

import com.yuyi.pts.common.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 自定义协议类的封装
 * 消息体字节大小：其中data_head为帧头，两个字节，固定为0xEF,0xEF
 * Total:总帧数（1-32），最大定义32个分包；
 * Index:当前帧（1-32），对于长度超过后面data限制的应用数据，需要分包发送和接收，只有在收到index=total时，将前面的帧的data合并在一起进行处理；
 * data_len 表示后接的应用数据（data部分）的长度,两个字节，低字节在前，高字节在后
 * Dev_Status表示设备当前主/备机状态，0x01:当前为主机，0x02:当前为备机；
 * data表示发送的应用数据，长度最长为2048字节，对于分包处理，最大长度为2048*32字节
 * data部分：Type(2b)content((n-2)b)Type：消息种类，2BYTE，低位在前Content：具体消息内容，根据不同消息种类不同而不同，长度(n-2)b；；
 * data_tail为帧尾，两个字节，固定为0xFD,0xFD
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Component
public class AtsMessage {

    /**
     * 消息头
     */
    private short dataHead = Constant.HEED_DATA;
    /**
     * 总帧数
     */
    @Value("${user.protocol.total}")
    private int total;
    /**
     * 当前帧
     */
    @Value("${user.protocol.index}")
    private int index;
    /**
     * 数据长度
     */
    @Value("${user.protocol.datalength}")
    private short dataLength;
    /**
     * 主备机状态
     */
    @Value("${user.protocol.status}")
    private String deviceStatus;
    /**
     * 消息种类
     */
    @Value("${user.protocol.type}")
    private String type;
    /**
     * 消息内容
     */
    private byte[] content;
    /**
     * 消息尾
     */
    private short headTail = Constant.TAIL_DATA;

}


