package me.hagen.pg;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

public class PredicateDaoTest {

	@Test
	public void testPrefixMatch() throws SQLException {
		Connection conn = Conn.getConnection();
		PredicateDao dao = new PredicateDao();
		List<Predicate> p = dao.prefixMatch(conn, "Birth", 10);
		for(Predicate pr:p)System.out.println(pr.getId());
		conn.close();
	}
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
