package com.common;
import java.util.List;

import com.common.Station;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Arrays;
public class Road {
	// #(ID,length,maxSpeed,channel,startId,endId,isDuplex)
	private String ID;
	private int length;
	private int maxSpeed;
	private int channelNumber;
	public int nowCarsNumber;
	public ArrayList<Channel> forwardChannel=new ArrayList<Channel>();//正向车道list
	public ArrayList<Channel> reverseChannel=new ArrayList<Channel>();//反向车道list
	private String startId;
	private String endId;
	boolean isDuplex;
	
	public String getId() {
		return ID;
	}
	public void setId(String id) {
		this.ID = id;
	}
	public void setLength(int Length)
	{
		this.length=Length;
	}
	public int getLength() {
		return length;
	}
	public void setMaxSpeed(int speed)
	{
		this.maxSpeed=speed;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public void setChannelNumber(int number)
	{
		this.channelNumber=number;
	}
	public int getChannelNumber() {
		return channelNumber;
	}
	public void setStartId(String id)
	{
		this.startId=id;
	}
	public String getStartId() {
		return startId;
	}
	
	public void setEndId(String id)
	{
		this.endId=id;
	}
	public String getEndId() {
		return endId;
	}
	
	public boolean getIsDuplex() {
		return isDuplex;
	}

	public void setDuplex(boolean isDuplex) {
		this.isDuplex = isDuplex;
	}
	//初始化道路
	public void initRoad(String id,int Length,int Speed,int channelNumber,String start,String end,boolean IsDuplex) {
		setId(id);//设置车道ID
		setLength(Length);
		setMaxSpeed(Speed);
		setChannelNumber(channelNumber);
		setStartId(start);
		setEndId(end);
		setDuplex(IsDuplex);
		for(int i=0;i<channelNumber;i++)
		{
			forwardChannel.add(new Channel(start,end,Length,Speed,id));//添加正向车道
		}
		if(IsDuplex)
		{
			for(int i=0;i<channelNumber;i++)
			{
				reverseChannel.add(new Channel(end,start,Length,Speed,id));//添加逆向车道
			}
		}
		
	}
}