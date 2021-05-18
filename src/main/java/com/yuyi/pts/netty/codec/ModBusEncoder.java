package com.yuyi.pts.netty.codec;

import com.alibaba.fastjson.JSONObject;
import com.yuyi.pts.common.util.ByteUtils;
import com.yuyi.pts.common.util.SpringUtils;
import com.yuyi.pts.model.protocol.ModBusMessage;
import com.yuyi.pts.model.vo.request.RequestDataDto;
import io.lettuce.core.StrAlgoArgs;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

/**
 * ModBus协议编码解码器
 *
 * @author greyson/wzl
 * @since 2021/4/16
 */
@Slf4j
@Component
public class ModBusEncoder extends MessageToByteEncoder<RequestDataDto> {


    private static ModBusMessage modBusMessage;
    static {
        modBusMessage = SpringUtils.getBean(ModBusMessage.class);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RequestDataDto requestDataDto, ByteBuf out) throws Exception {
        log.info("进入了modBus编码");
        // 业务标识符 两个字节 TODO 批量时考虑生成自增顺序号
        byte[] affairIdentification = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(
                        modBusMessage.getAffairIdentification()), 2);
        // 协议标识符 两个字节
        byte[] protocolIdentification = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(
                        modBusMessage.getProtocolIdentification()), 2);
        //  单元标识码  一个字节
        byte[] unitIdentification = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(modBusMessage.getUnitIdentification()), 1);

        // TODO 长度包含哪些值待确定 发送数据方式：  1.直接发送body  2. 构造后再发送 现在选择的方式是2 对数据有严格要求，对1的支持后续再处理
        Object body = requestDataDto.getBody();
        String jsonString = JSONObject.toJSONString(body);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //  功能码 一个字节
        String functionCode = (String) jsonObject.get("functionCode");
        functionCode = functionCode == null ? modBusMessage.getCode() : functionCode;
        byte[] code = ByteUtils.storeInBytes(
                ByteUtils.hexString2Bytes(functionCode), 1);
        //  写入数据    10进制转换成 16 进制
        String  startAddress = jsonObject.get("startAddress").toString();
        byte[] strAddressBytes = ByteUtils.shortToByte2(Short.valueOf(startAddress));
        byte[] strAddressByte = ByteUtils.storeInBytes(strAddressBytes,2);

        // 寄存器个数
        String  registerCount = jsonObject.get("registerCount").toString();
        byte[] registerCountStr=  ByteUtils.shortToByte2(Short.valueOf(registerCount));
        byte[] registerCountBytes = ByteUtils.storeInBytes(registerCountStr,2);

        //  长度两个字节 == 单元标识符一个字节 + 数据长度
        int length = code.length + strAddressBytes.length + registerCountBytes.length + 1;
        byte[] lengthBytes = ByteUtils.storeInBytes(
                ByteUtils.intToBytesLow(length), 2);
        // 注意！写入顺序不可调整
        out.writeBytes(affairIdentification);
        out.writeBytes(protocolIdentification);
        out.writeBytes(lengthBytes);
        out.writeBytes(unitIdentification);
        out.writeBytes(code);
        out.writeBytes(strAddressByte);
        out.writeBytes(registerCountBytes);
        if("0x10".equals(functionCode)){
            // 文本消息属性
            //  byte[] chars = new byte[16];
            // 第0位 综合监控控制取消播控区域文本内容   1 启动
            String  isOpen = jsonObject.get("isOpen").toString();
//            byte[] strAddressBytes = ByteUtils.shortToByte2(Short.valueOf(startAddress));
//            byte[] strAddressByte = ByteUtils.storeInBytes(strAddressBytes,2);
            //第1位 1 全屏播放文本  0 滚动播放文本
            String  showType = jsonObject.get("showType").toString();
            // 8-14  表示  1-127分钟
            String  showTime = Integer.toBinaryString(Integer.parseInt(jsonObject.get("showTime").toString()));
            if(showTime.length()<8){
                for (int i=showTime.length();i<7;i++){
                    showTime="0"+showTime;
                }
            }
            //第15位播放文本是否启用时限控制，0：不控制，1：启用时限控制  1000 0000 1000 0001 32897  0100000000000000 16384  1100 0000 1000 0001    49281
            String  limitStyle = jsonObject.get("limitStyle").toString();
            // 优先级
            String  priority = Integer.toBinaryString(Integer.parseInt(jsonObject.get("priority").toString()));
            if(priority.length()<4){
                for (int i=0;i<4-priority.length();i++){
                    priority="0"+priority;
                }
            }
            String request =  isOpen + showType + priority + "000" + showTime + limitStyle;
            short requestData = (short)  ByteUtils.binaryStringToInt(request);
            String  textCont = jsonObject.get("textCont").toString();
            ByteBuf byteBuf1 = Unpooled.buffer();
            byte[] bytes1 = ByteUtils.shortToByte2(requestData);
            byteBuf1.writeBytes(ByteUtils.storeInBytes(bytes1,2));
            // todo 暂时写死  后面在改
            byte[] num = new byte[2];
            ByteBuf byteBuf = Unpooled.buffer();
            // 文本消息内容
            byte[] bytesStr = ByteUtils.strToBytes(textCont);
            byteBuf1.writeBytes(ByteUtils.storeInBytes(bytesStr,250));

            for (int i = 0; i < 247; i++) {
                ByteUtils.hexString2Bytes("00"+i*10);
                byteBuf.writeBytes(num);
            }
            byte[] rnum = ByteUtils.hexString2Bytes("01");
            out.writeBytes(rnum);
            out.writeBytes(byteBuf1);
            out.writeBytes(byteBuf);
        }
    }
}
