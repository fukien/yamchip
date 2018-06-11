package kbsearch;

import ES.ESClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

/**
* @User: jyh
* @Date: 2018/6/11
* @Desc: handle the keywords search
*/
public class KBS extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        System.out.println("test");
     //   out.println("KBS---hello world ! version4");
        ESClient client = new ESClient();
        String key = request.getParameter("keywords");
        JSONObject result = null;
        if(key != null && (key != "")) {
            out.println("KBS---keywords:" + key);
            result = client.ESC_keySearch(key);
        }else{
            result = client.ESC_getall();
        }
        out.println("search result: " + result.toString());
    }
}
