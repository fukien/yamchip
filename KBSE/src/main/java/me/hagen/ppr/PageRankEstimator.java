package me.hagen.ppr;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;

import me.hagen.kg.QResultSet;
import me.hagen.kg.TDBConnection;
import me.hagen.pg.Node;
import me.hagen.pg.NodeDao;
import me.hagen.pg.Predicate;
import me.hagen.pg.PredicateDao;

public class PageRankEstimator {
	static class Triple{
		double p;
		double o;
	}
	private TDBConnection kbconn;
	private Connection dbconn;
	private NodeDao ndao = null;
	private PredicateDao pdao = null;
	public PageRankEstimator(TDBConnection tdb,Connection db){
		kbconn = tdb;
		dbconn = db;
		ndao = new NodeDao();
		pdao = new PredicateDao();
	}
	public double estimatePageRank(String id,boolean diverse){
		String forward = "select ?p ?o where {"+id+" ?p ?o . filter(isIRI(?o))}";
		String backward = "select ?p ?o where {?o ?p "+id+" .}";
		List<Triple> fors = null;
		if(!diverse){fors = collector(forward);}
		else {fors = collectorDiverse(forward);}
		List<Triple> back = null;
		if(!diverse)
		{back = collector(backward);}
		else{
			back = collector(backward);
		}
		double sum = 0.0;
		for(Triple t: fors){
			sum+=t.p*t.o;
			//weight+=t.p;
		}
		for(Triple t: back){
			sum+=t.p*t.o;
			//weight+=t.p;
		}
		//sum = sum/weight;
		return sum;
	}
	private List<Triple> collector(String sparql){
		QResultSet rs = kbconn.sparqlQuery(sparql);
		List<Triple> arrayList = new ArrayList<Triple>();
		while(rs.hasNext()){
			QuerySolution qs = rs.next();
			String predicate = "<"+qs.get("p").asNode().getURI()+">";
			String object = "<"+qs.get("o").asNode().getURI()+">";
			Predicate p = pdao.get(dbconn, predicate);
			Node o = ndao.get(dbconn, object);
			if(o==null||p==null||o.getWsum()==0){
				System.out.println(object+" "+predicate);
				continue;
			}
			Triple t = new Triple();
			t.p = p.getWeight()/o.getWsum();
			t.o = o.getPscore();
			arrayList.add(t);
		}
		return arrayList;
	}
	private List<Triple> collectorDiverse(String sparql){
		QResultSet rs = kbconn.sparqlQuery(sparql);
		List<Triple> arrayList = new ArrayList<Triple>();
		while(rs.hasNext()){
			QuerySolution qs = rs.next();
			String predicate = "<"+qs.get("p").asNode().getURI()+">";
			String object = "<"+qs.get("o").asNode().getURI()+">";
			Predicate p = pdao.get(dbconn, predicate);
			Node o = ndao.get(dbconn, object);
			if(o==null||p==null||o.getWsum2()==0){
				System.out.println(object+" "+predicate);
				continue;
			}
			Triple t = new Triple();
			t.p = p.getWeight()*o.getDiverse()/o.getWsum2();
			t.o = o.getPscore();
			arrayList.add(t);
		}
		return arrayList;
	}
}
