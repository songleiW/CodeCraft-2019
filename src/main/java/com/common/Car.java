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
	private int realStartTime;
	private int endTime;
	private boolean isEndThisTime=false;//是否结束这一周期
	
	public boolean isEnd=false;//是否完成整个规划
	public ArrayList<String> realRoute=new ArrayList<String>();//表示车辆实际走过的路线
	public ArrayList<Channel> nextChannels=null;//下一条想要去的路
	public Station nextStation;//下一次想去的站点
	public int nowLocation;//车辆目前在道路的位置
	public Channel nowOnChannel;//现在所在的车道
	public String nextTurn=null;//下一步想要去的方向
	public Car(String id,String from,String to,int maxSpeed,int planTime)
	{
		this.ID=id;
		this.from=from;
		this.to=to;
		this.maxSpeed=maxSpeed;
		this.planStartTime=planTime;
	}
	
	public String getId() {
		return ID;
	}
	public String getFrom() {
		return from;
	}
	public String getTo() {
		return to;
	}
	public int getPlanTime() {
		return planStartTime;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public int getStartTime() {
		return realStartTime;
	}
	public void setStartTime(int startTime) {
		this.realStartTime = startTime;
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
	public void setEndTime(int endTime) {
		this.endTime=endTime;
	}
	public int  getEndTime() {
		return endTime;
	}
	//添加车的行驶路线
	public void setPath(String fromId,String toId) {
		Station startStation=Main.allStations.get(fromId);//当前路口id
		nextStation=Main.allStations.get(toId);//下一站 路口
		
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
}
