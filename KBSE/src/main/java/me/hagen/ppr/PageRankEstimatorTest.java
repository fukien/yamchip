package me.hagen.ppr;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import me.hagen.kg.TDBConnection;
import me.hagen.pg.Conn;
import me.hagen.pg.Node;
import me.hagen.pg.NodeDao;

public class PageRankEstimatorTest {

	@Test
	public void testPageRankEstimator() throws SQLException {
		String id = "<http://dbpedia.org/resource/Xi_Jinping>";
		//String id = "<http://dbpedia.org/resource/Jinping_District>";
		Connection conn = Conn.getConnection();
		TDBConnection tdb = TDBConnection.getConnection();
		PageRankEstimator esti = new PageRankEstimator(tdb, conn);
		double estimate = esti.estimatePageRank(id,false);
		System.out.println(estimate);
		NodeDao ndao = new NodeDao();
		Node n = ndao.get(conn, id);
		if(n == null){
			System.out.println("node is not found!");
			return ;
		}
		System.out.println(n.getPscore());
		//System.out.println(estimate/0.85);
		tdb.close();
		conn.close();
	}

}
