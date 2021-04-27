package com.yuyi.pts.model.vo.response;


import com.yuyi.pts.model.excel.PlanInfoModel;

import java.util.List;

/**
 * @author : wzl
 * @date : 2021/4/27/11:47
 * @description: 计划信息模型
 */

public class PlanInfo {
      private List<PlanInfoModel> modelList;

    public List<PlanInfoModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<PlanInfoModel> modelList) {
        this.modelList = modelList;
    }
}
