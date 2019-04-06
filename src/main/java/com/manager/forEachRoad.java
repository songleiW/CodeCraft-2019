package com.manager;
import java.awt.List;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.log4j.Logger;

import com.common.Car;
import com.common.Channel;
import com.common.Road;
import com.huawei.*;
import com.manager.EndCar;;
public  class forEachRoad {
	public static void searchRoad() {
		if(Main.nowOnRoadCars==0)
		{
			return;//当前没有车在路上
		}
		for(String roadId:Main.roadID)//遍历每条路
		{
			Road road=Main.allRoads.get(roadId);
			ManagerChannel(road.forwardChannel);//先遍历正向车道
			if(road.getIsDuplex())//如果是双向车道 再遍历负向
			{
				ManagerChannel(road.reverseChannel);//再遍历负向车
			}
		}
	}
	public static void ManageOneChannel(Channel channel) {
		if(channel.nowCarsNumber==0)//当前车道为空
		{
			return;
		}
		for(int i=0;i<channel.carPortList.size();i++)
		{
			Car car=channel.carPortList.get(i);//获取车
			if(car.isEndThisTime())//如果这个车已经结束这一周期
			{
				continue;
			}
			int speed=Math.min(car.getMaxSpeed(),channel.maxSpeed);//最大速度
			int distance=0;//行驶距离
			if(i==0)//第一辆车
			{
				distance=channel.getLegnth()-car.nowLocation;//本车道剩余距离
				if(speed<=distance)//不出本路
				{
					car.setEndThisTime();//结束本周期调度
					car.nowLocation+=speed;//车辆前进
					Main.NumberEndThisTime++;//结束本周期车的数目加一
				}
			}
			else//不是第一辆车
			{
				distance=channel.carPortList.get(i-1).nowLocation-car.nowLocation;//与前车的距离
				if(speed<distance)//不会追尾前车
				{
					car.setEndThisTime();//结束本周期调度
					car.nowLocation+=speed;//车辆前进
					Main.NumberEndThisTime++;//结束本周期车的数目加一
				}
				else //会追尾前车
				{
					if(channel.carPortList.get(i-1).isEndThisTime())//前车已经结束本周期活动  否则的话就等前车走之后再行动
					{
						car.setEndThisTime();//结束本周期调度
						//车辆前进到前车后一位
						car.nowLocation=channel.carPortList.get(i-1).nowLocation-1;
						Main.NumberEndThisTime++;//结束本周期车的数目加一
					}
				}
			}
		}
	}
	public static void ManagerChannel(ArrayList<Channel> channels) {
		for(Channel channel:channels)
		{
			ManageOneChannel(channel);
		}
	}
}
