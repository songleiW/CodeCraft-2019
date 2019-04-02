package com.manager;
import java.awt.List;
import java.util.ArrayList;

import com.common.Car;
import com.common.Channel;
import com.common.Road;
import com.common.Station;
import com.huawei.*;
import com.util.Dijkstra;
public class StartCars {
	public static void startNewCars() {
		for(int i=0;i<Main.carID.size();i++)
		{
			String ID=Main.carID.get(i);
			Car car=Main.allCars.get(ID);;
			if(car.getPlanTime()<=Main.NOWTIME)//车辆到达发车时间
			{
				//if(car.nextChannels==null)//如果没有规划过路线 规划路线  防止程序运行时间太长
				//{
					Dijkstra.getRoute(car);
				//}
				//Dijkstra.chooseStartChannel(car);
				if(successStartCar(car))//发车成功
				{
					Main.carID.remove(i--);//因为移除了一辆车 因此 需要回退
					Main.nowOnRoadCarId.add(ID);
					Main.nowOnRoadCars++;//在路上的车加一
				}
			}
		}
	}
	public static boolean successStartCar(Car car) {//判断车道是否可进入
		int speed=Math.min(car.getMaxSpeed(), car.nextChannels.get(0).getMaxSpeed());//车可以走的最大速度
		int sum=0;
		for(Channel channel:car.nextChannels)
		{
			sum+=channel.nowCarsNumber;
			if(sum>=car.nextChannels.get(0).getLegnth())//如果当前道路内车的数量超过道路长度  就不发车
				return false;
			
		}
		for(Channel channel:car.nextChannels)//遍历车道
		{
			if(channel.carPortList.size()!=0)//此车不是第一辆车
			{
				Car frontCar=channel.carPortList.get(channel.carPortList.size()-1);//获得最后一个车
				if(frontCar.nowLocation==1)//本车道第一位有车 不能发车
				{
					return false;
				}
				if(frontCar.nowLocation<=speed)//如果不能以最大速度行驶  也不发车
				{
					return false;
				}
				//本车道第一个车位没有车 可以发车
				car.nowOnChannel=channel;//设置车当前所在车道
				car.nowLocation=Math.min(speed,frontCar.nowLocation-1);//可行驶速度 和与前车距离取最小值
				car.setStartTime(Main.NOWTIME);//设置发车时间
				channel.addCar(car);//加入车道
				car.setEndThisTime();
				return true;
			}
			else//此车是第一辆车，即前面没有车
			{
				car.nowOnChannel=channel;
				car.nowLocation=speed;//设置在车道上的位置
				car.setStartTime(Main.NOWTIME);
				channel.addCar(car);
				car.setEndThisTime();
				return true;
			}
		}
		return false;//车道遍历完了 没有找到合适的车道  下一周期再发车
	}
}
