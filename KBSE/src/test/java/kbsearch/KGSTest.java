package kbsearch;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.query.QuerySolution;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import me.hagen.kg.QResultSet;
import me.hagen.kg.TDBConnection;

public class KGSTest {

	//@Test
	public void testDoGetHttpServletRequestHttpServletResponse() {
		String suffix = "Yao_Ming";
    	JSONObject res = new JSONObject();
    	if(!suffix.startsWith("http://"))suffix = "http://dbpedia.org/resource/"+suffix;
    	res.put("subject", suffix);
    	suffix = "<"+suffix+">";
    	String sparql1 = "select ?p ?pname ?o ?oname where {"+suffix+" ?p ?o. "
    			+ "optional {?p <http://xmlns.com/foaf/0.1/name> ?pname  } ."
    			+ "optional {?o <http://xmlns.com/foaf/0.1/name> ?oname  } ."
    			+ "}";
    	TDBConnection conn = TDBConnection.getConnection();
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
    			System.out.println(qs.get("pname").asLiteral().getLexicalForm());
    			obj.put("pname", qs.get("pname").asLiteral().getLexicalForm());
    		}
    		if(qs.get("oname")!=null){
    			//System.out.println(qs.get("oname").asLiteral().getLexicalForm());
    			obj.put("oname", qs.get("oname").asLiteral().getLexicalForm());
    		}
    		array.put(obj);
    	}
    	//JSONObject res = new JSONObject();
    	res.put("relation", array);
    	rs.close();
    	conn.close();
    	System.out.println(res.toString());
	}

}
