package com.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import com.huawei.Main;

public class Station {
//#(id,roadId,roadId,roadId,roadId)
	private String ID;
	public ArrayList<String> toRoad=new ArrayList<String>();//路口通向的道路
	public ArrayList<String> toStation=new ArrayList<String>();//路口通向的站点
	private ArrayList<Integer> List=new ArrayList<Integer>();
	public ArrayList<Road> roadList=new ArrayList<Road>();
	public String getId() {
		return ID;
	}

	public void setId(String id) {
		this.ID = id;
	}
	
	public void setToRoad(String Up,String Right,String Down,String Left) {//设置通向的路
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
	public String getToStation(int i) {//根据编号得到通往的站点
		return toStation.get(i);
	}
	public String getToRoad(int i) {//根据编号得到通往的路的编号
		return toRoad.get(i);
	}
	private void setToStation() {//设置通向的站点
		Road road =new Road();
		for(String roadId:toRoad)
		{
			if(roadId==null)
			{
				toStation.add(null);
				continue;
			}
			List.add(Integer.parseInt(roadId));//添加进道路列表 ，便于遍历
			road=Main.allRoads.get(roadId);
			if(road.getStartId().equals(ID))//此条路的起点是本站点
			{
				toStation.add(road.getEndId());//把此条路的终点站点加入
			}
			else //这条路的起点不是本站点
			{
				if(road.getIsDuplex())//如果是双向的
				{
					toStation.add(road.getStartId());//就把起点添加进去
				}
				else {
					toStation.add(null);
				}
			}
		}
		Collections.sort(List);//排序
		for(int roadId:List)//升序遍历道路列表,安排处理车道的顺序
		{
			roadList.add(Main.allRoads.get(String.valueOf(roadId)));
		}
	}
	
}