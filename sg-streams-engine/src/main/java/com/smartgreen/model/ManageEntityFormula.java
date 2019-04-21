package com.smartgreen.model;

import java.util.List;
import java.util.Map;

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
