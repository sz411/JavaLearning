package com.imooc;
//1.定义一个类
public class Telphone {
//2.属性（成员变量）有什么
	float screen;
	float cpu;
	float mem;
	//3.方法 干什么
	void call(){
		System.out.println("Telephone有打电话的功能！");
	}
	void sendMessage(){
		System.out.println("screen:"+screen+"CPU:"+cpu+"men:"+mem+"Telephone有发短信的功能！");
	}
}
