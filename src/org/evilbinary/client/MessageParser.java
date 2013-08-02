/*
 * Create By EvilBinary СE
 * 2011-10-20
 * rootntsd@gmail.com
 */
package org.evilbinary.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageParser {
	public MessageParser() {

	}

	public Map<String, User> parseOnlineBuddies(String content, Map<String, User> users) throws JSONException {
		JSONArray array = new JSONArray(content);
		for (int i = 0; !array.isNull(i); i++) {
			JSONObject obj = array.getJSONObject(i);
			User u = users.get(obj.get("uin").toString());
			if (u == null) {
				u = new User();
			}
			u.status = obj.get("status").toString();
			u.clientType = obj.get("client_type").toString();
			u.uin = obj.get("uin").toString();
			users.put(u.uin, u);
		}
		return users;
	}

	public UserFriendMessage parseUserFriends(String content) throws JSONException {
		JSONObject obj = new JSONObject(content);
		UserFriendMessage userFriendMessage = new UserFriendMessage();
		userFriendMessage.friends = obj.get("friends").toString();
		userFriendMessage.markNames = obj.get("marknames").toString();
		userFriendMessage.categories = obj.get("categories").toString();
		userFriendMessage.info = obj.get("info").toString();
		userFriendMessage.vipinfo = obj.get("vipinfo").toString();
		return userFriendMessage;
	}

	public Map<String, User> parseFriends(String content, Map<String, User> users) throws JSONException {
		JSONArray array = new JSONArray(content);
		for (int i = 0; !array.isNull(i); i++) {
			JSONObject obj = array.getJSONObject(i);
			User u = users.get(obj.get("uin").toString());
			if (u == null) {
				u = new User();
			}
			u.flag = obj.get("flag").toString();
			u.categories = obj.get("categories").toString();
			u.uin = obj.get("uin").toString();
			users.put(u.uin, u);
		}
		return users;
	}

	public Map<String, User> parseFriendsInfo(String content, Map<String, User> users) throws JSONException {
		JSONArray array = new JSONArray(content);
		for (int i = 0; !array.isNull(i); i++) {
			JSONObject obj = array.getJSONObject(i);
			User u = users.get(obj.get("uin").toString());
			if (u == null) {
				u = new User();
			}
			u.face = obj.get("face").toString();
			u.flag = obj.get("flag").toString();
			u.uin = obj.get("uin").toString();
			u.nick = obj.get("nick").toString();
			users.put(u.uin, u);
		}
		return users;
	}

	public Map<String, User> parseFriendsMarkName(String content, Map<String, User> users) throws JSONException {
		JSONArray array = new JSONArray(content);
		for (int i = 0; !array.isNull(i); i++) {
			JSONObject obj = array.getJSONObject(i);
			User u = users.get(obj.get("uin").toString());
			if (u == null) {
				u = new User();
			}
			u.markname = obj.get("markname").toString();
			u.uin = obj.get("uin").toString();
			users.put(u.uin, u);
		}
		return users;
	}

	public Map<String, User> parseFriendsVipInfo(String content, Map<String, User> users) throws JSONException {
		JSONArray array = new JSONArray(content);
		for (int i = 0; !array.isNull(i); i++) {
			JSONObject obj = array.getJSONObject(i);
			User u = users.get(obj.get("u").toString());
			if (u == null) {
				u = new User();
			}
			u.vipLevel = obj.get("vip_level").toString();
			u.isVip = obj.get("is_vip").toString();
			u.uin = obj.get("u").toString();
			users.put(u.uin, u);
		}
		return users;
	}

	public List<Category> parseFriendCategories(String content, List<Category> categories) throws JSONException {
		JSONArray array = new JSONArray(content);
		for (int i = 0; !array.isNull(i); i++) {
			JSONObject obj = array.getJSONObject(i);
			Category cc = new Category();
			cc.index = obj.get("index").toString();
			cc.sort = obj.get("sort").toString();
			cc.name = obj.get("name").toString();
			categories.add(cc);
		}
		return categories;
	}

	public List<Message> parseMessagesResult(String content) {
		List<Message> messages = new ArrayList<Message>();
		BufferedReader reader = new BufferedReader(new StringReader(content));
		try {
			String s;
			while ((s = reader.readLine()) != null) {

				Message msg = parseMessageResultArray(s);
				if (msg == null) {
					msg = parseMessageResultObject(s);
					if (msg == null) {
						msg = new Message();
						msg.setResult(s);
					}
				}
				messages.add(msg);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return messages;
	}

	public Message parseMessageResultArray(String content) {
		// System.out.println("parseMessage");
		// System.out.println("#" + content);
		Message message = null;
		try {
			JSONObject obj = new JSONObject(content);
			message = new Message();
			message.setRetcode(obj.get("retcode").toString());
			message.setResult(obj.getJSONArray("result").toString());
			// message.print();
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return message;

	}

	public Message parseMessageResultObject(String content) {
		// System.out.println("parseMessage");
		// System.out.println("#" + content);

		Message message = null;
		try {
			JSONObject obj = new JSONObject(content);
			message = new Message();
			message.setRetcode(obj.get("retcode").toString());
			message.setResult(obj.getJSONObject("result").toString());
//			message.print();
		} catch (JSONException ex) {
			// message.setRetcode(obj.get("c").toString());
			// message.setResult(obj.getJSONArray("e").toString());
			ex.printStackTrace();
		}
		return message;

	}

	public SystemMessage parseSystemMessage(String content) throws JSONException {
		SystemMessage m = new SystemMessage();
		JSONObject obj = new JSONObject(content);
		m.setSeq(obj.get("seq").toString());
		m.setType(obj.get("type").toString());
		m.setUiUin(obj.get("uiuin").toString());
		m.setFromUin(obj.get("from_uin").toString());
		m.setAccount(obj.get("account").toString());
		m.setMessage(obj.get("msg").toString());
		m.setAllow(obj.get("allow").toString());
		m.setStat(obj.get("stat").toString());
		// m.setClientType(obj.get("client_type").toString());

		return m;
	}

	public SessionMessage parseSessionMessage(String content) throws JSONException {
		SessionMessage sm = new SessionMessage();
		JSONObject obj = new JSONObject(content);
		sm.setMessageId(obj.get("id").toString());
		JSONArray array = obj.getJSONArray("content");
		sm.setFont(array.get(0).toString());
		String contents = "";
		for (int i = 1; !array.isNull(i); i++) {
			contents += array.get(i);
		}
		sm.setContent(contents);
		sm.setTime(obj.get("time").toString());
		sm.setMessageId2(obj.get("msg_id2").toString());
		sm.setFlags(obj.getJSONObject("flags").toString());
		sm.setRuin(obj.get("ruin").toString());
		sm.setMessageType(obj.get("msg_type").toString());
		sm.setServiceType(obj.get("service_type").toString());

		sm.setMessageId(obj.get("msg_id").toString());
		sm.setToUin(obj.get("to_uin").toString());
		sm.setReplyIp(obj.get("reply_ip").toString());
		sm.setFromUin(obj.get("from_uin").toString());
		return sm;
	}

	public GroupMessage parseGroupMessage(String content) throws JSONException {
		GroupMessage gmsg = new GroupMessage();
		JSONObject obj = new JSONObject(content);
		JSONArray array = obj.getJSONArray("content");
		gmsg.setFont(array.get(0).toString());
		String contents = "";
		for (int i = 1; !array.isNull(i); i++) {
			contents += array.get(i);
		}
		gmsg.setContent(contents);
		gmsg.setTime(obj.get("time").toString());
		gmsg.setMessageId2(obj.get("msg_id2").toString());
		gmsg.setGroupCode(obj.get("group_code").toString());
		gmsg.setSendUin(obj.get("send_uin").toString());
		gmsg.setMessageType(obj.get("msg_type").toString());
		gmsg.setMessageId(obj.get("msg_id").toString());
		gmsg.setToUin(obj.get("to_uin").toString());
		gmsg.setSeq(obj.get("seq").toString());
		gmsg.setInfoSeq(obj.get("info_seq").toString());
		gmsg.setReplyIp(obj.get("reply_ip").toString());
		gmsg.setFromUin(obj.get("from_uin").toString());
		return gmsg;
	}

	public BuddyMessage parseBuddyMessage(String content) throws JSONException {
		BuddyMessage bm = new BuddyMessage();
		JSONObject obj = new JSONObject(content);
		JSONArray array = obj.getJSONArray("content");
		bm.setFont(array.get(0).toString());
		String contents = "";
		for (int i = 1; !array.isNull(i); i++) {
			contents += array.get(i);
		}
		bm.setContent(contents);
		bm.setTime(obj.get("time").toString());
		bm.setMessageId2(obj.get("msg_id2").toString());
		bm.setMessageType(obj.get("msg_type").toString());
		bm.setToUin(obj.get("to_uin").toString());
		bm.setMessageId(obj.get("msg_id").toString());
		bm.setReplyIp(obj.get("reply_ip").toString());
		bm.setFromUin(obj.get("from_uin").toString());
		return bm;
	}

	public Vector<String[]> parseMessagePollType(String content) throws JSONException {
		//System.out.println("parseMessagePollType");
		//System.out.println("#" + content);
		JSONArray array = new JSONArray(content);
		Vector<String[]> result = new Vector<String[]>();
		for (int i = 0; !array.isNull(i); i++) {
			JSONObject obj = array.getJSONObject(i);
			String[] typeandvalue = new String[2];
			typeandvalue[0] = obj.get("poll_type").toString();
			typeandvalue[1] = obj.getJSONObject("value").toString();
			result.add(typeandvalue);
		}
		// for(int i=0;i<result.size();i++){
		// System.out.println(result.get(i));
		// }
		return result;

	}
}
