package com.huawei;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.manager.StartCars;
import com.manager.forEachRoad;
import com.manager.forEachStation;
import com.util.CreateCars;
import com.util.CreateRoad;
import com.util.CreateStation;
import com.util.Dijkstra;
import com.util.WriteAnswer;
import com.common.Car;
import com.common.Road;
import com.common.Station;
import com.huawei.*;
public class Main {
	private static final Logger logger = Logger.getLogger(Main.class);
	public static int NOWTIME=1;//当前时间片
	public static int nowOnRoadCars=0;//记录当前有所少车在路上
	public static int endCarsNumber=0;//记录当前有多少车已经完成
	public static ArrayList<String> nowOnRoadCarId=new ArrayList<String>();
	public static int NumberEndThisTime=0;//记录结束这一周车的数目
	///////
	public static ArrayList<Integer> historyNowOnRoadCars=new ArrayList<Integer>();//记录当前有所少车在路上
	public static ArrayList<Integer> historyEndCarsNumber=new ArrayList<Integer>();//记录当前有所少车在路上
	public static ArrayList<ArrayList<String>> historyNowOnRoadCarId=new ArrayList<ArrayList<String>>();//记录结束这一周车的数目
	public static int MaxNumberCarsOnRoad=0;
	public static ArrayList<String> roadID=new ArrayList<String>();
	public static ArrayList<String> carID=new ArrayList<String>();//判断还有多少车未发车
	public static ArrayList<String> saveCarID=new ArrayList<String>();//副本
	public static ArrayList<String> stationID=new ArrayList<String>();//只存储对应的ID便于遍历
	public static Hashtable<String, Road> allRoads=new Hashtable<String, Road>();//道路ID 和道路类
	public static Hashtable<String, Station> allStations=new Hashtable<String, Station>();
	public static Hashtable<String, Car> allCars=new Hashtable<String, Car>();
	public static ArrayList<String> crossList=new ArrayList<String>();
	public static int sumTime=0;
	public static boolean graphNum=true;
	public static void main(String[] args) throws IOException {
		long startTime=System.currentTimeMillis();   //获取开始时间
		if (args.length != 4) {
			logger.error("please input args: inputFilePath, resultFilePath");
			return;
		}
		String carPath = args[0];
		String roadPath = args[1];
		String crossPath = args[2];
		String answerPath = args[3];
		logger.info("Start...");
		logger.info("carPath = " + carPath + " roadPath = " + roadPath + " crossPath = " + crossPath
				+ " and answerPath = " + answerPath);

		logger.info("start read input files");
		
		//初始化读取数据
		carID=CreateCars.readCar(carPath, allCars);//创建初始车辆
		roadID=CreateRoad.readRoad(roadPath, allRoads);//创建道路
		stationID=CreateStation.readStation(crossPath, allStations);//创建车辆
		saveCarID.addAll(carID);
		Dijkstra.newGraph();//新建图
		if(carID.get(0).equals("24370"))//地图一
		{
			MaxNumberCarsOnRoad=roadID.size()*24;
			graphNum=true;
		}
		else{//地图二
			MaxNumberCarsOnRoad=roadID.size()*16;
			graphNum=false;
		}

		//主循环
		while(endCarsNumber<allCars.size())//当所有车辆都到达站点
		{
			InitCarState();//初始化车的状态
			forEachRoad.searchRoad();
			while(NumberEndThisTime<nowOnRoadCars)//当所有在路上的车辆都完成本周期
			{
				int lastNumberEndThisTime=NumberEndThisTime;
				int lastnowOnRoadCars=nowOnRoadCars;
				forEachStation.searchStation();//再遍历站点
				if(lastNumberEndThisTime==NumberEndThisTime&&lastnowOnRoadCars==nowOnRoadCars)
				{
					System.out.println("发生锁死！");
					System.exit(0);
				}
			}
			System.out.println("已完成："+endCarsNumber+"未发车："+carID.size()+"在路上:"+nowOnRoadCars+
			           "时间:"+NOWTIME);
			if(nowOnRoadCars<MaxNumberCarsOnRoad)
			StartCars.startNewCars();//新车上路
			//DisplayResult.visulization();
			NOWTIME++;//时间片加一
		}
		//System.out.println("总时间:"+sumTime);
		logger.info("Start write output file");
		WriteAnswer.wtiteAnswer(answerPath);
		//long endTime=System.currentTimeMillis(); //获取结束时间
		//System.out.println("程序运行时间： "+(endTime-startTime)/1000.0+"s");
		//logger.info("总时间:"+(endTime-startTime)/1000.0+"s");
	}
	public static void InitCarState() {
		NumberEndThisTime=0;//所有车都未结束本周期
		Car car=null;
		for(String Id:nowOnRoadCarId)
		{
			car=allCars.get(Id);
			car.setStartThisTime();//设置车都未结束这一周期
			car.nextChannels=null;//位车重新选取下一步需要走的方向
			car.nextTurn=null;
		}
	}
}