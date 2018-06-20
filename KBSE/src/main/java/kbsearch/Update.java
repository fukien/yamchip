package kbsearch;

import ES.ESClient;
import me.hagen.pg.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray jsonRespArray = new JSONArray();

        Conn conn = new Conn();
        Connection con = conn.getConnection();
        String name = request.getParameter("name");
        String str=name.substring(0,3);
        if(str.equals("///"))
        {
            name=name.substring(3);
            List<Predicate> node=new ArrayList<Predicate>();
            PredicateDao nodeDao=new PredicateDao();
            try {
                node = nodeDao.prefixMatch(con, name, 100);
                for (int i = 0; i < node.size(); i++) {
                    Predicate value = node.get(i);
                    JSONObject jObject = new JSONObject();
                    jObject.put("name",value.getId());
                    jObject.put("uri",value.getId());
                    jsonRespArray.put(jObject);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            List<Node> node = new ArrayList<Node>();
            // List<String> name = new ArrayList();
            NodeDao nodedao = new NodeDao();

            try {
                node = nodedao.prefixMatch(con, name, 100);
                for (int i = 0; i < node.size(); i++) {
                    Node value = node.get(i);
                    JSONObject jObject = new JSONObject();
                    jObject.put("name",value.getName());
                    jObject.put("uri",value.getId());
                    jsonRespArray.put(jObject);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(jsonRespArray.toString());
        out.close();
    }
}