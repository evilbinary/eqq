package org.evilbinary.client;

public class BuddyMessage {
	private String messageId;
	private String fromUin;
	private String toUin;
	private String messageId2;
	private String messageType;
	private String replyIp;
	private String time;
	private String content;
	private String font;
	
	public BuddyMessage(){
		
	}
	
	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getFromUin() {
		return fromUin;
	}
	public void setFromUin(String fromUin) {
		this.fromUin = fromUin;
	}
	public String getToUin() {
		return toUin;
	}
	public void setToUin(String toUin) {
		this.toUin = toUin;
	}
	public String getMessageId2() {
		return messageId2;
	}
	public void setMessageId2(String messageId2) {
		this.messageId2 = messageId2;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getReplyIp() {
		return replyIp;
	}
	public void setReplyIp(String replyIp) {
		this.replyIp = replyIp;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void print(){
		System.out.print("fromUin:"+fromUin);
		System.out.print(" toUin:"+toUin);
		System.out.print(" content:"+content);
		
	}
	
}
