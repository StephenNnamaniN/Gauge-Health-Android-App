package uk.ac.tees.a0278818.gaugehealth.conditionsModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MainEntityOfPage {
    private String type;
    private List<String> genre = new ArrayList<String>();
    private String datePublished;
    private String dateModified;
    private List<String> lastReviewed = new ArrayList<String>();
    private String reviewDue;
    private String keywords;
    private List<Code> code = new ArrayList<Code>();
    private List<String> alternateName = new ArrayList<String>();
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<String> getGenre() {
        return genre;
    }
    public void setGenre(List<String> genre) {
        this.genre = genre;
    }
    public String getDatePublished() {
        return datePublished;
    }
    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }
    public String getDateModified() {
        return dateModified;
    }
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
    public List<String> getLastReviewed() {
        return lastReviewed;
    }
    public void setLastReviewed(List<String> lastReviewed) {
        this.lastReviewed = lastReviewed;
    }
    public String getReviewDue() {
        return reviewDue;
    }
    public void setReviewDue(String reviewDue) {
        this.reviewDue = reviewDue;
    }
    public String getKeywords() {
        return keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    public List<Code> getCode() {
        return code;
    }
    public void setCode(List<Code> code) {
        this.code = code;
    }
    public List<String> getAlternateName() {
        return alternateName;
    }
    public void setAlternateName(List<String> alternateName) {
        this.alternateName = alternateName;
    }
}
