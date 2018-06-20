package kbsearch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.apache.jena.query.QuerySolution;
import org.json.JSONArray;
import org.json.JSONObject;

import me.hagen.kg.QResultSet;
import me.hagen.kg.TDBConnection;

public class KGS extends HttpServlet {
	 protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
	        doGet(request, response);
	    }

	    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
	    	response.setContentType("text/html;charset=utf-8");
	    	String suffix = request.getParameter("suffix");
	    	JSONObject res = new JSONObject();
	    	if(!suffix.startsWith("http://"))suffix = "http://dbpedia.org/resource/"+suffix;
	    	res.put("subject", suffix);
	    	suffix = "<"+suffix+">";
	    	String sparql1 = "select ?p ?pname ?o ?oname where {"+suffix+" ?p ?o. "
	    			+ "optional {?p <http://xmlns.com/foaf/0.1/name> ?pname  } ."
	    			+ "optional {?o <http://xmlns.com/foaf/0.1/name> ?oname  } ."
	    			+ "}";
	    	TDBConnection conn = TDBConnection.getConnection();
	    	TDBConnection conn39 = TDBConnection.getConnection39();
	    	QResultSet rs = conn.sparqlQuery(sparql1);
	    	JSONArray array = new JSONArray();
	    	Map<String,JSONObject> hash = new HashMap<>();
	    	while(rs.hasNext()){
	    		JSONObject obj = new JSONObject();
	    		QuerySolution qs = rs.next();
	    		obj.put("predicate", qs.get("p").toString());
	    		obj.put("object", qs.get("o").toString());
	    		String hashs = (qs.get("p").toString()+qs.get("o").toString());
	    		if(hash.get(hashs)!=null){
	    			continue;
	    		}
	    		hash.put(hashs, obj);
	    		if(qs.get("pname")!=null){
	    			obj.put("pname", qs.get("pname").asLiteral().getLexicalForm());
	    		}
	    		if(qs.get("oname")!=null){
	    			obj.put("oname", qs.get("oname").asLiteral().getLexicalForm());
	    		}
	    		array.put(obj);
	    	}
	    	rs.close();
	    	rs = conn39.getAbstract(suffix);
	    	while(rs.hasNext()){
	    		JSONObject obj = new JSONObject();
	    		QuerySolution qs = rs.next();
	    		obj.put("predicate", qs.get("p").toString());
	    		obj.put("object", qs.get("o").toString());
	    		array.put(obj);
	    	}
	    	//JSONObject res = new JSONObject();
	    	res.put("relation", array);
	    	rs.close();
	    	conn.close();
	    	conn39.close();
	    	response.getWriter().println(res.toString());
	    	response.getWriter().close();
	    }
}