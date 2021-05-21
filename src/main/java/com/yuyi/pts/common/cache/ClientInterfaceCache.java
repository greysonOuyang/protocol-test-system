package com.yuyi.pts.common.cache;

import com.yuyi.pts.model.client.ClientInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * description
 *
 * @author greyson
 * @since 2021/5/19
 */
public class ClientInterfaceCache {
    public static Map<String, ClientInterface> HTTP_INTERFACE_MAP = new HashMap<>();
    public static Map<String, ClientInterface> WEBSOCKET_INTERFACE_MAP = new HashMap<>();
    public static Map<String, ClientInterface> TCP_INTERFACE_MAP = new HashMap<>();
    public static Map<String, ClientInterface> UDP_INTERFACE_MAP = new HashMap<>();

}
