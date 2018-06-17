package me.hagen.kg;

import java.io.Closeable;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.resultset.ResultSetWrapper;

public class QResultSet extends ResultSetWrapper implements Closeable {
	private QueryExecution exec = null;
	public QResultSet(ResultSet rs, QueryExecution _exec) {
		super(rs);
		exec = _exec;
	}
	public void close() {
		exec.close();
	}
}
