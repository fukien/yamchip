package kbsearch;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import net.sf.json.JSONArray;

import ES.ESClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;


public class Insert extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //System.out.println("aaaa");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        System.out.println("test");

        //将接受到的数据放入
        String jsonStr="";
        request.getParameter(jsonStr);
        JSONArray json = new JSONArray(jsonStr);

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
            if(job.get("subject_new")=="true")
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
            if(job.get("object_string")=="false")
            {
                object=(String)job.get("object");
                o="<"+object+">";
                //insert(s,p,o);
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
            }
        }


    }

    /*
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
    */

}

