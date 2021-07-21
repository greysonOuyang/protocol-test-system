package com.yuyi.pts.common.util;


import com.yuyi.pts.entity.CodecEntity;
import io.netty.channel.ChannelHandlerAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/21
 */
public class ReflectionUtil {
    public static Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    public static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    public static Class<ChannelHandlerAdapter> getCodec(String className) {
        Class<ChannelHandlerAdapter> codec = null;
        try {
            codec = (Class<ChannelHandlerAdapter>) Class.forName("com.yuyi.pts.netty.codec" + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return codec;
    }
}
