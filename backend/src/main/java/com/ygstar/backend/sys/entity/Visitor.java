package com.ygstar.backend.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author lca
 * @since 2024-01-08
 */
@TableName("t_visitor")
@ApiModel(value = "Visitor对象", description = "")
public class Visitor implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idcardNumber;

    private String visitorName;

    private String userMail;

    private Boolean deleteFlag;

    public String getIdcardNumber() {
        return idcardNumber;
    }

    public void setIdcardNumber(String idcardNumber) {
        this.idcardNumber = idcardNumber;
    }
    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
    public String getUserMail() {
        return userMail;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @Override
    public String toString() {
        return "Visitor{" +
            "idcardNumber=" + idcardNumber +
            ", visitorName=" + visitorName +
            ", userMail=" + userMail +
            ", deleteFlag="   + deleteFlag +
        "}";
    }
}
