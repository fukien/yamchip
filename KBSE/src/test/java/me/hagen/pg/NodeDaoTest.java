package me.hagen.pg;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Test;

public class NodeDaoTest {
	@Test
	public void testPrefix() throws ClassNotFoundException, SQLException{
		try(Connection conn = Conn.getConnection()){
			NodeDao dao = new NodeDao();
			List<Node> node = dao.prefixMatch(conn, "United", 10);
			for(Node n : node){
				System.out.println(n.getName());
		}			}

}
	//@Test
	public void testUpdate2() throws SQLException, IOException
	{
		try(Connection conn = Conn.getConnection()){
			conn.setAutoCommit(false);
			//Statement stmt = conn.createStatement();
			PreparedStatement psmt = conn.prepareStatement("update node set wsum=? where id =?");
			String url = "/E/git/dbp38-node-weight.tsv";
			BufferedReader br = new BufferedReader(new FileReader(url));
			String ln = null;
			int i = 0;
			while((ln=br.readLine())!=null){
				String[] arr = ln.split("\t");
				if(arr[0].startsWith("<http://dbpedia.org/resource/")){
					i++;
					//if(i<172000)continue;
					String id = arr[0].replaceAll("'", "''");
					double key = Double.parseDouble(arr[1]);
					//stmt.addBatch("update node set diverse="+key+" where id = E'"+id+"'");
					psmt.setDouble(1, key);
					psmt.setString(2, id);
					psmt.addBatch();
					
					//System.out.println(i);
					if(i%10000==0){
						psmt.executeBatch();
						conn.commit();
						System.out.println(i);
					}
				}
			}
			br.close();
			psmt.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}
		
	}
	//@Test
	public void testUpdate() throws SQLException, IOException
	{
		try(Connection conn = Conn.getConnection()){
			conn.setAutoCommit(false);
			//Statement stmt = conn.createStatement();
			PreparedStatement psmt = conn.prepareStatement("update node set diverse=? where id =?");
			String url = "/E/git/diverse095.info";
			BufferedReader br = new BufferedReader(new FileReader(url));
			String ln = null;
			int i = 0;
			while((ln=br.readLine())!=null){
				String[] arr = ln.split("\t");
				if(arr[0].startsWith("<http://dbpedia.org/resource/")){
					i++;
					if(i<172000)continue;
					String id = arr[0].replaceAll("'", "''");
					double key = Double.parseDouble(arr[1]);
					//stmt.addBatch("update node set diverse="+key+" where id = E'"+id+"'");
					psmt.setDouble(1, key);
					psmt.setString(2, id);
					psmt.addBatch();
					
					//System.out.println(i);
					if(i%10000==0){
						psmt.executeBatch();
						conn.commit();
						System.out.println(i);
					}
				}
			}
			br.close();
			psmt.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}
		
	}
	//@Test
	public void testAdd() throws ClassNotFoundException, SQLException, IOException {
		try(Connection conn = Conn.getConnection()){
			NodeDao dao = new NodeDao();
			String path = "/E/git/dbp38-ppr.tsv.100.sort";
			BufferedReader br = new BufferedReader(new FileReader(path));
			int i = 0;
			String ln = null;
			while((ln=br.readLine())!=null){
				i++;
				if(i%100000==0)System.out.println(i);
				String[] array = ln.split("\t");
				String iri = array[0];
				double value = Double.parseDouble(array[1]);
				String name = iri.substring(iri.lastIndexOf('/')+1, iri.length()-1);
				while(name.contains("_")){
					name = name.replace("_"," ");
				}
				//System.out.println(iri+"\t"+name+"\t"+value);
				Node node = new Node();
				node.setId(iri);
				node.setName(name);
				node.setPscore(value);
				dao.add(conn, node);
			}
			br.close();
			/*
			Node n = new Node();
			n.setId("http://dbpedia.org/resource/ahaha");
			n.setName("ahaha");
			n.setPscore(0.1);

			dao.add(conn, n);*/
		}
	}

}
