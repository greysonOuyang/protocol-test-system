package com.yuyi.pts.controller;



import com.yuyi.pts.model.client.ServiceInterfaceJDBC;
import com.yuyi.pts.service.impl.ParamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * description
 *
 * @author greyson
 * @since 2021/5/12
 */
@RestController
@RequestMapping("paramCtrl")
public class ParamController {
    @Autowired
   private ParamServiceImpl paramService;
    @PostMapping("/param/save")
    public void saveParamData(@RequestBody ServiceInterfaceJDBC serviceInterfaceJDBC) {
        paramService.insert(serviceInterfaceJDBC);
    }
}
