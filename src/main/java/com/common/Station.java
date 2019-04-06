package com.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import com.huawei.Main;

public class Station {
	private String ID;
	public ArrayList<String> toRoad=new ArrayList<String>();//路口通向的道路
	public ArrayList<String> toStation=new ArrayList<String>();//路口通向的站点
	private ArrayList<Integer> List=new ArrayList<Integer>();
	public ArrayList<Road> roadList=new ArrayList<Road>();
	public Station(String id,String Up,String Right,String Down,String Left )
	{
		this.ID = id;
		if(Integer.parseInt(Up)!=-1)
		{
			toRoad.add(Up);
		}
		else {
			toRoad.add(null);
		}
		if(Integer.parseInt(Right)!=-1)
		{
			toRoad.add(Right);
		}
		else {
			toRoad.add(null);
		}
		if(Integer.parseInt(Down)!=-1)
		{
			toRoad.add(Down);
		}
		else {
			toRoad.add(null);
		}
		if(Integer.parseInt(Left)!=-1)
		{
			toRoad.add(Left);
		}
		else {
			toRoad.add(null);
		}
		setToStation();
	}
	private void setToStation() {//设置通向的站点
		Road road;
		for(String roadId:toRoad)
		{
			if(roadId!=null)
			{
				List.add(Integer.parseInt(roadId));//添加进道路列表 ，便于遍历
			}
		}
		Collections.sort(List);//排序
		for(int roadId:List)//升序遍历道路列表,安排处理车道的顺序
		{
			roadList.add(Main.allRoads.get(String.valueOf(roadId)));
		}
	}
	
	public String getId() {
		return ID;
	}
	public String getToStation(int i) {//根据编号得到通往的站点
		return toStation.get(i);
	}
	public String getToRoad(int i) {//根据编号得到通往的路的编号
		return toRoad.get(i);
	}
}
