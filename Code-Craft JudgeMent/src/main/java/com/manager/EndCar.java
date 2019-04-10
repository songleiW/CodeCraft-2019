package com.manager;

import com.common.Car;
import com.common.Channel;
import com.huawei.Main;
public class EndCar {
	public static void OverTheCar(Car car) {
		Main.thisTimeEndCarsNumber++;
		car.nowOnChannel.removeCar(car);		//移除出车道
		car.setEndThisTime();
		Main.endCarsNumber++; 
		Main.nowOnRoadCarsNumber--;		//在路上的车减一
		Main.nowOnRoadCarId.remove(car.getId());
		car.setEndTime(Main.NOWTIME);
		Main.Tsum+=car.getEndTime()-car.getPlanTime();
		if(car.isPriority())
		{
			Main.priorityLatestEndTime=Main.NOWTIME;
			Main.Tsumpri+=(car.getEndTime()-car.getPlanTime());
		}
	}
}
