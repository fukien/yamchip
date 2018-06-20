package kbsearch;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import net.sf.json.JSONArray;










import ES.ESClient;
import me.hagen.kg.TDBConnection;
import me.hagen.pg.Conn;
import me.hagen.pg.Node;
import me.hagen.pg.NodeDao;
import me.hagen.pg.Predicate;
import me.hagen.pg.PredicateDao;
import me.hagen.ppr.PageRankEstimator;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;


public class Insert extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //将接受到的数据放入
        //String jsonStr="";
        String data = request.getParameter("data");
        System.out.println(data);
        JSONArray json = new JSONArray(data);
        TDBConnection tdb = TDBConnection.getConnection();
        Connection conn = Conn.getConnection();
        NodeDao ndao = new NodeDao();
        PredicateDao pdao = new PredicateDao();
        PageRankEstimator estimator = new PageRankEstimator(tdb,conn);
        //将jsonarray对象转换为单个json并进行解析。
       //先将对象转成json数组
        int success = 0;
        int error = 0;
        for(int i=0;i<json.length();i++)
        {
        	boolean errors = false;
            JSONObject job= json.getJSONObject(i);

            String time;
            String subject;
            String s;
            String object;
            String o;

           subject=(String)job.get("subject");
           subject = subject.trim();
           if(!subject.startsWith("<http://"))
           {
        	   subject ="<http://dbpedia.org/resource/"+subject+">";
           }
           if(!subject.startsWith("<"))
           {
        	   subject = "<"+subject+">";
           }
           s = subject;
            String predicate=(String)job.get("predicate");
            predicate = predicate.trim();
            if(!predicate.startsWith("<http://"))
            {
         	   predicate ="<http://dbpedia.org/ontology/"+predicate+">";
            }
            if(!predicate.startsWith("<"))
            {
         	   predicate = "<"+predicate+">";
            }
            String p = predicate;
            if(job.get("object_string").equals("false"))
            {
                object=(String)job.get("object");
                object = object.trim();
                if(!object.startsWith("<http://"))
                {
             	   object ="<http://dbpedia.org/resource/"+object+">";
                }
                if(!object.startsWith("<"))
                {
             	   object = "<"+subject+">";
                }
                o = object;
               
                tdb.insert(s,p,o);
                //tdb.close();
            }
            else
            {
                Map<String, Object> source = new HashMap<String,Object>();
                object=(String)job.get("object");
                //  insert(s,p,object);
                time="" + System.currentTimeMillis();
                String value=predicate+" "+object;
                source.put(time,value);

                ESClient client=new ESClient();
                client.ESC_insert(source,subject);
                
                String literal = object;
                if(!object.startsWith("\""))literal = "\""+literal+"\"";
                //TDBConnection tdb = TDBConnection.getConnection();
                tdb.insert(s,p,literal);
                //tdb.close();
            }

            if("true".equals(job.get("subject_new"))){
            	String node = s;
            	Node n = ndao.get(conn, node);
            	Predicate pred = pdao.get(conn, p);
            	if(n==null){
            		n = new Node();
            		n.setId(node);
            		n.setPscore(1.0);
            		n.setPscore2(1.0);
            		if(pred!=null){
            			n.setWsum(pred.getWeight());
            			n.setWsum2(pred.getWeight());
            		}
            		n.setDiverse(0);
            		try {
						ndao.add(conn, n);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						errors= true;
					}
            		
            		try {
						ndao.updatePrank(conn, node, estimator.estimatePageRank(node, false));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						errors = true;
					}
            	}else{
            		
            	}
            	

            }
            if(errors)error++;
            else success++;
        }
        tdb.close();
    	try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	response.getWriter().write("success:"+success+" error:"+error);
    	response.getWriter().close();
    }
    private String readJsonFromRequestBody(HttpServletRequest req){
        StringBuffer jsonBuf=new StringBuffer();
        char[] buf=new char[2048];
        int len=-1;
        try{
            BufferedReader reader=req.getReader();
            while((len=reader.read(buf))!=-1){
                jsonBuf.append(new String(buf,0,len));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return jsonBuf.toString();
    }

}