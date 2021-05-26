package com.yuyi.pts.model.client;

import java.io.Serializable;
import lombok.Data;

/**
 * param
 * @author 
 */
@Data
public class Param implements Serializable {
    /**
     * 主键
     */
    private Integer paramId;

    /**
     * 关联id,与interfaceConfig表id对应
     */
    private String paramInterfaceId;

    /**
     * 标识id
     */
    private String paramKeyId;

    /**
     * 写入下标
     */
    private Integer paramIndex;

    /**
     * 参数长度
     */
    private Integer paramLength;

    /**
     * 参数名称
     */
    private String paramField;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 输入输出
     */
    private String paramIo;

    private Integer extend3;

    private Integer extend4;

    private Integer extend5;

    private Integer extend6;

    private Integer extend7;

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
        Param other = (Param) that;
        return (this.getParamId() == null ? other.getParamId() == null : this.getParamId().equals(other.getParamId()))
            && (this.getParamInterfaceId() == null ? other.getParamInterfaceId() == null : this.getParamInterfaceId().equals(other.getParamInterfaceId()))
            && (this.getParamKeyId() == null ? other.getParamKeyId() == null : this.getParamKeyId().equals(other.getParamKeyId()))
            && (this.getParamIndex() == null ? other.getParamIndex() == null : this.getParamIndex().equals(other.getParamIndex()))
            && (this.getParamLength() == null ? other.getParamLength() == null : this.getParamLength().equals(other.getParamLength()))
            && (this.getParamField() == null ? other.getParamField() == null : this.getParamField().equals(other.getParamField()))
            && (this.getParamValue() == null ? other.getParamValue() == null : this.getParamValue().equals(other.getParamValue()))
            && (this.getParamType() == null ? other.getParamType() == null : this.getParamType().equals(other.getParamType()))
            && (this.getParamIo() == null ? other.getParamIo() == null : this.getParamIo().equals(other.getParamIo()))
            && (this.getExtend3() == null ? other.getExtend3() == null : this.getExtend3().equals(other.getExtend3()))
            && (this.getExtend4() == null ? other.getExtend4() == null : this.getExtend4().equals(other.getExtend4()))
            && (this.getExtend5() == null ? other.getExtend5() == null : this.getExtend5().equals(other.getExtend5()))
            && (this.getExtend6() == null ? other.getExtend6() == null : this.getExtend6().equals(other.getExtend6()))
            && (this.getExtend7() == null ? other.getExtend7() == null : this.getExtend7().equals(other.getExtend7()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getParamId() == null) ? 0 : getParamId().hashCode());
        result = prime * result + ((getParamInterfaceId() == null) ? 0 : getParamInterfaceId().hashCode());
        result = prime * result + ((getParamKeyId() == null) ? 0 : getParamKeyId().hashCode());
        result = prime * result + ((getParamIndex() == null) ? 0 : getParamIndex().hashCode());
        result = prime * result + ((getParamLength() == null) ? 0 : getParamLength().hashCode());
        result = prime * result + ((getParamField() == null) ? 0 : getParamField().hashCode());
        result = prime * result + ((getParamValue() == null) ? 0 : getParamValue().hashCode());
        result = prime * result + ((getParamType() == null) ? 0 : getParamType().hashCode());
        result = prime * result + ((getParamIo() == null) ? 0 : getParamIo().hashCode());
        result = prime * result + ((getExtend3() == null) ? 0 : getExtend3().hashCode());
        result = prime * result + ((getExtend4() == null) ? 0 : getExtend4().hashCode());
        result = prime * result + ((getExtend5() == null) ? 0 : getExtend5().hashCode());
        result = prime * result + ((getExtend6() == null) ? 0 : getExtend6().hashCode());
        result = prime * result + ((getExtend7() == null) ? 0 : getExtend7().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", paramId=").append(paramId);
        sb.append(", paramInterfaceId=").append(paramInterfaceId);
        sb.append(", paramKeyId=").append(paramKeyId);
        sb.append(", paramIndex=").append(paramIndex);
        sb.append(", paramLength=").append(paramLength);
        sb.append(", paramField=").append(paramField);
        sb.append(", paramValue=").append(paramValue);
        sb.append(", paramType=").append(paramType);
        sb.append(", paramIo=").append(paramIo);
        sb.append(", extend3=").append(extend3);
        sb.append(", extend4=").append(extend4);
        sb.append(", extend5=").append(extend5);
        sb.append(", extend6=").append(extend6);
        sb.append(", extend7=").append(extend7);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}