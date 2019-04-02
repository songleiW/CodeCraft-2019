package com.util;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.common.Car;
import com.common.Road;
import com.common.Station;
import com.huawei.Main;
public class Dijkstra{

  public static Graph g = new Graph(Main.allStations.size());
  public static void newGraph() throws IOException {
	  
	  //File writeName = new File("config/visulazation.txt");
	  //writeName.delete();
	  //writeName.createNewFile();
	  for(String roadId:Main.roadID)
		{
			Road road =Main.allRoads.get(roadId);
			//为每条路添加权值  
			g.addEdge(Main.crossList.indexOf(road.getStartId()), Main.crossList.indexOf(road.getEndId()),  road.getLength()*2);
			if(road.getIsDuplex())
			{
				g.addEdge(Main.crossList.indexOf(road.getEndId()), Main.crossList.indexOf(road.getStartId()), road.getLength()*2);
			}
		}
  }
  public static void chooseStartChannel(Car car) {
	Vertex startVertex=g.getVertex(Main.crossList.indexOf(car.getFrom()));
	Vertex toVertex=null;
	int maxWeight=0;
	for(Edge e:startVertex.neighbours)
	{
		if(maxWeight<e.weight)
		{
			toVertex=e.target;
		}
	}
	Station fromStation;
	fromStation=Main.allStations.get(car.getFrom());
	int index=fromStation.toStation.indexOf(Main.crossList.get(Integer.parseInt(toVertex.name)));
	String toRoad=fromStation.toRoad.get(index);
	if(Main.allRoads.get(toRoad).getStartId().equals(fromStation.getId()))
	{
		car.nextChannels=Main.allRoads.get(toRoad).forwardChannel;
	}
	else {
		car.nextChannels=Main.allRoads.get(toRoad).reverseChannel;
	}
}
  public static void getRoute(Car car){
		String startId;
		if(car.nowOnChannel==null)
		{
			startId=car.getFrom();
		}
		else {
			startId=car.nowOnChannel.getTo();
		}
		Vertex vStart=g.getVertex(Main.crossList.indexOf(startId));//得到起点站点
		if(car.nowOnChannel!=null)//加大当前道路的权值 防止原路返回
		{
			g.updateEdge(Main.crossList.indexOf(car.nowOnChannel.getTo()), Main.crossList.indexOf(car.nowOnChannel.getFrom()), 10000000);
		}
		
		calculate(vStart);	
		Vertex vEnd=g.getVertex(Main.crossList.indexOf(car.getTo()));
		if(car.nowOnChannel!=null)
		{
			g.updateEdge(Main.crossList.indexOf(car.nowOnChannel.getTo()), Main.crossList.indexOf(car.nowOnChannel.getFrom()), -10000000);
		}		
		vEnd.path.add(vEnd);
		car.setPath(vEnd.path.get(0),vEnd.path.get(1));
		initGraph(g);
	}
	public static void calculate(Vertex source){
		
		source.minDistance = 0;
		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
		queue.add(source);
		
		while(!queue.isEmpty()){
			
			Vertex u = queue.poll();
		
			for(Edge neighbour:u.neighbours){
				int newDist = u.minDistance+neighbour.weight;
				
				if(neighbour.target.minDistance>newDist){
					// Remove the node from the queue to update the distance value.
					queue.remove(neighbour.target);
					neighbour.target.minDistance = newDist;
					
					// Take the path visited till now and add the new node.s
					neighbour.target.path = new LinkedList<Vertex>(u.path);
					neighbour.target.path.add(u);
					
					//Reenter the node with new distance.
					queue.add(neighbour.target);					
				}
			}
		}
	}
	public static void initGraph(Graph g)
	{
		for(Vertex v:g.getVertices())
		{
			v.path.clear();
			v.minDistance=Integer.MAX_VALUE;
		}
	}
}

