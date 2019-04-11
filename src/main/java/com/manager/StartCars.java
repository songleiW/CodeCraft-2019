package com.manager;
import java.awt.List;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import com.common.Car;
import com.common.Channel;
import com.common.Road;
import com.common.Station;
import com.huawei.*;
import com.util.Dijkstra;
public class StartCars {
	public static void startNewCars(ArrayList<String> startCarsId,boolean isManageOneRoad,Channel lastChannel){
		for(int i=0;i<startCarsId.size();i++)
		{
			String id=startCarsId.get(i);
			Car car=Main.allCars.get(id);
			if(Main.nowOnRoadCarsNumber>=Main.MaxNumberCarsOnRoad&&!car.isPreSet())
			{
				continue;	
			}
			if(car.nextChannels==null)
			{
				Dijkstra.getRoute(car);//函数内部判断是否是预置车辆
			}
			if(isManageOneRoad)		//只处理一个道路 
			{
				if(!(car.getFrom().equals(lastChannel.getFrom())&&car.nextChannels.get(0).getTo().equals(lastChannel.getTo())))
				{
					continue;
				}
			}
			if(successStartCar(car))		//发车成功
			{
				if(car.isPriority())		//因为移除了一辆车
				{
					Main.priorityCarsId.remove(id);
					startCarsId.remove(i--);
				}
				else {
					Main.nonPriorityCarsId.remove(id);
					startCarsId.remove(i--);
				}
				Main.nowOnRoadCarId.add(id);
				Main.nowOnRoadCarsNumber++;		//在路上的车加一
				Main.NumberEndThisTime++;		//结束这一周期的车加一
			}
		}
	}
	public static boolean successStartCar(Car car) {		//判断车道是否可进入
		int speed=Math.min(car.getMaxSpeed(), car.nextChannels.get(0).getMaxSpeed());		//车可以走的最大速度
		int sum=0;
		for(Channel channel:car.nextChannels)		//遍历车道
		{
			if(channel.carPortList.size()!=0)		//此车不是第一辆车
			{
				Car frontCar=channel.carPortList.get(channel.carPortList.size()-1);		//获得最后一个车
				if(frontCar.nowLocation==1)		//本车道第一位有车 
				{
					//本车是优先车辆 且前车已经行驶完毕 或 本车不是优先车辆 车道第一位有车  都需要遍历下一车道
					if(!frontCar.isEndThisTime())
					{
						return false;
					}
					continue;
				}
				if(speed<frontCar.nowLocation||!car.isPriority())		//不会和前车冲突或不是优先车辆
				{
					//本车道第一个车位没有车 可以发车
					car.nowOnChannel=channel;//设置车当前所在车道
					car.nowLocation=Math.min(speed,frontCar.nowLocation-1);		//可行驶距离
					car.setStartTime(Main.NOWTIME);		//设置发车时间
					channel.addCar(car);		//加入车道
					car.setEndThisTime();
					return true;
				}
				else 		//本车是优先车辆  且会发生冲突
				{
					if(!frontCar.isEndThisTime())		//且前车没有行驶完毕
					{
						return false;
					}
					else 		//前车行驶完毕
					{
						car.nowOnChannel=channel;		//设置车当前所在车道
						car.nowLocation=frontCar.nowLocation-1;		//可行驶距离
						car.setStartTime(Main.NOWTIME);		//设置发车时间
						channel.addCar(car);		//加入车道
						car.setEndThisTime();
						return true;
					}
				}
			}
			else//此车是第一辆车，即前面没有车
			{
				car.nowOnChannel=channel;
				car.nowLocation=speed;		//设置在车道上的位置
				car.setStartTime(Main.NOWTIME);
				channel.addCar(car);
				car.setEndThisTime();
				return true;
			}
		}
		return false;		//车道遍历完了 没有找到合适的车道  下一周期再发车
	}
}
