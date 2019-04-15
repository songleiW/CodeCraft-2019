package com.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import com.common.Car;
import com.common.Channel;
import com.huawei.Main;

public class sortStartCars {
	public static ArrayList<String> sortCarsId(ArrayList<String> startCarsId) {
		ArrayList<String> carsId=new ArrayList<String>();
		Hashtable<Integer, ArrayList<String>> carsHashtable=new Hashtable<Integer, ArrayList<String>>();
		for(String id:startCarsId)
		{
			Car car=Main.allCars.get(id);
			if(car.isPreSet()&&car.getMustStartTime()<=Main.NOWTIME)
			{
				if(carsHashtable.containsKey(car.getMustStartTime()))
				{
					carsHashtable.get(car.getMustStartTime()).add(id);
				}
				else {
					ArrayList<String> list=new ArrayList<String>();
					list.add(id);
					carsHashtable.put(car.getMustStartTime(), list);
				}
			}
			if(!car.isPreSet()&&car.getPlanTime()<=Main.NOWTIME)
			{
				if(carsHashtable.containsKey(Main.NOWTIME))
				{
					carsHashtable.get(Main.NOWTIME).add(id);
				}
				else {
					ArrayList<String> list=new ArrayList<String>();
					list.add(id);
					carsHashtable.put(Main.NOWTIME, list);
				}
			}
		}
		int i=0;
		carsId.clear();
		while(!carsHashtable.isEmpty())
		{
			i++;
			if(carsHashtable.containsKey(i))
			{
				for(String id:carsHashtable.get(i))
				{
					Car car=Main.allCars.get(id);
					if(!car.isPreSet()&&Main.MaxNumberCarsOnRoad-Main.nowOnRoadCarsNumber<=carsId.size())
					{
						continue;
					}
					if(car.nextChannels==null)
					{
						Dijkstra.getRoute(car);//函数内部判断是否是预置车辆
					}
					/*if(!car.isPreSet())
					{
						ArrayList<Channel> channels=car.nextChannels;
						int num=0;
						for(Channel channel:channels)
						{
							num+=channel.carPortList.size();
						}
						if(num/channels.size()>3)
						{
							continue;
						}
					}*/
					carsId.add(id);
				}
			}
			carsHashtable.remove(i);
		}
		return carsId;
	}
}
