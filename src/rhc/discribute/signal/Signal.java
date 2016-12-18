package rhc.discribute.signal;

import rhc.discribute.SingleInstanceComponent;
import rhc.discribute.signal.signalInterface.HostJoinSignal;
import rhc.discribute.signal.signalInterface.HostRemoveSignal;
import rhc.discribute.signal.signalInterface.MainConnectSignal;
import rhc.discribute.signal.signalInterface.MainDisConnectSignal;
import rhc.discribute.signal.signalInterface.NoMainSignal;
import rhc.discribute.signal.signalTouch.HostJoinSignalTouch;
import rhc.discribute.signal.signalTouch.HostRemoveSignalTouch;
import rhc.discribute.signal.signalTouch.MainConnectSignalTouch;
import rhc.discribute.signal.signalTouch.MainDisConnectSignalTouch;
import rhc.discribute.signal.signalTouch.NoMainSignalTouch;

public enum Signal {

	HOST_JOIN("host_join", HostJoinSignal.class,
			SingleInstanceComponent.getInstance(HostJoinSignalTouch.class)), HOST_REMOVE("host_remove",
					HostRemoveSignal.class,
					SingleInstanceComponent.getInstance(HostRemoveSignalTouch.class)), MAIN_DIS_CONNECT(
							"main_dis_connect", MainDisConnectSignal.class,
							SingleInstanceComponent.getInstance(MainDisConnectSignalTouch.class)), MAIN_CONNECT(
									"main_connect", MainConnectSignal.class,
									SingleInstanceComponent.getInstance(MainConnectSignalTouch.class)), NO_MAIN(
											"no_main", NoMainSignal.class,
											SingleInstanceComponent.getInstance(NoMainSignalTouch.class));

	public String desc;
	public Class connectClass;
	public SignalTouch touch;

	Signal(String desc, Class connectClass, SignalTouch touch) {
		this.desc = desc;
		this.connectClass = connectClass;
		this.touch = touch;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Class getConnectClass() {
		return connectClass;
	}

	public void setConnectClass(Class connectClass) {
		this.connectClass = connectClass;
	}

	public SignalTouch getTouch() {
		return touch;
	}

	public void setTouch(SignalTouch touch) {
		this.touch = touch;
	}

}
