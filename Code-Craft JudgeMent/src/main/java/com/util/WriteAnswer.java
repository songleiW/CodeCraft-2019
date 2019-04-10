package com.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import com.common.Car;
import com.common.Channel;
import com.common.Road;
import com.common.Station;
import com.huawei.Main;

public class WriteAnswer {
	public static void wtiteAnswer(String answerPath) {
		try {
			File writeName = new File(answerPath);
			writeName.createNewFile();
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				for (String carID:Main.saveCarID) {
					Car car = Main.allCars.get(carID);
					String id = car.getId();
					if(car.isPreSet())
					{
						continue;
					}
					List<String> path= car.realRoute;
					String p = path.get(0);
					for (int j = 1; j < path.size(); j++) {
						p = p + "," + path.get(j);
					}
					out.write("(" + id + "," +car.getStartTime()+","+ p + ")\r\n"); 
				}
				out.flush(); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
