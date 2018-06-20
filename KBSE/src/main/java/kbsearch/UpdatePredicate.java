package kbsearch;

import ES.ESClient;
import me.hagen.pg.Conn;
import me.hagen.pg.Node;
import me.hagen.pg.NodeDao;
import me.hagen.pg.Predicate;
import me.hagen.pg.PredicateDao;

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
public class UpdatePredicate extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        JSONArray jsonRespArray=new JSONArray();
        //Conn conn=new Conn();
        String name= "";
        name = request.getParameter("data");
        List<Predicate> node=new ArrayList<Predicate>();
       // List<String> name = new ArrayList();
        //NodeDao nodedao=new NodeDao();
        PredicateDao pdao = new PredicateDao();
       Connection con=Conn.getConnection();
        try {
            node=pdao.prefixMatch( con,name,100);
            for(int i=0;i<node.size();i++)
            {
                Predicate value=node.get(i);
                String tmp=value.getId().replace("http://dbpedia.org/ontology/", "");
                tmp = tmp.substring(1,tmp.length()-1);
                String tmp1=value.getId();
                JSONObject obj = new JSONObject();
                obj.put("name", tmp);
                obj.put("uri", tmp1);
                jsonRespArray.put(obj);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out=response.getWriter();
        out.println(jsonRespArray.toString());
        out.close();
    }
}
