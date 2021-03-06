/*
 * Create By EvilBinary СE
 * 2011-10-20
 * rootntsd@gmail.com
 */
package org.evilbinary.client;

public class SystemMessage {
	private String seq;
	private String type;
	private String uiUin;
	private String fromUin;
	private String account;
	private String message;
	private String allow;
	private String stat;
	private String clientType;
	public SystemMessage(){
		
	}
	public SystemMessage(String seq, String type, String uiUin, String fromUin, String account, String message,
			String allow, String stat, String clientType) {
		super();
		this.seq = seq;
		this.type = type;
		this.uiUin = uiUin;
		this.fromUin = fromUin;
		this.account = account;
		this.message = message;
		this.allow = allow;
		this.stat = stat;
		this.clientType = clientType;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUiUin() {
		return uiUin;
	}
	public void setUiUin(String uiUin) {
		this.uiUin = uiUin;
	}
	public String getFromUin() {
		return fromUin;
	}
	public void setFromUin(String fromUin) {
		this.fromUin = fromUin;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAllow() {
		return allow;
	}
	public void setAllow(String allow) {
		this.allow = allow;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	
}
