package ES;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ESClient {
    RestHighLevelClient client = null;
    String ESIP = "10.77.40.35";
    public ESClient()
    {
        System.out.println("ESClient()");
        if(client == null)
        {
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(ESIP, 9200, "http"),
                            new HttpHost(ESIP, 9201, "http")));
        }
    }

    /**
    * @User: jyh
    * @Date: 2018/6/11
    * @Desc: get all entities without keywords
    */
    public JSONObject ESC_getall()
    {
        System.out.println("ESC_getall");
        JSONObject result = new JSONObject();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest);
            RestStatus status = searchResponse.status();
            if(status.getStatus() >= 400)
            {
                System.out.println("search error:  " + status.toString());
            }
            SearchHits hits = searchResponse.getHits();
            result.append("hits", hits.getTotalHits());
            result.append("results", hits.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
    * @User: jyh
    * @Date: 2018/6/11
    * @Desc: get the first 20(maybe need to be changed) entities about the keyword(@param:key)
     * @param key: keywords to match, could be a sentence.
    */
    public JSONObject ESC_keySearch(String key)
    {
        JSONObject result = new JSONObject();
        System.out.println("ESC_keySearch key:" + key);
        SearchRequest searchRequest = new SearchRequest();
        MultiMatchQueryBuilder queryBuilder = new MultiMatchQueryBuilder(key);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(20);
        searchSourceBuilder.from(0);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest);
            RestStatus status = searchResponse.status();
            if(status.getStatus() >= 400)
            {
                System.out.println("search error:  " + status.toString());
            }
            SearchHits hits = searchResponse.getHits();
            result.append("hits", hits.getTotalHits());
            SearchHit[] hitArray = hits.getHits();
            JSONArray ja = new JSONArray();
            System.out.println("result length:" + hitArray.length);
            int i = 0;
            for(i = 0; i < hitArray.length; i++)
            {
                SearchHit hit = hitArray[i];
                JSONObject jo = new JSONObject(hit.getSourceAsMap());
                ja.put(jo);

            }
            result.append("results", ja);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public void ESC_close()
    {
        try {
            client.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
