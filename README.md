# MultiNode
多节点在线，自动监测离线，自动选主，节点打标签

使用zookeeper监测多个节点是否在线，在配置文件里配置以下信息

localIp=localhost #配置的本节点ip，不配置则会在程序里自动获取<br>
localPort=8888 #使用的本节点端口，必须配置<br>
zkHost=localhost:2181 #zookeeper连接地址，多个地址使用,分隔<br>
localLabel=master,client #本节点标签，可以给一个节点打多个标签，<br>
canMain=1 #在主节点失去连接后是否可以成为新的主节点，配置为1则会尝试成为主节点，不配置或其它值不会尝试<br>

当有节点失去连接或主机失去连接会触发相应的信号，一共有五种信号<br>
Signal.HOST_JOIN<br>
节点加入信号，对应信号接口为 rhc.discribute.signal.signalInterface.HostJoinSignal<br>
Signal.HOST_REMOVE<br>
节点离开信号，对应信号接口为 rhc.discribute.signal.signalInterface.HostJoinSignal<br>
Signal.MAIN_CONNECT<br>
主机加入信号，对应信号接口为 rhc.discribute.signal.signalInterface.MainConnectSignal<br>
Signal.MAIN_DIS_CONNECT<br>
主机离开信号，对应信号接口为 rhc.discribute.signal.signalInterface.MainDisConnectSignal<br>
Signal.NO_MAIN<br>
暂无主机信号，对应信号接口为 rhc.discribute.signal.signalInterface.NoMainSignal<br>

关注其中某一种信号时需要实现相应的接口，<br>
然后将实现类实例与信号连接<br>
如 commander.getSignalManage().connect(Signal.HOST_JOIN, new HostJoin());<br>


src/rhc/discribute/Access路径下有使用例子，
