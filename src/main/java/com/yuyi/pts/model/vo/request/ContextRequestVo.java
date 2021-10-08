package com.yuyi.pts.model.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文本下发请求VO
 *
 * @author greyson
 * @since 2021/9/25
 */
@NoArgsConstructor
@Data
public class ContextRequestVo {


    @JsonProperty("stationId")
    private String stationId;
    @JsonProperty("interfaceSelection")
    private String interfaceSelection;
    @JsonProperty("context")
    private String context;
    @JsonProperty("playerSelection")
    private String playerSelection;
    @JsonProperty("stationList")
    private String stationList;
    @JsonProperty("startAddress")
    private Object startAddress;
    @JsonProperty("endAddress")
    private Object endAddress;
    @JsonProperty("attribute")
    private AttributeDTO attribute;

    @NoArgsConstructor
    @Data
    public static class AttributeDTO {
        @JsonProperty("isOpen")
        private String isOpen;
        @JsonProperty("showType")
        private String showType;
        @JsonProperty("limitStyle")
        private String limitStyle;
        @JsonProperty("priority")
        private String priority;
        @JsonProperty("showTime")
        private String showTime;
    }
}
