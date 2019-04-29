# exercise

**Q:  1.什么是前台任务，为什么需要前台任务，请写一个例子。**

A: 前台服务是那些被认为用户知道并且在系统内存不足的时候不会被系统杀死的服务。前台服务需要给通知栏设置通知消息，，由于这个服务被放到正在运行(Ongoing)标题之下，所以意味着通知只有在这个服务被终止或从前台主动移除通知后才能被解除。
在默认情况下，service默认是在后台运行的，系统的优先级比较低,如果设置了前台服务，service的优先级比较高，被杀掉的概率相对较小。
**使用场景**：

 - 许多音乐播放器在后台运行的时候，会在下拉的通知栏上有正在运行的通知显示给用户，这样可以保证Service不会被轻易杀掉
 - 许多跑步软件，类似keep,咕咚类型的跑步app,需要频繁的更新用户的位置信息，因此常采用前台service来避免service被系统回收，精准上传用户坐标
 
tip:示例代码可参考项目exercise1


**Q:2.  JobScheduler是为了做什么。它的设计目的是什么。请写一个例子。**

A:JobScheduler是为了允许开发者创建在后台执行的job，当某些指定条件被满足时，这些Job将会在后台被执行。它设计的目的是为了我们可以利用JobScheduler来执行这些特殊的后台任务时来减少电量的消耗，同时在Android L引入的空闲模式，在条件符合的情况下，系统服务BindService的方式把应用内Manifest中配置的JobService启动起来，并通过进程间通信Binder方式调用JobService的onStartJob、onStopJob等方法来进行Job的管理。即便在执行任务之前应用程序进程被杀，也不会导致任务中断，Jobservice不会因应用退出而退出，但确实是运行在该应用进程中。
使用场景：

 - 应用具有当插入设备时您希望优先执行的工作（充电时才希望执行的工作备份数据)
 - 需要访问网络或 Wi-Fi 连接的任务(如向服务器拉取内置数据)
 - 希望作为一个批次定期运行的许多任务
 - 应用具有您可以推迟的非面向用户的工作（定期数据库数据更新）
 
tip:示例代码可参考项目exercise2
 

**3.  做一个android app,完成以下功能:**
某项目，有Ｎ个成员，每天开一次晨会.　晨会每天轮流主持，就是今天张三，明天李四...。
现在你需要做一个应用:
    1. 输入名单,编辑名单.
    2. 打开app，可以看到昨天，今天，明天是谁主持会议。
    3. 跳过功能，可以跳过某一个人主持
    4. 查看主持会议历史
    
tip:实例代码可参考项目exercise3
