package com.util;
import java.util.*;
public class Graph {
  private ArrayList<Vertex> vertices;
	public Graph(int numberVertices){
		vertices = new ArrayList<Vertex>(numberVertices);
		for(int i=0;i<numberVertices;i++){
			vertices.add(new Vertex(Integer.toString(i)));
		}
	}
	
	public void addEdge(int src, int dest, int weight){
		Vertex s = vertices.get(src);
		Edge new_edge = new Edge(vertices.get(dest),weight);
		s.neighbours.add(new_edge);
	}
	public void updateEdge(int src,int dest,int number)
	{
		Vertex s = vertices.get(src);
		Vertex d = vertices.get(dest);
		for(Edge e:s.neighbours)
		{
			if(e.target==d)
			{
				e.weight+=number;//当前边权重加一 或减一  
				break;
			}
		}
	}
	public ArrayList<Vertex> getVertices() {
		return vertices;
	}
	
	public Vertex getVertex(int vert){
		return vertices.get(vert);
	}
}
