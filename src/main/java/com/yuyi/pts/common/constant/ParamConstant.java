package com.yuyi.pts.common.constant;

import com.yuyi.pts.common.enums.FieldType;
import com.yuyi.pts.model.server.Param;
import com.yuyi.pts.model.server.ServiceInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wzl
 * @date : 2021/4/29/15:21
 * @description:
 */
public class ParamConstant {
    /**
     * 给模型赋值  输入  输出  模型
     */

    public final static ServiceInterface INPUT_PARAM = getInputParam();

    public final static ServiceInterface OUTPUT_PARAM = getOutputParam();

    public final static ServiceInterface SERVICE_INTERFACE = getServiceInterface();

    public static ServiceInterface getInputParam(){
        // 填充serviceInterface对象数据
        ServiceInterface serviceInterface = new ServiceInterface();
        Param model1 = new Param(1, 1, "1","1", FieldType.Hex);
        Param model2 = new Param(4, 1, "1","1", FieldType.Hex);
        Param model3 = new Param(7, 1, "1","1", FieldType.Hex);
        Param model4 = new Param(8, 1, "1","1", FieldType.Hex);
        Param model5 = new Param(12, 1, "1","1", FieldType.Hex);
        Param model6 = new Param(54, 1, "1","1", FieldType.Hex);
        Param model7 = new Param(112, 1, "1","1", FieldType.Hex);
        Param model8 = new Param(101, 1, "1","1", FieldType.Hex);
        Param model9 = new Param(45, 1, "1","1", FieldType.Hex);
        List<Param> list = new ArrayList();
        list.add(model1);
        list.add(model2);
        list.add(model3);
        list.add(model4);
        list.add(model5);
        list.add(model6);
        list.add(model7);
        list.add(model8);
        list.add(model9);
        serviceInterface.setInput(list);
        return serviceInterface;
    }
    public static ServiceInterface getOutputParam(){
        // 填充serviceInterface对象数据
        ServiceInterface serviceInterface = new ServiceInterface();
        Param model1 = new Param(1, 1, "1","1", FieldType.Hex);
        Param model2 = new Param(5, 1, "1","1", FieldType.Hex);
        Param model3 = new Param(8, 1, "1","1", FieldType.Hex);
        Param model4 = new Param(11, 1, "1","1", FieldType.Hex);
        Param model5 = new Param(18, 1, "1","1", FieldType.Hex);
        Param model6 = new Param(21, 1, "1","1", FieldType.Hex);
        Param model7 = new Param(31, 1, "1","1", FieldType.Hex);
        Param model8 = new Param(101, 1, "1","1", FieldType.Hex);
        Param model9 = new Param(91, 1, "1","1", FieldType.Hex);
        List<Param> list = new ArrayList();
        list.add(model1);
        list.add(model2);
        list.add(model3);
        list.add(model4);
        list.add(model5);
        list.add(model6);
        list.add(model7);
        list.add(model8);
        list.add(model9);
        serviceInterface.setOutput(list);
        return serviceInterface;
    }
    public static Map getModel(){
        Map map = new HashMap();
        map.put("a","写入下标");
        map.put("b","写入字节长度");
        map.put("c","参数名称");
        map.put("d","参数值");
        map.put("e","参数类型");
        return map;
    }
    private static ServiceInterface getServiceInterface() {
        ServiceInterface serviceInterface = new ServiceInterface();
        serviceInterface.setInput(INPUT_PARAM.getInput());
        serviceInterface.setOutput(OUTPUT_PARAM.getOutput());
        return serviceInterface;
    }
}
