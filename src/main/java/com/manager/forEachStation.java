package com.manager;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Enumeration;

import com.common.Car;
import com.common.Channel;
import com.common.Road;
import com.common.Station;
import com.huawei.*;
import com.util.Dijkstra;
public class forEachStation {
	public static void searchStation() {
		if(Main.nowOnRoadCars==0)return;//当前没有车在路上
		for(String ID:Main.stationID)//遍历站点
		{
			Station station=Main.allStations.get(ID);//获得对应站点
			manageOneStation(station);//处理这个站
		}
	}
	public static void manageOneStation(Station station) {//处理一个站
		Car car =new Car();//存放每个第一优先级的车
		for(Road roadTemp:station.roadList)//依次按顺序处理路
		{
			while(true)//一直处理到本路没有车可以处理
			{
				//roadList不会有空，因此不必处理空
				car=getFirstCarOfRoad(station,roadTemp);
				if(car==null)     
				{
					break;
				}
				Channel lastChannel=car.nowOnChannel;
				if(!successCrossOneCar(car, station))//处理此车  如果有冲突车辆　则下一轮在调度
				{
					break;
				}
				forEachRoad.ManageOneChannel(lastChannel);//上一辆车处理成功了  重新遍历这一车道
			}
		}
	}
	public static boolean successCrossOneCar(Car car,Station station) {
		if(car.getTo().equals(station.getId())) {//车到达终点站
			EndCar.OverTheCar(car);
			return true;//调度成功
		}
		String dir=getDirection(car,station);//更新道路状况后 获得方向 
		ArrayList<Channel> toChannel =car.nextChannels;//获得下一步想要去的路的车道
		String crashRoadId=null;//冲突道路的ID
		Car crashCar=null;//冲突车辆
		int dirNow=station.toRoad.indexOf(car.nowOnChannel.ID);
		if(dir.equals("D"))//直行 不需要判断冲突
		{
			return successThroughCross(car,toChannel,station);
		}
		else if(dir.equals("L"))//左转需要防止与直行的车冲突
		{
			//获得可能冲突的路的状况
			if(dirNow==0)
			{
				crashRoadId=station.toRoad.get(3);
			}
			else {
				crashRoadId=station.getToRoad(dirNow-1);
			}
			if(crashRoadId!=null)//冲突车道存在
			{
				crashCar=getFirstCarOfRoad(station, Main.allRoads.get(crashRoadId));//获取第一辆车
				if(crashCar!=null&&getDirection(crashCar,station).equals("D"))//冲突车辆打算直行
				{
					return false ;//结束，等待下一轮
				}
				//否则
				return successThroughCross(car,toChannel,station);
			}
			//否则冲突车道不存在
			return successThroughCross(car,toChannel,station);
		}
		else if(dir.equals("R"))//打算右转
		{
			String crashRoad1=null;//左转冲突车道
			Car crashCar1=null;//左转冲突车辆
			//先处理直行冲突车辆
			if(dirNow==3)
			{
				crashRoadId=station.toRoad.get(0);
			}
			else {
				crashRoadId=station.getToRoad(dirNow+1);
			}
			if(dirNow==0||dirNow==1)
			{
				crashRoad1=station.toRoad.get(dirNow+2);
			}
			else 
			{
				crashRoad1=station.toRoad.get(dirNow-2);
			}
			if(crashRoadId!=null)//如果直行车道冲突存在
			{
				crashCar=getFirstCarOfRoad(station, Main.allRoads.get(crashRoadId));//获取第一辆车
				if(crashCar!=null&&getDirection(crashCar,station).equals("D"))
				{
					return false;//结束，等待下一轮
				}
			}
			if(crashRoad1!=null)//如果左转冲突存在
			{
				crashCar1=getFirstCarOfRoad(station, Main.allRoads.get(crashRoad1));//获取第一辆车
				//冲突车辆存在或打算直行或左转
				if(crashCar1!=null&&getDirection(crashCar1,station).equals("L"))
				{
					return false;//结束，等待下一轮
				}
			}
			//否则冲突车辆不存在
			return successThroughCross(car,toChannel,station);
		}
		return false;//没卵用  
	}
	public static boolean successThroughCross(Car car,ArrayList<Channel> channels,Station station)
	{
		Channel channel=getSpareChannel(channels);
		if(channel ==null)//车道已满。本车就只能在当前车道行驶
		{
			//车辆前进到本道路最末位置
			car.setEndThisTime();//结束这一周期
			car.nowLocation=car.nowOnChannel.getLegnth();
			Main.NumberEndThisTime++;
			return true;//调度成功
		}
		//车道未满
		int nowRoadDistance;//当前道路可行驶的最大距离
		int nextRoadDistance;//下一道路可行驶的最大距离
		int nowSpeed;//当前道路的最大行驶速度;
		int nextSpeed;//吓一条道路的可行驶的最大速度
		nowRoadDistance=car.nowOnChannel.getLegnth()-car.nowLocation;//当前道路可行驶的距离
		nextRoadDistance=Math.min(car.getMaxSpeed(), channel.getMaxSpeed());//下一条路可行是的距离
		if(nextRoadDistance-nowRoadDistance>0)//可以进入道路
		{
			if(channel.nowCarsNumber==0)//车道内为空
			{
				car.nowOnChannel.removeCar(car);//从上一车道移除
				//加入下一车道
				car.setEndThisTime();//结束这一周期的调度
				car.nowLocation=nextRoadDistance-nowRoadDistance;
				Main.NumberEndThisTime++;
				car.nowOnChannel=channel;
				channel.addCar(car);
				return true;//调度成功
			}
			else //车道内非空
			{
				Car frontCar=channel.carPortList.get(channel.carPortList.size()-1);//获得前车
				if(frontCar.nowLocation>(nextRoadDistance-nowRoadDistance))//与前车的距离够行驶
				{
					car.nowOnChannel.removeCar(car);//从上一车道移除
					//加入下一车道
					car.setEndThisTime();//结束这一周期的调度
					car.nowLocation=nextRoadDistance-nowRoadDistance;
					Main.NumberEndThisTime++;
					car.nowOnChannel=channel;
					channel.addCar(car);
					return true;//调度成功
				}
				else //前车的距离不够行驶
				{
					if (frontCar.isEndThisTime())
					{
						car.nowOnChannel.removeCar(car);//从上一车道移除
						//加入下一车道
						car.setEndThisTime();//结束这一周期的调度
						car.nowLocation=frontCar.nowLocation-1;
						Main.NumberEndThisTime++;
						car.nowOnChannel=channel;
						channel.addCar(car);
						return true;//调度成功
					}
					else //等待前车
					{
						return false;//调度失败
					}
				}
			}
		}
		else //距离不够行驶 只能在本车道行驶
		{
			//车辆前进到本道路最末位置
			car.setEndThisTime();//结束这一周期
			car.nowLocation=car.nowOnChannel.getLegnth();
			Main.NumberEndThisTime++;
			return true;//调度成功
		}
	}
	public static Channel getSpareChannel(ArrayList<Channel> channels) {//获取空闲的车道
		Car car;
		for(Channel channel:channels)
		{
			if(channel.nowCarsNumber==0)//当前车道为空
			{
				return channel;
			}
			car=channel.carPortList.get(channel.carPortList.size()-1);//获取最后一辆车
			if(car.isEndThisTime()&&car.nowLocation==1)//前车已经行驶完毕，并且在第一个位置 就遍历下一车道
			{
				continue;
			}
			else//前车没有行驶完毕，说明此车道可以行驶　就返回这一车道
			{
				return channel;
			}
		}
		return null;//返回空，车道已满,后车就可以结束这一阶段的行驶了
	}
	public static  String getDirection(Car car,Station nowStation) {//用于获得车的前进方向
		if(car.getTo().equals(nowStation.getId()))//本车需要出站，但是在最后一条路上
		{
			return "D";//也需要按直行处理
		}
		if(car.nextTurn!=null)//已经选择过方向了
		{
			return car.nextTurn;
		}
		//否则  还没有选择过方向
		Dijkstra.getRoute(car);//更新车的路径选择
		String nowOnRoad=car.nowOnChannel.ID;//目前所在道路的ID
		String nextToRoad=car.nextChannels.get(0).ID;//下一步想要去的道路的ID
		int indexNow,indexNext;
		indexNow=nowStation.toRoad.indexOf(nowOnRoad);
		indexNext=nowStation.toRoad.indexOf(nextToRoad);
		int temp=indexNow-indexNext;
		if(temp%2==0)//直行
		{
			car.nextTurn="D";
			return "D";
		}
		if(temp==-1||temp==3)//左转
		{
			car.nextTurn="L";
			return "L";
		}
		car.nextTurn="R";
		return "R";
	}
	public static Car getFirstCarOfRoad(Station station,Road road)//获得道路的第一优先级可以动的车
	{
		ArrayList<Channel> channels=new ArrayList<Channel>();
		ArrayList<Car> cars=new ArrayList<Car>();
		if(road.getEndId().equals(station.getId()))//道路的终点是本站点
		{
			channels=road.forwardChannel;
    	}
    	else if(road.getIsDuplex())//道路是双向的,则起点是本站点，选择负向车道
    	{
    		channels=road.reverseChannel;
    	}
    	else//单向车道 且终点不是本站 
    	{
			return null;
		}
		for(Channel channel:channels)
		{
			if(channel.carPortList.size()!=0)
			{
				cars.add(channel.carPortList.get(0));//本车道第一位的车
			}
		}
		if(cars.size()==0)//车道内为空
		{
			return null;
		}
		while(true)//找第一优先级的车
		{
			if(cars.size()==0)
			{
				return null;
			}
			Car carResult=cars.get(0);//初始选择第一位车
			for(Car car:cars)
			{
				if(car.nowLocation>carResult.nowLocation)
				{
					carResult=car;
				}
			}
			if(carResult.isEndThisTime())//第一优先级的车已经结束这一周期 则不再处理这一车
			{
				cars.remove(cars.indexOf(carResult));
				continue;
			}
			return carResult;
		}
	}
}
