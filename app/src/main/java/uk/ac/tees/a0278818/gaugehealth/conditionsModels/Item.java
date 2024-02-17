package uk.ac.tees.a0278818.gaugehealth.conditionsModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {
    private String name;
    private String Description;
    private String url;
    private String articleStatus;
    private String logo;
    private List<Author> author;
    private List<SignificantLink> significantLinks;
    private List<ItemListElement> itemList;
    private String id;
    private String elementName;
    private String elementId;
    private List<Object> genre = new ArrayList<Object>();


    static Item fill(JSONObject jsonobj) throws JSONException {
        Item item = new Item();
        if (jsonobj.has("name")){
            item.setName(jsonobj.getString("name"));
        }
        if (jsonobj.has("elementName")){
            item.setName(jsonobj.getString("elementName"));
        }
        if (jsonobj.has("elementId")){
            item.setName(jsonobj.getString("elementId"));
        }
        if (jsonobj.has("url")){
            item.setUrl(jsonobj.getString("url"));
        }
        if (jsonobj.has("description")){
            item.setDescription(jsonobj.getString("description"));
        }
        if (jsonobj.has("articleStatus")){
            item.setArticleStatus(jsonobj.getString("articleStatus"));
        }
        if (jsonobj.has("significantLink")){
            item.setSignificantLinks((List<SignificantLink>) jsonobj.getJSONArray("significantLink"));
        }
        if (jsonobj.has("itemListElement")){
            item.setItemList((List<ItemListElement>) jsonobj.getJSONArray("itemListElement"));
        }
        if (jsonobj.has("logo")){
            item.setLogo(jsonobj.getString("logo"));
        }
        if (jsonobj.has("author")){
            item.setAuthor((List<Author>) jsonobj.getJSONArray("author"));
        }
        return item;
    }

    static List<Item> fillList(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null || jsonArray.length() == 0)
            return null;
        List<Item> olist = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            olist.add(fill(jsonArray.getJSONObject(i)));
        }
        return olist;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public List<ItemListElement> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListElement> itemList) {
        this.itemList = itemList;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Object> getGenre() {
        return genre;
    }
    public void setGenre(List<Object> genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return Description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    public List<SignificantLink> getSignificantLinks() {
        return significantLinks;
    }

    public void setSignificantLinks(List<SignificantLink> significantLinks) {
        this.significantLinks = significantLinks;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(String articleStatus) {
        this.articleStatus = articleStatus;
    }
}
