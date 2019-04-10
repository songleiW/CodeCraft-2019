package com.util;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.common.Car;
import com.common.Road;
import com.common.Station;
import com.huawei.Main;
public class Dijkstra{

  public static Graph g = new Graph(Main.stationID);  //新建地图
  public static void newGraph() throws IOException {
	  for(String roadId:Main.roadID)
		{
			Road road =Main.allRoads.get(roadId);
			//为每条路添加权值  
			g.addEdge(road.getStartId(), road.getEndId(),  road.getLength()*2);//为有向图添加边
			if(road.getIsDuplex())
			{
				g.addEdge(road.getEndId(), road.getStartId(), road.getLength()*2);
			}
		}
  }
  //使用动态迪杰斯特拉算法获得下一步想要去的路
  public static boolean getRoute(Car car){
		String startId;
		if(car.isPreSet())//如果是预置车辆
		{
			if(car.nowOnChannel==null)//第一次发车
			{
				car.nextChannels=car.nextChannelList.get(0);//下一步想要去的车道
			}
			else {
				//下一步的目标
				car.nextChannels=car.nextChannelList.get(car.realRoute.indexOf(car.nowOnChannel.getId())+1);
			}
			return true;
		}
		if(car.nowOnChannel==null)
		{
			startId=car.getFrom();
		}
		else {
			startId=car.nowOnChannel.getTo();
		}
		Vertex vStart=g.getVertex(startId);//得到起点站点
		if(car.nowOnChannel!=null)//加大当前道路的权值 防止原路返回
		{
			g.updateEdge(car.nowOnChannel.getTo(), car.nowOnChannel.getFrom(), 10000000.0);
		}
		calculate(vStart);	
		Vertex vEnd=g.getVertex(car.getTo());
		if(car.nowOnChannel!=null)
		{
			g.updateEdge(car.nowOnChannel.getTo(),car.nowOnChannel.getFrom(), -10000000.0);
		}		
		vEnd.path.add(vEnd);
		car.setPath(vEnd.path.get(0).ID,vEnd.path.get(1).ID);
		initGraph(g);
		return true;
	}
  //计算地图权值
  public static void calculate(Vertex source){
		
		source.minDistance = 0;
		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
		queue.add(source);
		
		while(!queue.isEmpty()){
			
			Vertex u = queue.poll();
		
			for(Edge neighbour:u.neighbours){
				double newDist = u.minDistance+neighbour.weight;	//越远的节点需要增大权值
				
				if(neighbour.target.minDistance>newDist){
					queue.remove(neighbour.target);
					neighbour.target.minDistance = newDist;
					neighbour.target.path = new LinkedList<Vertex>(u.path);
					neighbour.target.path.add(u);
					queue.add(neighbour.target);					
				}
			}
		}
	}
  //由于每次都是计算一个节点的最短路径  因此每次计算需要初始化
  public static void initGraph(Graph g)
	{
		for(String stationId:Main.stationID)
		{
			Vertex v=g.getVertex(stationId);
			v.path.clear();
			v.minDistance=Integer.MAX_VALUE;
		}
	}
}

