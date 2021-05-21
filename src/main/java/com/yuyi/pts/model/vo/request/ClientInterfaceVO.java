package com.yuyi.pts.model.vo.request;

import com.yuyi.pts.common.enums.RequestType;
import com.yuyi.pts.model.client.ClientInterface;
import com.yuyi.pts.model.client.TInterfaceConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端请求VO
 *
 * @author greyson
 * @since 2021/5/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInterfaceVO {
    private RequestType requestType;
    private TInterfaceConfig tInterfaceConfig;
}
