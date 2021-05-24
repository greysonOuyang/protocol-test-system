package com.yuyi.pts.model.client;

import java.io.Serializable;
import lombok.Data;

/**
 * t_interface_config
 * @author 
 */
@Data
public class TInterfaceConfig implements Serializable {
    private Integer keyId;

    /**
     * 唯一标识符
     */
    private String interfaceConfigId;

    /**
     * 请求名称
     */
    private String requestName;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求内容
     */
    private String content;

    /**
     * 端口号
     */
    private Integer port;

    private Integer newColumn;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TInterfaceConfig other = (TInterfaceConfig) that;
        return (this.getKeyId() == null ? other.getKeyId() == null : this.getKeyId().equals(other.getKeyId()))
            && (this.getInterfaceConfigId() == null ? other.getInterfaceConfigId() == null : this.getInterfaceConfigId().equals(other.getInterfaceConfigId()))
            && (this.getRequestName() == null ? other.getRequestName() == null : this.getRequestName().equals(other.getRequestName()))
            && (this.getRequestType() == null ? other.getRequestType() == null : this.getRequestType().equals(other.getRequestType()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getRequestMethod() == null ? other.getRequestMethod() == null : this.getRequestMethod().equals(other.getRequestMethod()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getPort() == null ? other.getPort() == null : this.getPort().equals(other.getPort()))
            && (this.getNewColumn() == null ? other.getNewColumn() == null : this.getNewColumn().equals(other.getNewColumn()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getKeyId() == null) ? 0 : getKeyId().hashCode());
        result = prime * result + ((getInterfaceConfigId() == null) ? 0 : getInterfaceConfigId().hashCode());
        result = prime * result + ((getRequestName() == null) ? 0 : getRequestName().hashCode());
        result = prime * result + ((getRequestType() == null) ? 0 : getRequestType().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getRequestMethod() == null) ? 0 : getRequestMethod().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getPort() == null) ? 0 : getPort().hashCode());
        result = prime * result + ((getNewColumn() == null) ? 0 : getNewColumn().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", keyId=").append(keyId);
        sb.append(", interfaceConfigId=").append(interfaceConfigId);
        sb.append(", requestName=").append(requestName);
        sb.append(", requestType=").append(requestType);
        sb.append(", url=").append(url);
        sb.append(", requestMethod=").append(requestMethod);
        sb.append(", content=").append(content);
        sb.append(", port=").append(port);
        sb.append(", newColumn=").append(newColumn);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}