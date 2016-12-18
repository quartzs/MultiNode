package rhc.discribute.access;

import java.util.concurrent.TimeUnit;

import rhc.discribute.access.signal.HostJoin;
import rhc.discribute.access.signal.HostRemove;
import rhc.discribute.access.signal.MainConnect;
import rhc.discribute.access.signal.MainDisConnect;
import rhc.discribute.access.signal.NoMain;
import rhc.discribute.node.commander.Commander;
import rhc.discribute.node.commander.impl.CommanderImpl;
import rhc.discribute.node.connecter.impl.ZookperConnecter;
import rhc.discribute.signal.Signal;

public class Access1 {

	private static final String PATH = "/config1.properties";

	public static void main(String[] args) throws Exception {
		Commander commander = new CommanderImpl(PATH);

		commander.getSignalManage().connect(Signal.HOST_JOIN, new HostJoin());
		commander.getSignalManage().connect(Signal.HOST_REMOVE, new HostRemove());
		commander.getSignalManage().connect(Signal.MAIN_CONNECT, new MainConnect());
		commander.getSignalManage().connect(Signal.MAIN_DIS_CONNECT, new MainDisConnect());
		commander.getSignalManage().connect(Signal.NO_MAIN, new NoMain());

		// while(true){
		// Thread.sleep(TimeUnit.SECONDS.toMillis(30));
		// connecter.setCurrentHostData("adfdasfdsad");
		// }

		commander.begin();

		Thread.sleep(TimeUnit.DAYS.toMillis(30));
	}

}
