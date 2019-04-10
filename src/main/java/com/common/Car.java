package com.common;

import java.beans.IntrospectionException;
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
	private int mustStartTime;
	private int endTime;
	private boolean isEndThisTime=false;		//是否结束这一周期
	private boolean isPriority=false;
	private boolean isPreset=false;
	public ArrayList<String> realRoute=new ArrayList<String>();		//表示车辆实际走过的路线
	public ArrayList<Channel> nextChannels=null;		//下一条想要去的路
	//对于预置车辆  下一步的车道已经确定了
	public ArrayList<ArrayList<Channel>> nextChannelList=new ArrayList<ArrayList<Channel>>();
	public Station nextStation;		//下一次想去的站点
	public int nowLocation;		//车辆目前在道路的位置
	public Channel nowOnChannel;		//现在所在的车道
	public String nextTurn=null;		//下一步想要去的方向
	public int waitTime=0;		//表示车在当前车道带了多久
	public Car(String id,String from,String to,int maxSpeed,int planTime,int isPiority,int isPreset)
	{
		this.ID=id;
		this.from=from;
		this.to=to;
		this.maxSpeed=maxSpeed;
		this.planStartTime=planTime;
		this.isPriority=isPiority==1?true:false;
		this.isPreset=isPreset==1?true:false;
	}
	public void setPresetChannels()		//设置预置车道信息
	{
		String fromId=from;		//起点是车的起点
		for(String realRouteId:realRoute)
		{
			Road road=Main.allRoads.get(realRouteId);		//获得道路
			if(road.getStartId().equals(fromId))		//路的起点和当前车的所在点吻合
			{
				nextChannelList.add(road.forwardChannel);
				fromId=road.getEndId();		//下一个就是从本路的终点出发
			}
			else{
				nextChannelList.add(road.reverseChannel);
				fromId=road.getStartId();		//下一个就是从本路的起点出发
			}
		}
	}
	public boolean isPriority()
	{
		return isPriority;
	}
	public boolean isPreSet()		//确定是否位预制车辆
	{
		return isPreset;
	}
	public void setMustStartTime(int time)
	{
		this.mustStartTime=time;
	}
	public int getMustStartTime()
	{
		return this.mustStartTime;
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
	public void setEndThisTime() {		//本时间片结束
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
		Station startStation=Main.allStations.get(fromId);		//当前路口id
		nextStation=Main.allStations.get(toId);		//下一站 路口
		
		for(String roadID:nextStation.toRoad)
		{
			if(roadID==null)
			{
				continue;
			}
			Road road =Main.allRoads.get(roadID);
			if(road.getStartId().equals(startStation.getId())		//路的起点和终点吻合
			 &&road.getEndId().equals(nextStation.getId()))
			{
				nextChannels=road.forwardChannel;
				break;
			}
			if(road.getIsDuplex()&&road.getEndId().equals(startStation.getId())		//路的起点和终点吻合
					&&road.getStartId().equals(nextStation.getId()))
			{
				nextChannels=road.reverseChannel;
				break;
			}
		}
	}
}
