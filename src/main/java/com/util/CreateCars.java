package com.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.common.Car;
import com.common.Channel;
import com.common.Road;
import com.common.Station;
import com.huawei.Main;
import com.manager.forEachRoad;

public class CreateCars {
	public static int priorityEarlistPlanStartTime=Integer.MAX_VALUE;//所有车辆的最早出发时间
	public static void readCars(String carPath, Hashtable<String, Car> cars) {
		try (FileReader reader = new FileReader(carPath); BufferedReader br = new BufferedReader(reader)) {
			String line = br.readLine();
			ArrayList<Integer> priorityId=new ArrayList<Integer>();
			ArrayList<Integer> nonPriorityId=new ArrayList<Integer>();
			
			int maxSpeedAllCars=0;	//所有车辆的最高车速
			int minSpeedAllCars=Integer.MAX_VALUE;//所有车辆的最低车速
			int earlistPlanStartTimeAllCars=Integer.MAX_VALUE;//所有车辆的最早出发时间
			int latestPlanStartTimeAllCars=0;//所有车辆的最晚出发时间
			Hashtable<Integer, Integer> startIdDistributionAllCars=new Hashtable<Integer, Integer>();//所有车辆的出发点分布
			Hashtable<Integer, Integer> endIdDistributionAllCars=new Hashtable<Integer, Integer>();//所有车辆的终止点分布
			//优先车辆
			int priorityMaxSpeed=0;	//所有车辆的最高车速
			int priorityMinSpeed=Integer.MAX_VALUE;//所有车辆的最低车速
			int priorityLatestPlanStartTime=0;//所有车辆的最晚出发时间
			Hashtable<Integer, Integer> prioritStartIdDistribution=new Hashtable<Integer, Integer>();//所有车辆的出发点分布
			Hashtable<Integer, Integer> prioritEndIdDistribution=new Hashtable<Integer, Integer>();//所有车辆的终止点分布
			while ((line = br.readLine()) != null) {
				String[] strings = line.substring(1, line.length() - 1).split(",");
				Car car = new Car(strings[0].trim(),strings[1].trim(),strings[2].trim(),
						Integer.parseInt(strings[3].trim()),Integer.parseInt(strings[4].trim()),
						Integer.parseInt(strings[5].trim()),Integer.parseInt(strings[6].trim()));
				cars.put(car.getId(), car);		//向hashmap中添加car
				//计算参数
				maxSpeedAllCars=Math.max(car.getMaxSpeed(), maxSpeedAllCars);
				minSpeedAllCars=Math.min(car.getMaxSpeed(), minSpeedAllCars);
				earlistPlanStartTimeAllCars=Math.min(earlistPlanStartTimeAllCars, car.getPlanTime());
				latestPlanStartTimeAllCars=Math.max(latestPlanStartTimeAllCars, car.getPlanTime());
				
				startIdDistributionAllCars.put(Integer.parseInt(car.getFrom()),0);
				endIdDistributionAllCars.put(Integer.parseInt(car.getTo()),0);
				
				if(car.isPriority())
				{
					priorityId.add(Integer.parseInt(car.getId()));//将车的添加进优先车列表
					//计算参数
					priorityMaxSpeed=Math.max(car.getMaxSpeed(), priorityMaxSpeed);
					priorityMinSpeed=Math.min(car.getMaxSpeed(), priorityMinSpeed);
					priorityEarlistPlanStartTime=Math.min(car.getPlanTime(), priorityEarlistPlanStartTime);
					priorityLatestPlanStartTime=Math.max(priorityLatestPlanStartTime,car.getPlanTime());
					prioritStartIdDistribution.put(Integer.parseInt(car.getFrom()),0);
					prioritEndIdDistribution.put(Integer.parseInt(car.getTo()),0);
				}
				else {
					nonPriorityId.add(Integer.parseInt(car.getId()));
				}
			}
			Collections.sort(priorityId);
			Collections.sort(nonPriorityId);
			for(int id:priorityId)
			{
				Main.priorityCarsId.add(String.valueOf(id));
			}
			
			for(int id:nonPriorityId)
			{
				Main.nonPriorityCarsId.add(String.valueOf(id));
			}
			//计算a b
			double a,b;
			a=(priorityId.size()+nonPriorityId.size())*0.05/priorityId.size();
			a+=(maxSpeedAllCars/minSpeedAllCars)*0.2375/(priorityMaxSpeed/priorityMinSpeed);
			a+=(latestPlanStartTimeAllCars/earlistPlanStartTimeAllCars)*0.2375/(priorityLatestPlanStartTime/priorityEarlistPlanStartTime);
			a+=startIdDistributionAllCars.size()*0.2375/prioritStartIdDistribution.size();
			a+=endIdDistributionAllCars.size()*0.2375/prioritEndIdDistribution.size();
			Main.a=a;
			b=(priorityId.size()+nonPriorityId.size())*0.8/priorityId.size();
			b+=(maxSpeedAllCars/minSpeedAllCars)*0.05/(priorityMaxSpeed/priorityMinSpeed);
			b+=(latestPlanStartTimeAllCars/earlistPlanStartTimeAllCars)*0.05/(priorityLatestPlanStartTime/priorityEarlistPlanStartTime);
			b+=startIdDistributionAllCars.size()*0.05/prioritStartIdDistribution.size();
			b+=endIdDistributionAllCars.size()*0.05/prioritEndIdDistribution.size();
			Main.b=b;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void presetCars(String presetPath,Hashtable<String, Car> cars) {
		try (FileReader reader = new FileReader(presetPath); BufferedReader br = new BufferedReader(reader)) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] strings = line.substring(1, line.length() - 1).split(",");
				Car car=cars.get(strings[0].trim());//得到这个车
				car.setMustStartTime(Integer.parseInt(strings[1].trim()));//设置必须出发的时间
				for(int i=2;i<strings.length;i++)
				{
					car.realRoute.add(strings[i].trim());//添加实际的行车路线
				}
				car.setPresetChannels();//设置预置车道
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
