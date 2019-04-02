package com.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.common.Car;
import com.common.Channel;
import com.common.Road;
import com.common.Station;

public class CreateCars {
	public static ArrayList<String> readCar(String carPath, Hashtable<String, Car> cars) {

		try (FileReader reader = new FileReader(carPath); BufferedReader br = new BufferedReader(reader)) {
			String line = br.readLine();
			ArrayList<String> carID=new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				String[] strings = line.substring(1, line.length() - 1).split(",");
				Car car = new Car();
				// #(id,from,to,speed,planTime) 
				car.setId(strings[0].trim());//设置车辆ID
				car.setFrom(strings[1].trim());//设置car起点
				car.setTo(strings[2].trim());//设置车辆目的地
				car.setMaxSpeed(Integer.parseInt(strings[3].trim()));//设置车辆最大速度
				car.setPlanTime(Integer.parseInt(strings[4].trim()));//设置计划开始时间
				cars.put(car.getId().trim(), car);//向hashmap中添加car
				carID.add(car.getId());
			}
			return carID;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
