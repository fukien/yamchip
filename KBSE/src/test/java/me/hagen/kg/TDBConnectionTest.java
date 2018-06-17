package me.hagen.kg;

import static org.junit.Assert.*;

import org.junit.Test;

public class TDBConnectionTest {

	@Test
	public void testInsert() {
		TDBConnection conn = TDBConnection.getConnection();
		conn.insert("<http://dbpedia.org/resource/ahaha>", "<http://dbpedia.org/resource/ahaha>", "<http://dbpedia.org/resource/ahaha>");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

}
