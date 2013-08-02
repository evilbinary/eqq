package org.evilbinary.client;

public class GroupMessage {
	 private String messageId;
	 private String fromUin;
	 private String toUin;
	 private String messageId2;
	 private String messageType;
	 private String replyIp;
	 private String groupCode;
	 private String sendUin;
	 private String seq;
	 private String time;
	 private String infoSeq;
	 private String content;
	 private String font;
	 
	 public GroupMessage(){
		 
	 }

	public GroupMessage(String messageId, String fromUin, String toUin, String messageId2, String messageType,
			String replyIp, String groupCode, String sendUin, String seq, String time, String infoSeq, String content,
			String font) {
		super();
		this.messageId = messageId;
		this.fromUin = fromUin;
		this.toUin = toUin;
		this.messageId2 = messageId2;
		this.messageType = messageType;
		this.replyIp = replyIp;
		this.groupCode = groupCode;
		this.sendUin = sendUin;
		this.seq = seq;
		this.time = time;
		this.infoSeq = infoSeq;
		this.content = content;
		this.font = font;
	}
	public void print(){
		 System.out.println("messageId="+messageId);
		 System.out.println("fromUin="+fromUin);
		 System.out.println("toUin="+toUin);
		 System.out.println("messageId2="+messageId2);
		 System.out.println("messageType="+messageType);
		 System.out.println("replyIp="+replyIp);
		 System.out.println("groupCode="+groupCode);
		 System.out.println("sendUin="+sendUin);
		 System.out.println("seq="+seq);
		 System.out.println("time="+seq);
		 System.out.println("infoSeq="+infoSeq);
		 System.out.println("content="+content);
		 System.out.println("font="+font);
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

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getSendUin() {
		return sendUin;
	}

	public void setSendUin(String sendUin) {
		this.sendUin = sendUin;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getInfoSeq() {
		return infoSeq;
	}

	public void setInfoSeq(String infoSeq) {
		this.infoSeq = infoSeq;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}
	 
}
