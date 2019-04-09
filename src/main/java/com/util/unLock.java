package com.util;

import com.common.Car;
import com.common.Channel;
import com.common.Road;
import com.huawei.Main;

public class unLock {
	public static void backTrack() {
		int unBackRange=10;
		int index=Main.NOWTIME-11;
		while(Main.historyConditions.get(index).isBackTracked)
		{
			System.err.println("已经回朔过"+index);
			for(int i=0;i<unBackRange-1;i++)
			{
				Main.historyConditions.remove(index);
				index--;
			}
			index--;
		}
		Main.NOWTIME=index+1;
		Condition condition=Main.historyConditions.get(index);		//获得上一周期的状况
		condition.isBackTracked=true;//本时间段已经回朔过
		Main.endCarsNumber=condition.endCarsNumber;
		Main.nonPriorityCarsId.clear();
		Main.nonPriorityCarsId.addAll(condition.nonPriorityCarsId);
		Main.priorityCarsId.clear();
		Main.priorityCarsId.addAll(condition.priorityCarsId);
		Main.nowOnRoadCarId.clear();
		Main.nowOnRoadCarId.addAll(condition.nowOnRoadCarId);
		Main.nowOnRoadCars=condition.nowOnRoadCars;
		Main.Tsum=condition.Tsum;
		Main.Tsumpri=condition.Tsumpri;
		Main.priorityLatestEndTime=condition.priorityLatestEndTime;
		unTrackRoad(condition);
		Dijkstra.g=condition.historyGraph;
	}
	static void unTrackRoad(Condition condition) {
		for(String roadId:Main.roadID)
		{
			Road nowRoad =Main.allRoads.get(roadId);
			road histioryRoad=condition.historyRoads.get(roadId);
			for(int i=0;i<nowRoad.getChannelNumber();i++)//先遍历正向车道
			{
				ManageOneChannel(nowRoad.forwardChannel.get(i),histioryRoad.forwardChannels.get(i));
			}
			if(nowRoad.getIsDuplex())//如果是双向车道 再遍历负向
			{
				for(int i=0;i<nowRoad.getChannelNumber();i++)//遍历正向车道
				{
					ManageOneChannel(nowRoad.reverseChannel.get(i),histioryRoad.reverseChannels.get(i));
				}
			}
		}
	}
	static void ManageOneChannel(Channel nowChannel,channel historyChannel) {
		nowChannel.carPortList.clear();
		nowChannel.nowCarsNumber=historyChannel.nowOnChannelCarsNumber;
		if(historyChannel.carPortArrayList.size()==0)
		{
			return;
		}
		for(int i=0;i<historyChannel.carPortArrayList.size();i++)
		{
			car historyCar=historyChannel.carPortArrayList.get(i);
			Car car=Main.allCars.get(historyCar.ID);//获取车
			car.nowLocation=historyCar.nowLocation;
			nowChannel.carPortList.add(car);
			//非预置车辆 这一周期跨越路口了
			car.realRoute.clear();
			car.realRoute.addAll(historyCar.realRoute);
			car.nowOnChannel=historyCar.nowOnChannel;
		}
		if(nowChannel.carPortList.size()!=nowChannel.nowCarsNumber)
		{
			System.err.println(nowChannel.carPortList.size());
			System.err.println(nowChannel.nowCarsNumber);
		}
	}
}
