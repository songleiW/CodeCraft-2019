package com.util;
import java.util.*;
public class Vertex implements Comparable<Vertex>{
  public final String name;
	public ArrayList<Edge> neighbours;
	public LinkedList<Vertex> path;
	public int minDistance = Integer.MAX_VALUE;
	public int compareTo(Vertex other){
		return Double.compare(minDistance,other.minDistance);		
	}
	public Vertex(String name){
		this.name = name;
		neighbours = new ArrayList<Edge>();
		path = new LinkedList<Vertex>();
	}
	public String toString(){
		return name;
	}	
}
