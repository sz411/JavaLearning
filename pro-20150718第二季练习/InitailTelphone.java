package com.imooc;

public class InitailTelphone {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Telphone phone= new Telphone();
		phone.sendMessage();
		//给实例变量赋值
		phone.screen=5.0f;
		phone.cpu=1.4f;
		phone.mem=2.0f;
		//调用对象的方法
		phone.sendMessage();
	}

}
