package uk.ac.tees.a0278818.gaugehealth.conditionsModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Root {
    private String context;
    private String type;
    private String name;
    private CopyrightHolder copyrightHolder;
    private String license;
    private Author author;
    private About about;
    private String description;
    private String url;
    private List<Object> genre = new ArrayList<Object>();
    private String keywords;
    private String dateModified;
    private Breadcrumb breadcrumb;
    private List<Object> contentSubTypes = new ArrayList<Object>();
    private List<SignificantLink> significantLink = new ArrayList<SignificantLink>();
    private List<RelatedLink> relatedLink = new ArrayList<RelatedLink>();
    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public CopyrightHolder getCopyrightHolder() {
        return copyrightHolder;
    }
    public void setCopyrightHolder(CopyrightHolder copyrightHolder) {
        this.copyrightHolder = copyrightHolder;
    }
    public String getLicense() {
        return license;
    }
    public void setLicense(String license) {
        this.license = license;
    }
    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }
    public About getAbout() {
        return about;
    }
    public void setAbout(About about) {
        this.about = about;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public List<Object> getGenre() {
        return genre;
    }
    public void setGenre(List<Object> genre) {
        this.genre = genre;
    }
    public String getKeywords() {
        return keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    public String getDateModified() {
        return dateModified;
    }
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
    public Breadcrumb getBreadcrumb() {
        return breadcrumb;
    }
    public void setBreadcrumb(Breadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
    }
    public List<Object> getContentSubTypes() {
        return contentSubTypes;
    }
    public void setContentSubTypes(List<Object> contentSubTypes) {
        this.contentSubTypes = contentSubTypes;
    }
    public List<SignificantLink> getSignificantLink() {
        return significantLink;
    }
    public void setSignificantLink(List<SignificantLink> significantLink) {
        this.significantLink = significantLink;
    }
    public List<RelatedLink> getRelatedLink() {
        return relatedLink;
    }
    public void setRelatedLink(List<RelatedLink> relatedLink) {
        this.relatedLink = relatedLink;
    }
}

