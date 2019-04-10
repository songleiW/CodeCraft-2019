//To represent the edges in the graph.
package com.util;
public class Edge{
  public final Vertex target;
	public double weight;
	public Edge(Vertex target, int weight){
		this.target = target;
		this.weight = weight;
	}
	
}
