package org.evilbinary.client;

public class Message {
	private String retcode;
	private String result;
//	private String pollType;
//	private String value;
	// private String size;
	// private String color;
	// private String style;
	// private String name;

	public Message() {

	}

	public Message(String retcode, String pollType, String value) {
		super();
//		this.retcode = retcode;
//		this.pollType = pollType;
//		this.value = value;
	}

	public void print() {
		System.out.println("retcode:" + retcode);
		System.out.println("result:" + result);
		System.out.println();

	}

	 
	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	 

}
