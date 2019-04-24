package com.smartgreen.model;

/**
 * @Description TODO
 * @Author Honda
 * @Date 2019/4/24 16:45
 **/

import java.util.List;

/**
 * 计量实体到管理实体的转换表
 */
public class ManageEntityFormula {

    private List<ManageEntity> formula;

    public ManageEntityFormula(List<ManageEntity> formula) {
        this.formula = formula;
    }

    public ManageEntityFormula() {
    }

    public List<ManageEntity> getFormula() {
        return formula;
    }

    public void setFormula(List<ManageEntity> formula) {
        this.formula = formula;
    }
}
