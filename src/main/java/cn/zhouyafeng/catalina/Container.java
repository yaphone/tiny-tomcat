package cn.zhouyafeng.catalina;

public interface Container extends Lifecycle {
	public static final String ADD_CHILD_EVENT = "addChild";
	public static final String ADD_VALVE_EVENT = "addValve";
	public static final String REMOVE_CHILD_EVENT = "removeChild";
	public static final String REMOVE_VALVE_EVENT = "removeValve";

}
