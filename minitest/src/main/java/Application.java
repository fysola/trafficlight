import java.util.HashMap;
import java.util.Map;

/**
 * @author: create by Young
 * @version: v1.0
 * @description: PACKAGE_NAME
 */

//交通灯类型：普通还是带导向灯
enum Type {
	NORMAL,GUIDANCE
}

//当前红绿灯颜色
enum Signl {
	RED,YELLOW,GREEN
}

//当前汽车行驶状态
enum CurrentAction {
	GO_STRAIGHT_BEHIND //正在停车线后面直行
	,GO_STRAIGHT_OVER  //已过停车线并且在直行
	,TURNING_RIGHT //正在右转
	,TURNING_LEFT //正在左转
}

//当前导向灯信号
enum GuidanceSignl {
	TURN_RIGHT,
	TURN_LEFT
}


class TrafficLightFactory {
	public static TrafficLight getInstance(Type t,GuidanceSignl g) {
		return t.equals(Type.NORMAL) ? new NormalTrafficLight() : new GuidanceTrafficLight(g);
	}
}

interface TrafficLight {
	Boolean pass(CurrentAction action, Signl signl);
}

class NormalTrafficLight implements TrafficLight {
	public Boolean pass(CurrentAction action, Signl signl) {
		System.out.println("当前汽车行驶状态 : "+action);
		System.out.println("当前交通灯类型 : "+Type.NORMAL);
		System.out.println("当前交通灯颜色 : "+signl);
		System.out.println("当前导向灯信号 : 无导向灯");
		switch (signl) {
			case RED:
				return action == CurrentAction.TURNING_RIGHT; //红灯只能等待或者右转
			case YELLOW:
				return action == CurrentAction.GO_STRAIGHT_OVER; //黄灯只允许过线的车直行
			case GREEN:
				return true;
			default:
				return false;
		}
	}
}

class GuidanceTrafficLight implements TrafficLight {
	//导向灯要初始化一个导向属性
	private GuidanceSignl guidanceSignl;
	public GuidanceTrafficLight(GuidanceSignl g) {
		guidanceSignl = g;
	}
	public Boolean pass(CurrentAction action, Signl signl) {
		System.out.println("当前汽车行驶状态 : "+action);
		System.out.println("当前交通灯类型 : "+Type.GUIDANCE);
		System.out.println("当前交通灯颜色 : "+signl);
		System.out.println("当前导向灯信号 : "+guidanceSignl);
		switch (signl) {
			case RED:
				//红灯时如果当前是在右转，并且右转导向灯也亮了，则可右转通行
				//红灯时如果当前是在左转，并且左转导向灯也亮了，则可左转通行
				return (action == CurrentAction.TURNING_RIGHT && guidanceSignl == GuidanceSignl.TURN_RIGHT)
						|| (action == CurrentAction.TURNING_LEFT && guidanceSignl == GuidanceSignl.TURN_LEFT);
			case YELLOW:
				return action == CurrentAction.GO_STRAIGHT_OVER; //黄灯只允许过线的车直行
			case GREEN:
				return true;
				default:
				return false;
		}
	}
}

class Car {
	private CurrentAction action;
	public CurrentAction getAction() {return action;}
	public void setAction(CurrentAction a){ action = a;}
	public Boolean pass(TrafficLight t, Signl s){ return t.pass(action,s); }
}

public class Application {

	public static void main(String args[]) {
		Car car = new Car();
		//代替读取配置项
		car.setAction(CurrentAction.TURNING_RIGHT); //汽车当前行驶状态
		Signl signl = Signl.RED; //当前信号灯:红黄绿
		Type trafficLightType = Type.GUIDANCE; //当前遇到的交通交通灯类型：普通还是导向
		GuidanceSignl guidanceSignl = GuidanceSignl.TURN_RIGHT; //当前导向信号

		//处理规则
		TrafficLight trafficLight = TrafficLightFactory.getInstance(trafficLightType,guidanceSignl); //代替配置注入
		Boolean pass = car.pass(trafficLight,signl);

		System.out.println("是否能通过 : "+pass);
	}
}
