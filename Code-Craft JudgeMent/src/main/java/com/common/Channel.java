package com.common;

import java.util.ArrayList;

import com.huawei.Main;
import com.manager.forEachRoad;
import com.util.Dijkstra;

public class Channel {
	private String from;
	private String to;
	private String ID;
	private int length;
	public int nowCarsNumber=0;		//当前有多少车在本车道上面
	public int maxSpeed;		//限速
	public ArrayList<Car> carPortList=new ArrayList<Car>();		//存放车的列表
	public int channelNumber=0;
	public Channel(String From,String To,int Length,int MaxSpeed,String id,int num) {
		this.from=From;
		this.to=To;
		this.length=Length;
		this.maxSpeed=MaxSpeed;
		ID=id;
		channelNumber=num;
	}
	public String getId()
	{
		return ID;
	}
	public String getFrom() {
		return this.from;
	}
	public String getTo() {
		return this.to;
	}
	public int getLegnth() {
		return this.length;
	}
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	public void addCar(Car car) {
		nowCarsNumber++;//当前车道车的数量加一
		carPortList.add(car);
		double weight=nowCarsNumber*1.0/(channelNumber*channelNumber);//修改对应道路的权值  防止拥堵 
		Dijkstra.g.updateEdge(from,to, weight);
		Road road=Main.allRoads.get(ID);
		road.nowCarsNumber++;
		if(!car.isPreSet())//不是预置车辆才添加路线
		{
			car.realRoute.add(car.nowOnChannel.ID);//添加实际的行车路线
		}
	}
	public void removeCar(Car car) {
		double weight=nowCarsNumber*1.0/(channelNumber*channelNumber);
		nowCarsNumber--; 
		carPortList.remove(0);
		Dijkstra.g.updateEdge(from, to, -weight);
		Road road=Main.allRoads.get(ID);
		road.nowCarsNumber--;
	}
}
