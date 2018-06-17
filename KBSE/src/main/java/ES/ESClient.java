package ES;

import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
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
import java.util.Map;

public class ESClient {
    RestHighLevelClient client = null;
    String ESIP = "10.77.40.35";
    static String indexName = "kb";
    static String docType = "_doc";
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
     * @param from: the start position of the results
     * @param size: the number of results that want to get
    */
    public SearchHits ESC_getall(int from, int size)
    {
        System.out.println("ESC_getall");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        return  ESC_search(searchSourceBuilder, from, size);
    }

    /**
    * @User: jyh
    * @Date: 2018/6/11
    * @Desc: get the first 20(maybe need to be changed) entities about the keyword(@param:key)
     * @param key: keywords to match, could be a sentence.
     * @param from: the start position of the results
     * @param size: the number of results that want to get
    */
    public SearchHits ESC_keySearch(String key, int from, int size)
    {
        System.out.println("ESC_keySearch key:" + key);
        MultiMatchQueryBuilder queryBuilder = new MultiMatchQueryBuilder(key);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        return  ESC_search(searchSourceBuilder, from, size);
    }

    /**
    * @User: jyh
    * @Date: 2018/6/17
    * @Desc: get the results from a given SearchSourceBuilder.
     * @param from: the start position of the results
     * @param size: the number of results that want to get
    */
    private SearchHits ESC_search(SearchSourceBuilder searchSourceBuilder, int from, int size){
//        JSONObject result = new JSONObject();
        SearchHits result = null;
        searchSourceBuilder.size(size);
        searchSourceBuilder.from(from);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest);
            RestStatus status = searchResponse.status();
            if(status.getStatus() >= 400)
            {
                System.out.println("search error:  " + status.toString());
            }
            result = searchResponse.getHits();
//            result.append("hits", hits.getTotalHits());
//            SearchHit[] hitArray = hits.getHits();
//            JSONArray ja = new JSONArray();
//            System.out.println("result length:" + hitArray.length);
//            int i = 0;
//            for(i = 0; i < hitArray.length; i++)
//            {
//                SearchHit hit = hitArray[i];
//                JSONObject jo = new JSONObject(hit.getSourceAsMap());
//                ja.put(jo);
//
//            }
//            result.append("results", ja);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    /**
    * @User: jyh
    * @Date: 2018/6/12
    * @Desc: insert an entity into the ES
     * @param source: the source of the entity to be inserted.
     *              e.g.  "subject" : "http://dbpedia.org/resource/George_Adamski",
     *                    "0" : "name George Adamski",
     *                    "1" : "birthDate 1891-04-17",
     *                   "2" : "birthYear 1891",
     * @param subject: the URI of the entity
    */
    public boolean ESC_insert(Map<String, Object> source, String subject)
    {
        boolean result = true;
        IndexRequest indexRequest = new IndexRequest(indexName, docType, subject).source(source);
        try {
            IndexResponse indexResponse = client.index(indexRequest);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                System.out.println("ESC_insert: create index-" +  indexResponse.getId());
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                System.out.println("ESC_insert: update index-" +  indexResponse.getId());
            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                System.out.println("ERROR: ESC_insert(" +  indexResponse.getId() + "): number of successful shards is less than total shards");
                result = false;
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                    System.out.println("ERROR: ESC_insert(" +  indexResponse.getId() + "): failure: " + reason );
                    result = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
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
