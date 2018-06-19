package me.hagen.pg;

public class Node {
	private String id;
	private String name;
	private double pscore;
	private double pscore2;
	private double diverse;
	private double wsum;
	private double wsum2;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPscore() {
		return pscore;
	}
	public void setPscore(double pscore) {
		this.pscore = pscore;
	}
	public double getPscore2() {
		return pscore2;
	}
	public void setPscore2(double pscore2) {
		this.pscore2 = pscore2;
	}
	public double getDiverse() {
		return diverse;
	}
	public void setDiverse(double diverse) {
		this.diverse = diverse;
	}
	public double getWsum() {
		return wsum;
	}
	public void setWsum(double wsum) {
		this.wsum = wsum;
	}
	public double getWsum2() {
		return wsum2;
	}
	public void setWsum2(double wsum2) {
		this.wsum2 = wsum2;
	}
	
	
}
