package kbsearch;

import ES.ESClient;
import me.hagen.pg.Conn;
import me.hagen.pg.Node;
import me.hagen.pg.NodeDao;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.*;

/**
* @User: jyh
* @Date: 2018/6/11
* @Desc: handle the keywords search
*/
public class KBS extends javax.servlet.http.HttpServlet {
    public static int ResultNum = 100;
    public static  ESClient client = new ESClient();
    public static  SearchHits hits = null;
    public static  String SCORE = "total_score";
    public static  String SOURCE = "source";
    public static  String COUNT = "count";
    public static  String RESULT = "results";
    public static double pageRankWeight = 0.4;
    public static double ESWeight = 0.6;
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        // 允许该域发起跨域请求
        response.setHeader("Access-Control-Allow-Origin", "*");//*允许任何域
        // 允许的外域请求方式
        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        // 在999999秒内，不需要再发送预检验请求，可以缓存该结果
        response.setHeader("Access-Control-Max-Age", "999999");
        // 允许跨域请求包含某请求头,x-requested-with请求头为异步请求
        response.setHeader("Access-Control-Allow-Headers",
                "x-requested-with");
        PrintWriter out = response.getWriter();
        System.out.println("test");
     //   out.println("KBS---hello world ! version4");
        String key = request.getParameter("keywords");

        out.println(getResults(key).toString());
    }

    public JSONObject getResults(String key){

        JSONObject result = new JSONObject();
        // get results from ES
        if(key != null && (key != "")) {
            //     out.println("KBS---keywords:" + key);
            hits = client.ESC_keySearch(key, 0, ResultNum);
        }else{
            hits = client.ESC_getall(0, ResultNum);
        }
        SearchHit[] hitArray = hits.getHits();
        System.out.println("result length:" + hitArray.length);

        // get score of pagerank and sum
        DecimalFormat df = new DecimalFormat( "0.0000 ");
        List<JSONObject> resultList = new ArrayList<JSONObject>();
        Connection conn = Conn.getConnection();
        int i = 0;
        for(i = 0; i < hitArray.length; i++)
        {

            SearchHit hit = hitArray[i];
            double pangRankScore = 0;
            String id ="<" + hit.getId() + ">";
            Node node = new NodeDao()
                    .get(conn, id);
            if(node != null){
                pangRankScore = node.getPscore();
            }else {
                System.out.println("ERROR:" + id + " not exist in Mysql");
            }
            double score = pangRankScore * pageRankWeight + hit.getScore() * ESWeight;
            JSONObject jo = new JSONObject();
            jo.put(SOURCE, new JSONObject(hit.getSourceAsMap()));
            jo.put(SCORE, score);
            System.out.println(id + ": " + pangRankScore + "," + hit.getScore());
            resultList.add(jo);
        }

        // sort by score
        Collections.sort(resultList, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
       //         System.out.println(o1.toString());
                double score1 = o1.getDouble(SCORE);
                double score2 = o2.getDouble(SCORE);
        //        System.out.println(score1 + "," + score2);
                if(score1 > score2)
                {
                    return -1;
                }else if (score1 < score2)
                {
                    return 1;
                }else
                {
                    return 0;
                }
            }
        });
        result.put(COUNT, resultList.size());
        result.put(RESULT, new JSONArray(resultList));
        return result;
    }
}


