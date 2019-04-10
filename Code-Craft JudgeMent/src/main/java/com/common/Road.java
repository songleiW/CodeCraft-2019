package com.common;
import java.util.List;

import com.common.Station;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Arrays;
public class Road {
	private String ID;
	private int length;
	private int maxSpeed;
	private int channelNumber;
	public int nowCarsNumber;
	public double forwardFillCoe=0;
	public double reverseFillCoe=0;
	private String startId;
	private String endId;
	private boolean isDuplex;
	public ArrayList<Channel> forwardChannel=new ArrayList<Channel>();//正向车道list
	public ArrayList<Channel> reverseChannel=new ArrayList<Channel>();//反向车道list
	public boolean forward=false;
	public boolean reverse=false;
	//构造函数
	public Road(String id,int Length,int Speed,int channelNumber,String start,String end,boolean IsDuplex) {
		setId(id);//设置车道ID
		setLength(Length);
		setMaxSpeed(Speed);
		setChannelNumber(channelNumber);
		setStartId(start);
		setEndId(end);
		setDuplex(IsDuplex);
		for(int i=0;i<channelNumber;i++)
		{
			forwardChannel.add(new Channel(start,end,Length,Speed,id,channelNumber));//添加正向车道
		}
		if(IsDuplex)
		{
			for(int i=0;i<channelNumber;i++)
			{
				reverseChannel.add(new Channel(end,start,Length,Speed,id,channelNumber));//添加逆向车道
			}
		}
		
	}
	public String getId() {
		return ID;
	}
	private void setId(String id) {
		this.ID = id;
	}
	private void setLength(int Length)
	{
		this.length=Length;
	}
	public int getLength() {
		return length;
	}
	private void setMaxSpeed(int speed)
	{
		this.maxSpeed=speed;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	private void setChannelNumber(int number)
	{
		this.channelNumber=number;
	}
	public int getChannelNumber() {
		return channelNumber;
	}
	private void setStartId(String id)
	{
		this.startId=id;
	}
	public String getStartId() {
		return startId;
	}
	
	private void setEndId(String id)
	{
		this.endId=id;
	}
	public String getEndId() {
		return endId;
	}
	
	public boolean getIsDuplex() {
		return isDuplex;
	}

	private void setDuplex(boolean isDuplex) {
		this.isDuplex = isDuplex;
	}
}
