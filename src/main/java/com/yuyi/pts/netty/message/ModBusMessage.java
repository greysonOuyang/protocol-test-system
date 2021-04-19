package com.yuyi.pts.netty.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author greyson
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModBusMessage {


    private ModBusMessageHeader header;
    private ModBusMessageBody body;


}
