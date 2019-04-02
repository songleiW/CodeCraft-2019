package com.common;

import java.util.ArrayList;

import com.huawei.Main;
import com.manager.forEachRoad;
import com.util.Dijkstra;

public class Channel {
	private String from;
	private String to;
	private int length;
	public int nowCarsNumber=0;//当前有多少车在本车道上面
	public int maxSpeed;//限速
	public ArrayList<Car> carPortList=new ArrayList<Car>();//存放车的列表
	public ArrayList<Car> historyPortList=new ArrayList<Car>();
	public int historyNowCarsNumber;
	public String ID;
	public Channel(String From,String To,int Length,int MaxSpeed,String id) {//构造函数
		this.from=From;
		this.to=To;
		this.length=Length;
		this.maxSpeed=MaxSpeed;
		ID=id;
	}
	public void setFrom(String From) {
		this.from=From;
	}
	public String getFrom() {
		return this.from;
	}
	
	public void setTo(String To) {
		this.to=To;
	}
	public String getTo() {
		return this.to;
	}
	
	public void setLength(int Length) {
		this.length=Length;
	}
	public int getLegnth() {
		return this.length;
	}
	
	public void setMaxSpeed(int speed) {
		this.maxSpeed=speed;
	}
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	public void addCar(Car car) {
		nowCarsNumber++;//当前车道车的数量加一
		carPortList.add(car);
		int weight=nowCarsNumber;//修改对应道路的权值  防止拥堵 
		
		Dijkstra.g.updateEdge(from,to, weight);
		car.realRoute.add(car.nowOnChannel.ID);//添加实际的行车路线
	}
	public void removeCar(Car car) {
		int weight=nowCarsNumber;
		nowCarsNumber--;
		carPortList.remove(0);
		Dijkstra.g.updateEdge(from, to, -weight);
	}
}
