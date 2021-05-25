package com.yuyi.pts.model.client;

import java.io.Serializable;
import lombok.Data;

/**
 * t_config
 * @author 
 */
@Data
public class TConfig implements Serializable {
    /**
     * 唯一标识符
     */
    private Integer configId;

    /**
     * 配置Key
     */
    private String configKey;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置类型
     */
    private String configType;

    /**
     * 类型对应值
     */
    private String configValue;

    /**
     * 接口配置id
     */
    private String interfaceConfigId;

    /**
     * 唯一标识符
     */
    private String configKeyId;

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
        TConfig other = (TConfig) that;
        return (this.getConfigId() == null ? other.getConfigId() == null : this.getConfigId().equals(other.getConfigId()))
            && (this.getConfigKey() == null ? other.getConfigKey() == null : this.getConfigKey().equals(other.getConfigKey()))
            && (this.getConfigName() == null ? other.getConfigName() == null : this.getConfigName().equals(other.getConfigName()))
            && (this.getConfigType() == null ? other.getConfigType() == null : this.getConfigType().equals(other.getConfigType()))
            && (this.getConfigValue() == null ? other.getConfigValue() == null : this.getConfigValue().equals(other.getConfigValue()))
            && (this.getInterfaceConfigId() == null ? other.getInterfaceConfigId() == null : this.getInterfaceConfigId().equals(other.getInterfaceConfigId()))
            && (this.getConfigKeyId() == null ? other.getConfigKeyId() == null : this.getConfigKeyId().equals(other.getConfigKeyId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getConfigId() == null) ? 0 : getConfigId().hashCode());
        result = prime * result + ((getConfigKey() == null) ? 0 : getConfigKey().hashCode());
        result = prime * result + ((getConfigName() == null) ? 0 : getConfigName().hashCode());
        result = prime * result + ((getConfigType() == null) ? 0 : getConfigType().hashCode());
        result = prime * result + ((getConfigValue() == null) ? 0 : getConfigValue().hashCode());
        result = prime * result + ((getInterfaceConfigId() == null) ? 0 : getInterfaceConfigId().hashCode());
        result = prime * result + ((getConfigKeyId() == null) ? 0 : getConfigKeyId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", configId=").append(configId);
        sb.append(", configKey=").append(configKey);
        sb.append(", configName=").append(configName);
        sb.append(", configType=").append(configType);
        sb.append(", configValue=").append(configValue);
        sb.append(", interfaceConfigId=").append(interfaceConfigId);
        sb.append(", configKeyId=").append(configKeyId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}