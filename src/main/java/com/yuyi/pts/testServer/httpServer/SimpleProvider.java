package com.yuyi.pts.testServer.httpServer;

import org.springframework.web.bind.annotation.*;
/**
 * @author : wzl
 * @date   : 2021/3/24/16:19
 * @description: http服务测试
 */

@RestController
public class SimpleProvider {

    @GetMapping("/simple_FinishRegister")
    public String simpleFinishRegister(@RequestBody String json) {
        System.out.println("test-------"+json);
       return  "hello";
    }
    @PutMapping("/simple_FinishRegister")
    public String simpleFinishRegisterPut(@RequestBody String json) {
        System.out.println("test-------"+json);
        return  "hello";
    }
    @PostMapping("/simple_FinishRegister")
    public String simpleFinishRegisterPost(@RequestBody String json) {
        System.out.println("test-------"+json);
        return  "hello";
    }
    @DeleteMapping ("/simple_FinishRegister")
    public String simpleFinishRegisterDelete(@RequestBody String json) {
        System.out.println("test-------"+json);
        return  "hello";
    }

}
