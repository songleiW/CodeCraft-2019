package com.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.huawei.Main;
import com.util.Vertex;

public class Car {
	private String ID;
	private String from;
	private String to;
	private int maxSpeed;
	private int planStartTime;
	public ArrayList<String> realRoute=new ArrayList<String>();//表示车辆实际走过的路线
	public ArrayList<Channel> nextChannels=null;//下一条想要去的路
	public Station nextStation=new Station();//下一次想去的站点
	private int startTime;
	private boolean isEndThisTime=false;//是否结束这一周期
	public boolean isEnd=false;//是否完成整个规划
	public int nowLocation;//车辆目前在道路的位置

	public Channel nowOnChannel;//现在所在的车道
	public int endTime;
	public String nextTurn=null;//下一步想要去的方向
	public String getId() {
		return ID;
	}

	public void setId(String id) {
		this.ID = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public int getPlanTime() {
		return planStartTime;
	}

	public void setPlanTime(int planTime) {
		this.planStartTime = planTime;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int Speed) {
		this.maxSpeed = Speed;
	}

	public void setPath(Vertex fromVertex,Vertex toVertex) {
		String id=Main.crossList.get(Integer.parseInt(fromVertex.name));
		Station startStation=Main.allStations.get(id);
		id=Main.crossList.get(Integer.parseInt(toVertex.name));
		nextStation=Main.allStations.get(id);
		
		for(String roadID:nextStation.toRoad)
		{
			if(roadID==null)
			{
				continue;
			}
			Road road =Main.allRoads.get(roadID);
			if(road.getStartId().equals(startStation.getId())//路的起点和终点吻合
										&&road.getEndId().equals(nextStation.getId()))
			{
				nextChannels=road.forwardChannel;
				break;
			}
			if(road.getIsDuplex()&&road.getEndId().equals(startStation.getId())//路的起点和终点吻合
					&&road.getStartId().equals(nextStation.getId()))
			{
				nextChannels=road.reverseChannel;
				break;
			}
		}
	}
	public  void addPath() {
		realRoute.add(nowOnChannel.ID);
	}
	public int getStartTime() {
		return startTime;
	}
	
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public boolean isEndThisTime() {
		return isEndThisTime;
	}

	public void setEndThisTime() {//本时间片结束
		isEndThisTime=true;
	}
	public void setStartThisTime() {
		isEndThisTime=false;
	}
}
