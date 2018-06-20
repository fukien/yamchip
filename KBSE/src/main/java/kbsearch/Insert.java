package kbsearch;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import net.sf.json.JSONArray;

import ES.ESClient;
import me.hagen.kg.TDBConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;


public class Insert extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("aaaa");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        System.out.println("test");

        //将接受到的数据放入
        String jsonStr="";
        JSONArray json =new JSONArray(request.getParameter(jsonStr));
        //JSONArray json = (jsonStr);
        TDBConnection tdb=TDBConnection.getConnection();
        //将jsonarray对象转换为单个json并进行解析。
       //先将对象转成json数组
        for(int i=0;i<json.length();i++)
        {
            JSONObject job= json.getJSONObject(i);

            String time;
            String subject;
            String s;
            String object;
            String o;
            System.out.println(job.get("subject_new"));
            if(job.get("subject_new").equals("true"))
            {
                subject=(String)job.get("subject");
                s="<"+subject+">";
            }
            else
            {
                subject=(String)job.get("subject");
                s="<"+subject+">";
            }
            String predicate=(String)job.get("predicate");
            String p="<"+predicate+">";
            if(job.get("object_string").equals("false"))
            {
                object=(String)job.get("object");
                o="<"+object+">";
                tdb.insert(s,p,o);
            }
            else
            {
                Map<String, Object> source = new HashMap<String,Object>();
                object=(String)job.get("object");
                tdb.insert(s,p,object);
                time="" + System.currentTimeMillis();
                String value=predicate+" "+object;
                source.put(time,value);

                ESClient client=new ESClient();
                if(client.ESC_insert(source,subject))
                    System.out.println("yes");
                else
                    System.out.println("no");
            }
        }


    }



}

