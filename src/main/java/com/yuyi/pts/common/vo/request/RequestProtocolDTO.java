package com.yuyi.pts.common.vo.request;

import com.yuyi.pts.common.constant.ConstanValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

/**
 * 自定义协议类的封装
 *
 * @author wzl
 * @since 2021/4/16
 */

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
public class RequestProtocolDTO {

    /**
     * 消息头
     */
    private short head_data = ConstanValue.HEED_DATA;
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
    private short data_len;
    /**
     * 主备机状态
     */
    @Value("${user.protocol.status}")
    private int status;
    /**
     * 消息种类
     */
    @Value("${user.protocol.type}")
    private int type;
    /**
     * 消息内容
     */
    private byte[] content;
    /**
     * 消息尾
     */
    private short data_tail = ConstanValue.TAIL_DATA;

    public RequestProtocolDTO(int total, int index, short data_len, int type, byte[] content) {
        this.total = total;
        this.index = index;
        this.data_len = data_len;
        this.type = type;
        this.content = content;
    }

    public short getHead_data() {
        return head_data;
    }

    public void setHead_data(short head_data) {
        this.head_data = head_data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public short getData_len() {
        return data_len;
    }

    public void setData_len(short data_len) {
        this.data_len = data_len;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public short getData_tail() {
        return data_tail;
    }

    public void setData_tail(short data_tail) {
        this.data_tail = data_tail;
    }

    @Override
    public String toString() {
        return "RequestProtocolDTO{" +
                "head_data=" + head_data +
                ", total=" + total +
                ", index=" + index +
                ", data_len=" + data_len +
                ", status=" + status +
                ", type=" + type +
                ", content=" + Arrays.toString(content) +
                ", data_tail=" + data_tail +
                '}';
    }
}
