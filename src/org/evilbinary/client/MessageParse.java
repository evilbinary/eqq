/*
 * Create By EvilBinary 小E
 * 2011-10-20
 * rootntsd@gmail.com
 */
package org.evilbinary.client;

import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParse {
	private Map<String, Group> groups; // gid,group
	private Map<String, User> friends; // uin,user
	private Queue<Message> messages;
	private Map<String, String> session; // uin,sgid

	public MessageParse() {
		groups = null;
		session = null;
		messages = null;
		friends = null;
	}

	public MessageParse(Map<String, Group> groups, Map<String, User> friends, Queue<Message> messages,
			Map<String, String> session) {
		super();
		this.groups = groups;
		this.friends = friends;
		this.messages = messages;
		this.session = session;
	}

	public static String genContent(String value) {
		return "\\\"" + value + "\\\"";
	}

	public static String genFace(String number) {
		return "[\\\"face\\\"," + number + "]";
	}

	public static String genFace(int number) {
		return "[\\\"face\\\"," + number + "]";
	}

	public String[] parseGroupMessage(String message) {
		String regex = "\\{\"msg_id\":(.*?),\"from_uin\":(.*?),\"to_uin\":(.*?),\"msg_id2\":(.*?),\"msg_type\":(.*?),\"reply_ip\":(.*?),\"group_code\":(.*?),\"send_uin\":(.*?),\"seq\":(.*?),\"time\":(.*?),\"info_seq\":(.*?),\"content\":(.*)\\}";
		return parseXXX(message, regex);
	}

	public Vector<String[]> parseGroupMessages(String message) {
		String regex = "\\{\"msg_id\":(.*?),\"from_uin\":(.*?),\"to_uin\":(.*?),\"msg_id2\":(.*?),\"msg_type\":(.*?),\"reply_ip\":(.*?),\"group_code\":(.*?),\"send_uin\":(.*?),\"seq\":(.*?),\"time\":(.*?),\"info_seq\":(.*?),\"content\":(.*)\\}";
		return parseXXXX(message, regex);
	}

	public GroupMessage parseGroupMessage1(String message) {
		String regex = "\"msg_id\":(.*?),\"from_uin\":(.*?),\"to_uin\":(.*?),\"msg_id2\":(.*?),"
				+ "\"msg_type\":(.*?),\"reply_ip\":(.*?),\"group_code\":(.*?),\"send_uin\":(.*?),"
				+ "\"seq\":(.*?),\"time\":(.*?),\"info_seq\":(.*?),\"content\":\\[\\[(.*?)\\}\\],(.*?)\\]\\}";
		GroupMessage msg = new GroupMessage();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(message);
		if (m.find()) {
			msg.setMessageId(m.group(1));
			msg.setFromUin(m.group(2));
			msg.setToUin(m.group(3));
			msg.setMessageId2(m.group(4));
			msg.setMessageType(m.group(5));
			msg.setReplyIp(m.group(6));
			msg.setGroupCode(m.group(7));
			msg.setSendUin(m.group(8));
			msg.setSeq(m.group(9));
			msg.setTime(m.group(10));
			msg.setInfoSeq(m.group(11));
			msg.setFont(m.group(12));
			msg.setContent(m.group(13));

		}
		return msg;

	}

	public void parseUserFriends(String content) {
		String regex = "\"flag\":(.*?),\"uin\":(.*?),\"categories\":(.*?)\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			User user = new User(m.group(1), m.group(2), m.group(3));
			friends.put(m.group(2), user);
			// System.out.println("flag=" + m.group(1) + " uin=" + m.group(2) +
			// " == " + m.group(3));
		}
		regex = "\"vip_level\":(.*?),\"u\":(.*?),\"is_vip\":(.*?)\\}";
		p = Pattern.compile(regex);
		m = p.matcher(content);
		while (m.find()) {
			User u = friends.remove(m.group(2));
			u.setVipLevel(m.group(1));
			u.setIsVip(m.group(3));
			friends.put(m.group(2), u);
		}
		regex = "\"face\":(.*?),\"flag\":(.*?),\"nick\":\"(.*?)\",\"uin\":(.*?)\\}";
		p = Pattern.compile(regex);
		m = p.matcher(content);
		while (m.find()) {
			User u = friends.remove(m.group(4));
			u.setFace(m.group(1));
			u.setNick(m.group(3));
			friends.put(m.group(4), u);
		}
		// printFriends();
	}

	public String[] parseBuddyListChange(String content) {
		String regex = "\\{\"added_friends\":[(.*?)],\"removed_friends\":[(.*?)]}";
		return parseXXX(content, regex);
	}

	public String[] parseAddedFriends(String content) {
		String regex = "\\{\"uin\":(.*?),\"groupid\":(.*?)\\}";
		return parseXXX(content, regex);
	}

	public String[] parseSessionMessage(String content) {
		String regex = "\\{\"msg_id\":(.*?),\"from_uin\":(.*?),\"to_uin\":(.*?),\"msg_id2\":(.*?),\"msg_type\":(.*?),\"reply_ip\":(.*?),\"time\":(.*?),\"id\":(.*?),\"ruin\":(.*?),\"service_type\":(.*?),\"flags\":(.*?),\"content\":(.*)\\}";
		return parseXXX(content, regex);
	}

	public String[] parseC2CMessage(String content) {
		String regex = "\\{\"type\":(.*?),\"value\":\"(.*?)\",\"flags\":(.*?\\})\\}";
		return parseXXX(content, regex);
	}

	public void parseGroupInfoExt(String content, String gid) {
		String regex = "\"result\":\\{\"stats\":\\[(.*?)\\],\"minfo\":\\[(.*?)\\],\"ginfo\":\\{(.*?),\"members\":\\[(.*?)\\],\"option\":(.*?)\\},\"cards\":\\[(.*?)\\],\"vipinfo\":\\[(.*?)\\]\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			// System.out.println(m.group(1));
			// System.out.println(m.group(2));
			// System.out.println(m.group(3));
			// System.out.println(m.group(4));
			// System.out.println(m.group(5));
			// System.out.println(m.group(6));
			// System.out.println(m.group(7));

			parseMinfo(m.group(2), gid); // must first
			parseStats(m.group(1), gid);
			groups.get(gid).setGinfo(m.group(3)); // ginfo 未解析数�?
                        parseMembers(m.group(4), gid);
			// m.group(5); //option
			parseCard(m.group(6), gid);
			parseVipInfo(m.group(7), gid);
		}
	}

	public void parseMinfo(String content, String gid) {
		String regex = "\\{\"nick\":\"(.*?)\",\"province\":\"(.*?)\",\"gender\":\"(.*?)\",\"uin\":(.*?),\"country\":\"(.*?)\",\"city\":\"(.*?)\"\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			User u = new User();
			u.setNick(m.group(1));
			u.setProvince(m.group(2));
			u.setGender(m.group(3));
			u.setUin(m.group(4));
			u.setCountry(m.group(5));
			u.setCity(m.group(6));
			Group g = groups.get(gid);
			if (g != null) {
				// System.out.println(m.group(4));
				g.putUser(m.group(4), u);
			} else {
				System.out.println("gid can not found.");
			}
			// System.out.println(m.group(1));
			// System.out.println(m.group(2));
			// System.out.println(m.group(3));
			// System.out.println(m.group(4));
			// System.out.println(m.group(5));
			// System.out.println(m.group(6));
		}
	}

	public void parseMembers(String content, String gid) {
		String regex = "\\{\"muin\":(.*?),\"mflag\":(.*?)\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			Group g = groups.get(gid);
			if (g != null) {
				// System.out.println(m.group(4));
				// System.out.println("mfla="+m.group(2));
				User u = g.getUsers().get(m.group(1));
				u.setMflag(m.group(2));
			} else {
				System.out.println("gid can not found.");
			}
			// System.out.println(m.group(1));
			// System.out.println(m.group(2));
		}
	}

	public void parseVipInfo(String content, String gid) {
		String regex = "\\{\"vip_level\":(.*?),\"u\":(.*?),\"is_vip\":(.*?)\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			Group g = groups.get(gid);
			if (g != null) {
				User u = g.getUsers().get(m.group(2));
				u.setVipLevel(m.group(1));
				u.setIsVip(m.group(3));
			} else {
				System.out.println("gid can not found.");
			}

		}
	}

	public void parseCard(String content, String gid) {
		String regex = "\\{\"muin\":(.*?),\"card\":\"(.*?)\"\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			Group g = groups.get(gid);
			if (g != null) {
				User u = g.getUsers().get(m.group(1));
				u.setCard(m.group(2));
			} else {
				System.out.println("gid can not found.");
			}
		}
	}

	public void parseStats(String content, String gid) {
		String regex = "\\{\"client_type\":(.*?),\"uin\":(.*?),\"stat\":(.*?)\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			Group g = groups.get(gid);
			if (g != null) {

				User u = g.getUsers().get(m.group(2));
				u.setClientType(m.group(1));
				u.setStatus(m.group(3));
				// System.out.println("client_type:" + m.group(1) + "uin:" +
				// m.group(2) + "stat:" + m.group(3)
				// + u.getNick());
			} else {
				System.out.println("gid can not found.");
			}
		}
	}

	public String[] parseReturnResult(String content) {
		System.out.println("parsing result and return");
		// System.out.println(content);
		String regex = "\\{\"retcode\":(.*?),\"result\":(.*)\\}";
		String[] returnString = new String[2];
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		if (m.find()) {

			returnString[0] = new String(m.group(1));
			returnString[1] = new String(m.group(2));
		}
		// System.out.println(returnString[0]);
		// System.out.println(returnString[1]);
		return returnString;
	}

	public String[] parseMessage(String content) {
		System.out.println("parseMessage");
		// System.out.println(content);
		String regex = "\\[\\{\"poll_type\":\"(.*?)\",\"value\":(.*)\\}\\]";
		String[] returnString = new String[2];
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		if (m.find()) {

			returnString[0] = new String(m.group(1));
			returnString[1] = new String(m.group(2));
		}
		// System.out.println(returnString[0]);
		// System.out.println(returnString[1]);
		return returnString;
	}

	public Vector<String[]> parseMessages(String content) {
		System.out.println("parseMessages");
		System.out.println(content);
		String returnString[][] = null;
		String regex = "\\{\"poll_type\":\"(.*?)\",\"value\":(.*?)\\},";
		return parseXXXX(content, regex);

	}

	public String[] parseBuddyMessage(String content) {
		System.out.println("parseBuddyMessage");
		String regex = "\\{\"msg_id\":(.*?),\"from_uin\":(.*?),\"to_uin\":(.*?),\"msg_id2\":(.*?),\"msg_type\":(.*?),\"reply_ip\":(.*?),\"time\":(.*?),\"content\":(.*)\\}";
		return parseXXX(content, regex);
	}

	// 0 font 1 content
	public String[] parseContent(String content) {
		System.out.println("parseContent");
		System.out.println(content);
		String regex = "\\[\\[(.*?\\})\\],(.*)\\]";
		return parseXXX(content, regex);
	}

	public String[] parseXXX(String content, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		String[] returnString = new String[m.groupCount()];
		if (m.find()) {
			for (int i = 0; i < m.groupCount(); i++) {
				returnString[i] = new String(m.group(i + 1));
			}
		}
		return returnString;
	}

	public Vector<String[]> parseXXXX(String content, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		Vector<String[]> total = new Vector<String[]>();
		int j = 0;
		while (m.find()) {
			String s[] = new String[m.groupCount()];
			for (int i = 0; i < m.groupCount(); i++) {
				s[i] = new String(m.group(i + 1));

			}
			total.add(s);
		}
		return total;
	}

	public String[] parseSystemMessage(String content) {
		String regex = "\\{\"seq\":(.*?),\"type\":\"(.*?)\",\"uiuin\":\"(.*?)\",(.*)\\}";
		return parseXXX(content, regex);
	}

	public String[] parseVerifyRequired(String content) {
		String regex = "\"from_uin\":(.*?),\"account\":(.*?),\"msg\":\"(.*?)\",\"allow\":(.*?),\"stat\":(.*?),\"client_type\":(.*?)";
		return parseXXX(content, regex);
	}

	public void parseGroupNameListMask(String content) {
		String regex = "\"flag\":(.*?),\"name\":\"(.*?)\",\"gid\":(.*?),\"code\":(.*?)\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			Group group = new Group(m.group(1), m.group(2), m.group(3), m.group(4));
			groups.put(m.group(3), group);
			// System.out.println(m.group(0) + " " + m.group(1));
			// System.out.println("groupCount"+m.groupCount());
		}
	}
	
	
}
