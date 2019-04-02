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
				Car car = new Car(strings[0].trim(),strings[1].trim(),strings[2].trim(),
						Integer.parseInt(strings[3].trim()),Integer.parseInt(strings[4].trim()));
				cars.put(car.getId(), car);//向hashmap中添加car
				carID.add(car.getId());
			}
			//按速度对车进行排序  速度大的早发车
			for(int i=0;i<carID.size()-1;i++)
			{
				for(int j=i+1;j<carID.size();j++)
				{
					Car car1=cars.get(carID.get(i));
					Car car2=cars.get(carID.get(j));
					if(car1.getMaxSpeed()<car2.getMaxSpeed())
					{
						carID.set(i, car2.getId());
						carID.set(j, car1.getId());
					}
				}
			}
			return carID;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
