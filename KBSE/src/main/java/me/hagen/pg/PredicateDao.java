package me.hagen.pg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PredicateDao {
	public Predicate map(ResultSet rs) throws SQLException{
		Predicate n = new Predicate();
		n.setId(rs.getString("id"));
		n.setWeight(rs.getDouble("weight"));
		return n;
	}
	public Predicate get(Connection conn, String id){
		String sql = "select id,weight from predicate where id = ?";
		try(PreparedStatement psmt = conn.prepareStatement(sql)){
			psmt.setString(1, id);
			try(ResultSet rs = psmt.executeQuery()){
				while(rs.next()){
					return map(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Predicate> prefixMatch(Connection conn,String name,int top) throws SQLException{
		String sql = "select id,weight from predicate where id like '<http://dbpedia.org/ontology/"+name+"%>' limit "+top;
		List<Predicate> nodeList = new ArrayList<Predicate>();
		try(PreparedStatement psmt = conn.prepareStatement(sql)){
			try(ResultSet rs = psmt.executeQuery()){
				while(rs.next()){
					nodeList.add(map(rs));
				}
			}
		}
		return nodeList;
	}
}
