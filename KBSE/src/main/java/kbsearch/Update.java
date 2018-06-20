package kbsearch;

import ES.ESClient;
import me.hagen.pg.Conn;
import me.hagen.pg.Node;
import me.hagen.pg.NodeDao;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @User: hxr
 * @Date: 2018/6/20
 * @Desc: handle the keywords search
 */
public class Update extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        JSONArray jsonRespArray=new JSONArray();
        Conn conn=new Conn();
        String name= "";
        request.getParameter("name");
        List<Node> node=new ArrayList<Node>();
       // List<String> name = new ArrayList();
        NodeDao nodedao=new NodeDao();
       Connection con=conn.getConnection();
        try {
            node=nodedao.prefixMatch( con,name,100);
            String result;
            result="";
            for(int i=0;i<node.size();i++)
            {
                Node value=node.get(i);
                String tmp=value.getName();
                String tmp1=value.getId();
                result=result+"{ name:"+tmp+",uri=:"+tmp1+"}";
                jsonRespArray.put(new JSONObject(result));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out=response.getWriter();
        out.println(jsonRespArray.toString());
        out.close();
    }
}