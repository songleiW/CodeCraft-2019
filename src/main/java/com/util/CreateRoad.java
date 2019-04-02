package com.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.common.Car;
import com.common.Road;
import com.common.Station;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class CreateRoad {
	public static ArrayList<String> readRoad(String roadPath, Hashtable<String, Road> roads) {

		try (FileReader reader = new FileReader(roadPath); BufferedReader br = new BufferedReader(reader)) {
			String line = br.readLine();
			ArrayList<String> roadID=new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				String[] strings = line.substring(1, line.length() - 1).split(",");
				Road road = new Road();
				// #(id,length,speed,channel,from,to,isDuplex)
				road.initRoad(strings[0].trim(), //车道ID
						Integer.parseInt(strings[1].trim()), //车道长度
						Integer.parseInt(strings[2].trim()), //车道限速
						Integer.parseInt(strings[3].trim()),//车道数目
						strings[4].trim(), strings[5].trim(), //起点和终点ID
						Integer.parseInt(strings[6].trim())==1);//是否双向 1表示双向 等式成立
				roads.put(road.getId().trim(), road);//添加进map
				roadID.add(road.getId());
			}
			return roadID;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
