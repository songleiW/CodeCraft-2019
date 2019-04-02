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
import com.common.Road;
import com.common.Station;
import com.huawei.Main;

public class CreateStation {
	public static ArrayList<String> readStation(String crossPath, Hashtable<String, Station> stations) {
		try (FileReader reader = new FileReader(crossPath); BufferedReader br = new BufferedReader(reader)) {
			String line = br.readLine();
			ArrayList<String> stationID=new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				String[] strings = line.substring(1, line.length() - 1).split(",");
				Station station = new Station();
				// #(id,roadId,roadId,roadId,roadId)
				station.setId(strings[0].trim());
				station.setToRoad(strings[1].trim(),strings[2].trim(),
						            strings[3].trim(),strings[4].trim());
				stations.put(station.getId().trim(), station);
				stationID.add(station.getId());
				Main.crossList.add(station.getId());
			}
			return stationID;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
