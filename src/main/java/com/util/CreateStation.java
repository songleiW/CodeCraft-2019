package com.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.common.Car;
import com.common.Road;
import com.common.Station;
import com.huawei.Main;

public class CreateStation {
	public static void readStation(String crossPath, Hashtable<String, Station> stations) {
		try (FileReader reader = new FileReader(crossPath); BufferedReader br = new BufferedReader(reader)) {
			String line = br.readLine();
			ArrayList<Integer> stationID=new ArrayList<Integer>();
			while ((line = br.readLine()) != null) {
				String[] strings = line.substring(1, line.length() - 1).split(",");

				// #(id,roadId,roadId,roadId,roadId)
				Station station = new Station(strings[0].trim(),strings[1].trim(),strings[2].trim(),
			            strings[3].trim(),strings[4].trim());
				stations.put(station.getId().trim(), station);
				stationID.add(Integer.parseInt(station.getId()));
			}
			Collections.sort(stationID);
			ArrayList<String> list=new ArrayList<String>();
			for(int i:stationID)
			{
				list.add(String.valueOf(i));
			}
			Main.stationID=list;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
