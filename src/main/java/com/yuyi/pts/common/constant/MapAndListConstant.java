package com.yuyi.pts.common.constant;

import com.yuyi.pts.model.excel.PlanInfoModel;
import com.yuyi.pts.model.vo.response.PlanInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wzl
 * @date : 2021/4/27/15:08
 * @description: 存储map 和 list数据
 */
public class MapAndListConstant {

    /**
     * 给模型赋值
     */
    public final static PlanInfo PLANINFO = getPlanInfo();
    public final static Map map = getPlanModel();
    /**
     * 写死数据
     * @return
     */
     public static PlanInfo getPlanInfo(){
         // 填充planInfo对象数据
         PlanInfo planInfo = new PlanInfo();
         // todo  给PlanInfo赋初始值
         PlanInfoModel model1 = new PlanInfoModel("1", "1", "1",null);
         PlanInfoModel model2 = new PlanInfoModel("2", "1", "1",null);
         PlanInfoModel model3 = new PlanInfoModel("3", "1", "1",null);
         PlanInfoModel model4 = new PlanInfoModel("4", "1", "1",null);
         PlanInfoModel model5 = new PlanInfoModel("5", "1", "1",null);
         PlanInfoModel model6 = new PlanInfoModel("6", "1", "1",null);
         PlanInfoModel model7 = new PlanInfoModel("7", "1", "1",null);
         PlanInfoModel model8 = new PlanInfoModel("8", "1", "1",null);
         PlanInfoModel model9 = new PlanInfoModel("9", "1", "1",null);
         List<PlanInfoModel> list = new ArrayList();
         list.add(model1);
         list.add(model2);
         list.add(model3);
         list.add(model4);
         list.add(model5);
         list.add(model6);
         list.add(model7);
         list.add(model8);
         list.add(model9);
         planInfo.setModelList(list);
         return planInfo;
     }

     public static Map getPlanModel(){
         Map map = new HashMap();
         map.put("a","名称");
         map.put("b","长度");
         map.put("c","数据类型");
         map.put("d","描述");
         return map;
     }
}
