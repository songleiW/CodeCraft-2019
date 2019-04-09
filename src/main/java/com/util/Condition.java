package com.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.common.Car;
import com.common.Channel;
import com.common.Road;
import com.common.Station;
import com.huawei.Main;

public class Condition {//本类记录一个时间片的地图信息
	public boolean isBackTracked=false;		//本时间片是否回朔过
	public int nowOnRoadCars=0;				//记录当前有所少车在路上
	public int endCarsNumber;		//记录当前有多少车已经完成
	public ArrayList<String> nowOnRoadCarId=new ArrayList<String>();		//目前在路上的车的ID
	public ArrayList<String> nonPriorityCarsId=new ArrayList<String>();		//非优先的未发车ID
	public ArrayList<String> priorityCarsId=new ArrayList<String>();		//优先车未发车ID
	public Hashtable<String,road> historyRoads=new Hashtable<String, road>();
	public long Tsum;
	public int priorityLatestEndTime;
	public long Tsumpri;
	public Graph historyGraph = new Graph(Main.stationID);
	public void copyRoadCondition() {
		for(String roadId:Main.roadID)
		{
			Road nowRoad =Main.allRoads.get(roadId);
			road histioryRoad=new road(nowRoad.getIsDuplex(),nowRoad.getChannelNumber());
			historyRoads.put(roadId, histioryRoad);
			for(int i=0;i<nowRoad.getChannelNumber();i++)//先遍历正向车道
			{
				ManageOneChannel(nowRoad.forwardChannel.get(i),histioryRoad.forwardChannels.get(i));
			}
			if(nowRoad.getIsDuplex())//如果是双向车道 再遍历负向
			{
				for(int i=0;i<nowRoad.getChannelNumber();i++)//先遍历正向车道
				{
					ManageOneChannel(nowRoad.reverseChannel.get(i),histioryRoad.reverseChannels.get(i));
				}
			}
		}
	}
	public void ManageOneChannel(Channel nowChannel,channel historyChannel) {
		if(nowChannel.carPortList.size()==0)
		{
			return ;
		}
		for(int i=0;i<nowChannel.carPortList.size();i++)
		{
			Car car=nowChannel.carPortList.get(i);//获取车
			car historyCar=new car();
			historyCar.ID=car.getId();
			historyCar.nowLocation=car.nowLocation;
			historyCar.nowOnChannel=car.nowOnChannel;
			historyCar.realRoute.addAll(car.realRoute);
			historyChannel.carPortArrayList.add(historyCar);
		}
		historyChannel.nowOnChannelCarsNumber=nowChannel.nowCarsNumber;
	}
	public void copyGraph() {
		for(String stationId:Main.stationID)
		{
			Vertex v=Dijkstra.g.getVertex(stationId);
			for(Edge e:v.neighbours)
			{
				historyGraph.addEdge(v.ID, e.target.ID, e.weight);
			}
		}
	}
}

class road{
	public ArrayList<channel> forwardChannels=new ArrayList<channel>();
	public ArrayList<channel> reverseChannels=new ArrayList<channel>();
	public road(boolean isDuplex,int channelsNumber)
	{
		for(int i=0;i<channelsNumber;i++)
		{
			channel channel=new channel();
			forwardChannels.add(channel);
		}
		if(isDuplex)
		{
			for(int i=0;i<channelsNumber;i++)
			{
				channel channel=new channel();
				reverseChannels.add(channel);
			}
		}
	}
}
class channel{
	public int nowOnChannelCarsNumber=0;
	public ArrayList<car> carPortArrayList=new ArrayList<car>();
}
class car{
	public String ID;
	public int nowLocation;
	public ArrayList<String> realRoute=new ArrayList<String>();//表示车辆实际走过的路线
	public Channel nowOnChannel;//现在所在的车道
}