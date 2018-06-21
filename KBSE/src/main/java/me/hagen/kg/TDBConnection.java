package me.hagen.kg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.ARQ;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.rdfconnection.examples.RDFConnectionExample1;
import org.apache.jena.rdfconnection.examples.RDFConnectionExample2;
import org.apache.jena.rdfconnection.examples.RDFConnectionExample3;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;


/**
 * 
 * 
 */
public class TDBConnection implements AutoCloseable{

	RDFConnection conn = null;
	String queryService = null;
	static String queryPoint = "http://10.77.50.103:3030/dbpedia381";
	public static TDBConnection getConnection(){
		return new TDBConnection(queryPoint);
	}
	public static TDBConnection getConnection39(){
		return new TDBConnection("http://10.77.50.103:3030/dbp39");
	}
	public TDBConnection(String queryService){
		conn = RDFConnectionFactory.connect(queryService);
	}
	public QResultSet getAbstract(String id){
		String sparql = "select ?p ?o where {?s ?p ?o . filter(?s="+id+") .  filter(?p=<http://dbpedia.org/ontology/abstract>)}";
		return sparqlQuery(sparql);
	}
	public QResultSet sparqlQuery(String query){
		QueryExecution qexec = conn.query(query);
		return new QResultSet(qexec.execSelect(),qexec);
	}
	public boolean askQuery(String query){
		QueryExecution qexec = conn.query(query);
		boolean ret = qexec.execAsk();
		qexec.close();
		return ret;
	}
	public void insert(String s,String p, String o){
		String sparql = "insert { ?s ?p ?o } where { bind("+s+" as ?s) . bind("+p+" as ?p) . bind("+o+" as ?o)}";
		conn.begin(ReadWrite.WRITE);
		//System.out.println(sparql);
		System.out.println(sparql);
		UpdateRequest req = UpdateFactory.create(sparql);
		conn.update(req);
		conn.commit();
	}
	public void insert2(String s,String p, String o){
		String sparql = "insert { "+s+" "+p+" "+o+" } ";
		System.out.println(sparql);
		conn.begin(ReadWrite.WRITE);
		UpdateRequest req = UpdateFactory.create(sparql);
		conn.update(req);
		conn.commit();
	}
	public void delete(String s,String p, String o){
		String sparql = "delete { ?s ?p ?o } where { bind("+s+" as ?s) . bind("+p+" as ?p) . bind("+o+" as ?o)}";
		conn.begin(ReadWrite.WRITE);
		UpdateRequest req = UpdateFactory.create(sparql);
		conn.update(req);
		conn.commit();
	}
	public void close(){
		conn.close();
	}
	public static void main(String args[]){
		String queryService = "http://10.77.50.103:3030/dbpedia38";
		String query = "PREFIX fb:  <http://rdf.freebase.com/ns/> select ?s ?p ?o where { ?s ?p ?o . filter(isIRI(?o)) . filter(?s=fb:m.01jzhl) }";
        try ( TDBConnection conn = new TDBConnection(queryService) ) {
            QResultSet qrs = conn.sparqlQuery(query);
            while(qrs.hasNext()){
            	QuerySolution qs = qrs.next();
            	System.out.println(qs);
            }
            qrs.close();
        }
	}
}
