package me.hagen.pg;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class PredicateDaoTest {

	@Test
	public void test() throws SQLException {
		Connection conn = Conn.getConnection();
		String p = "<http://dbpedia.org/ontology/range>";
		
		PredicateDao dao = new PredicateDao();
		Predicate pred = dao.get(conn, p);
		System.out.println(pred.getWeight());
		conn.close();
	}

}
