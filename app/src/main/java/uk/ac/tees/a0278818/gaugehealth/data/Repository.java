package uk.ac.tees.a0278818.gaugehealth.data;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.a0278818.gaugehealth.conditionsModels.Author;
import uk.ac.tees.a0278818.gaugehealth.conditionsModels.Item;
import uk.ac.tees.a0278818.gaugehealth.conditionsModels.SignificantLink;
import uk.ac.tees.a0278818.gaugehealth.controller.AppController;
import uk.ac.tees.a0278818.gaugehealth.utils.ConditionsUtil;

public class Repository {
    static List<Item> itemList = new ArrayList<>();
    public static void getItems(final AsyncResponse callback){
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, ConditionsUtil.CONDITIONS_URL, null, response -> {

                    try {
                        JSONArray jsonArray = response.getJSONArray("significantLink");
                        for (int i = 0; i < jsonArray.length(); i++){
                            Item item = new Item();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            item.setName(jsonObject.getString("name"));
                            item.setUrl(jsonObject.getString("url"));
                            item.setDescription(jsonObject.getString("description"));
                            item.setArticleStatus(jsonObject.getString("articleStatus"));
//                            item.setId(jsonObject.getString("id"));

                            itemList.add(item);


//                            // Setup SignificantLinks
//                            JSONArray linksArray = jsonObject.getJSONArray("significantLink");
//                            List<SignificantLink> links = new ArrayList<>();
//                            for (int j = 0; j < linksArray.length(); j++) {
//                                SignificantLink significantLink = new SignificantLink();
//                                significantLink.setName(linksArray.getJSONObject(j).getString("name"));
//                                significantLink.setDescription(linksArray.getJSONObject(j).getString("description"));
//                                significantLink.setUrl(linksArray.getJSONObject(j).getString("url"));
//                                significantLink.setArticleStatus(linksArray.getJSONObject(j).getString("articleStatus"));
//                                significantLink.setLinkRelationship(linksArray.getJSONObject(j).getString("linkRelationship"));
//
//                                links.add(significantLink);
//                            }
//                            item.setSignificantLinks(links);
                        }

                        if (null != callback) { callback.processItem(itemList);}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                   error.printStackTrace();
                });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
