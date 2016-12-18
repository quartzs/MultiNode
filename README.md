# MultiNode
多节点在线，自动监测离线，自动选主，节点打标签

使用zookeeper监测多个节点是否在线，在配置文件里配置以下信息

localIp=localhost #配置的本节点ip，不配置则会在程序里自动获取
localPort=8888 #使用的本节点端口，必须配置
zkHost=localhost:2181 #zookeeper连接地址，多个地址使用,分隔
localLabel=master,client #本节点标签，可以给一个节点打多个标签，
canMain=1 #在主节点失去连接后是否可以成为新的主节点，配置为1则会尝试成为主节点，不配置或其它值不会尝试
