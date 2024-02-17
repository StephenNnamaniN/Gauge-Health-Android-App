package uk.ac.tees.a0278818.gaugehealth.conditionsModels;

import java.util.HashMap;
import java.util.Map;
public class Code {
    private String type;
    private String codeValue;
    private String codingSystem;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getCodeValue() {
        return codeValue;
    }
    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
    public String getCodingSystem() {
        return codingSystem;
    }
    public void setCodingSystem(String codingSystem) {
        this.codingSystem = codingSystem;
    }
}
