package com.yuyi.pts.model.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description
 *
 * @author greyson
 * @since 2021/9/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IscsRequest {
    /**
     * 车站列表
     */
    private List<String> stationIds;

    /**
     * 是否为中心服务
     */
    private boolean isCenter;

    /**
     * jar包路径
     */
    private String jarPath;

    /**
     * 点表路径
     */
    private String excelPath;

    private String command;
}
