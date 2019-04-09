package com.util;
import java.util.*;

import com.common.Car;
public class Graph {
	private Hashtable<String, Vertex> vertices=new Hashtable<String, Vertex>();//节点map
	public Graph(ArrayList<String> stationId){
		for(String id:stationId)
		{
			vertices.put(id, new Vertex(id));//添加节点
		}
	}
	
	public void addEdge(String src, String  dest, int weight){
		Vertex s = vertices.get(src);
		Edge new_edge = new Edge(vertices.get(dest),weight);
		s.neighbours.add(new_edge);
	}
	public void updateEdge(String src,String dest,Double number)
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
	public Hashtable<String, Vertex> getVertices() {
		return vertices;
	}
	
	public Vertex getVertex(String vert){
		return vertices.get(vert);
	}
}
