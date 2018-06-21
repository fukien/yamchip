package me.hagen.kg;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class TDBConnectionTest {

	//@Test
	public void parse() throws IOException{
		String path = "/J/dataset/dbp/dbp-ttl-3.9/short_abstracts_en.ttl";
		BufferedReader br = new BufferedReader(new FileReader(path));
		String path2 = "/J/dataset/dbp/dbp-ttl-3.9/short_abstracts_en.nt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(path2));
		String ln = null;
		int i = 0;
		while((ln=br.readLine())!=null){
			if(ln.startsWith("#"))continue;
			String subject = ln.substring(0, ln.indexOf(' '));
			String left = ln.substring(ln.indexOf(' ')+1, ln.length());
			String predicate = left.substring(0,left.indexOf(' '));
			String object = left.substring(left.indexOf(' ')+1);
			if(object.endsWith(" .")){
				object = object.substring(0, object.lastIndexOf(' '));
			}
			bw.write(subject+"\t"+predicate+"\t"+object+"\t.\n");
		}
		bw.flush();
		bw.close();
	}
	//@Test
	public void testInsert() throws IOException {
		TDBConnection conn = TDBConnection.getConnection();
		//conn.insert("<http://dbpedia.org/resource/ahaha>", "<http://dbpedia.org/resource/ahaha>", "<http://dbpedia.org/resource/ahaha>");
		String path = "/J/dataset/dbp/dbp-ttl-3.9/short_abstracts_en.ttl";
		BufferedReader br = new BufferedReader(new FileReader(path));
		String ln = null;
		int i = 0;
		while((ln=br.readLine())!=null){
			if(ln.startsWith("#"))continue;
			String subject = ln.substring(0, ln.indexOf(' '));
			String left = ln.substring(ln.indexOf(' ')+1, ln.length());
			String predicate = left.substring(0,left.indexOf(' '));
			String object = left.substring(left.indexOf(' ')+1);
			if(object.endsWith(" .")){
				object = object.substring(0, object.lastIndexOf(' '));
			}
			//System.out.println(subject+"$$$"+predicate+"$$$"+object+"$$$");
			if(subject.getBytes().length!=subject.length())continue;
			conn.insert(subject, predicate, object);
			i++;
			if(i%10000==0){
				System.out.println(i);
			}
		}
		br.close();
		conn.close();
	}

	@Test
	public void testDelete() {

	}

}
