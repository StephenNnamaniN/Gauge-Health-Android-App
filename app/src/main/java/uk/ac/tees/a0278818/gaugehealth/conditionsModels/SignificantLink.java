package uk.ac.tees.a0278818.gaugehealth.conditionsModels;

import java.util.HashMap;
import java.util.Map;
public class SignificantLink {
    private String description;
    private String linkRelationship;
    private String type;
    private String articleStatus;
    private MainEntityOfPage mainEntityOfPage;
    private String url;
    private String name;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getArticleStatus() {
        return articleStatus;
    }
    public void setArticleStatus(String articleStatus) {
        this.articleStatus = articleStatus;
    }
    public MainEntityOfPage getMainEntityOfPage() {
        return mainEntityOfPage;
    }
    public void setMainEntityOfPage(MainEntityOfPage mainEntityOfPage) {
        this.mainEntityOfPage = mainEntityOfPage;
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
}
