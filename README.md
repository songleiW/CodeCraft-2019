# CodeCraft-2019
2019华为软件精英挑战赛 

粤港奥赛区第五名，最后得分3550左右。

   最终版现场赛代码，线上线下一致（因为最后一版代码在笔记本上，所以成绩可能不太一致，但是只是个别参数不同，算法思想还是相同的）。

   能参加这个比赛纯属偶然，当时也就抱着进复赛拿手环的心态，没有特别认真的做，但过了初赛，发现自己的成绩还不错（初赛第七名）就想着冲击一波复赛，能去华为欧洲小镇玩一圈也不错（虽然没有实现这个薅羊毛的想法，嘿嘿嘿）。当时复赛练习赛大概线上五六名左右，心里就想千万别正式赛给我来个第五名啊(这种擦边的经历实在有太多了)，没想到我这个开光的嘴。。。真的应验了。
   不过自己也输的心服口服，因为直到复赛前夕才把线上线下的结果调到一致（待会会说在哪里被坑了），同时前四名的大佬们确实都非常厉害，线上赛从来没有超过他们过，所以自己也没有什么遗憾的，大佬永远是大佬。

×××××××××××××××××××××分割线××××××××接下来就是算法的思想了××××××××××××××××××××××××××××××
   我想我和别的队的核心算法应该差不多（Dijkstra算法找最短路径）但是根据实际情况我对这个算法进行了以下优化（xjbs哈哈）：
   
1.动态更新每条路的权值

   --1.1.每条路的初始权值是：roadLength/roadChannelsNumber（即 路的长度/路的这个方向的车道数目）
   
   因为我们不能单纯用路的长度作为初始权重，因为路越长不代表这段路不是一个好的选择。因为路越长，车位越多，越不容易拥堵，因此我用路的长度除以车道数目（同理，车道数目越多，说明这条路越宽敞。说明这条路可以降低权重，让更多的车选择这里，可以有效的防止拥堵）
   
   --1.2.吸取hashTable线性探测处理冲突的思想，车道上的车数目越多，权值增长越快（如本条路上来第一辆车，+1,来第二辆车，+2等等，来第n辆车本路的权值就是: 
   roadLength/roadChannelsNumber +1+2+3+4+5+...+n）
    
   这样做的优点是，路上的车积累的越多,权值就会变化的越剧烈，可以让更少的车选择这条车道。
   
   但是需要注意，当车离开这条路时要减去当前道路上的车辆数目（-nowCarsOnTheRoadNumber）这是一个比较容易忽视的问题。
    
2.使用dijkstra算法获得前进的方向时只确定下一步的前进方向，就是步步为营，走一步再看下一步的方向，因为道路状态是实时更新的，这样动态的获得前进方向可以使车获得当前时刻的最优选择。

  同时，不是在周期开始为各个车确定方向，而是需要确定方向的时候再确定。
  
  如本车成为第一优先车，需要过路口或其他的车需要获得本车的前进方向用来判断是否发生冲突。
  
  仅在这两个时刻确定车的前进方向（但是确定之后就不能更改了）。

3.关于发车，我尝试了三种策略：

   --1.新上路的车不能走最大速度，不发车
   
   --2.始发车道太拥挤不发车（要不上去也是添堵。。。）
   
   --3.使用dijkstra算法初步获得所有预定路径，如果存在高拥挤路段也不发车。
   
   关于这三种策略都可以有效的降低死锁的风险，但是对成绩提高不大，有的还会降低成绩（因为不管车有没有上路，都在计时）。
  
 4.死锁回朔没有做成功，总是在无限回朔，这个如果可以做出来的话，成绩应该会有很大提高，希望大老能做出来。
 
 ×××××××××××××××××××××××××××××××××又是分割线×××××××××××××××××××××××××××××××××××××××
 最后说一下在调判提器期间我遇到的坑：
 
 1.关于发车，自己的发车顺序和线上判题器的发车顺序有可能不一致的问题。
 
   情况是线上发车是按 车id》实际发车时间来排序的，但是对于预置车辆，预置发车时间在我的算法中并不一定能按时发车的，因此在线下发车排序中，我对每个周期可以出发的车 做如下排序：
   
   数据结构是hashtable< startTime,ArrayList< carID > >
   
   发车时间作为key值，value是车的id用列表,即统一时刻发车的在同一list中。
   
   (由于我程序开始已经对id进行了排序 对优先和非有先分别建立了ArrayList,因此这里添加顺序就是排好序的)
   
   那么对于预置车辆设值的预置发车时间按从小到大排序，相同预置发车时间添加进同一list中，按id排序，之后再将所有这一周期可以出发的非预置车辆添加到hashTable对应key值是NOWTIME（画重点）的列表中，即只要是当前时刻可以发车的非预置车，都统一视作是这一周期发车的（因为线上判题器是按这个实际发车时间排序的），每轮调度开始时，都对优先list和非优先list进行一次这样的操作。
   
   这样就可以使线下线上的发车顺序一致。
   
   （表达能力欠缺，具体代码可以参考src/main/java/com/util/sortStartCars.java）
   
   这一点也是我在复赛前夕才发现的，应该很多判题器不一致的队都忽视了这个问题。
    
2.车的前进方向要专一（不能当一个“渣车”）这是在应用动态规划算法的程序中才会出现问题。

   即这个周期一辆车打算直行，但是过路口失败，只能停留在车道的最前端。
    
   但是下个周期由于我是动态规划，道路状况可能发生变化，此车可能的计算结果可能是右转或者左转。
   
   但事实是他不能这样做，因为此车上一周期的直行有可能导致了别的车道打算右转或者左转的车冲突，但是你这个周期告诉我你不打算直行了，你要右转，这种伤了别人心却说没关系的渣男行为，就会导致线上不一致的问题（代码人生，写代码也是在书写人生，不做渣男，从调度车辆开始，哈哈）。

   关于现场赛预置车辆路径规划问题我现场也没想出来好的算法，只是对发车最晚的百分之十的车视作非预置车辆，对于地图二效果并不好这也导致了地图二成绩不太好。（地图一 1500+分 地图二：1900+分）
   
PS：总的来说这次参赛体验敢还是非常棒的，从赛制的策划，到复赛现场赛（出行，食宿）完全是站在参赛选手的立场考虑问题（还记的由于我们在深圳的动车延误，有可能不能按时参加比赛，宸哥在群里面比我们还着急，让我们打车去广州，还有广州的麦当劳等等的一切），从这可以看出华为是真的想为我们提供一个公平公正的舞台展示自己。

如果对你有帮助，麻烦赏一颗星，哈哈（目的性太强）。    
