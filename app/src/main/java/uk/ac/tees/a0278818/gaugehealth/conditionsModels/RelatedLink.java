package uk.ac.tees.a0278818.gaugehealth.conditionsModels;

import java.util.HashMap;
import java.util.Map;
public class RelatedLink {
    private String type;
    private String url;
    private String name;
    private String description;
    private String linkRelationship;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLinkRelationship() {
        return linkRelationship;
    }
    public void setLinkRelationship(String linkRelationship) {
        this.linkRelationship = linkRelationship;
    }
}