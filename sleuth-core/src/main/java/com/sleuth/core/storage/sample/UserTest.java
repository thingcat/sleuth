package com.sleuth.core.storage.sample;

import java.util.ArrayList;
import java.util.List;

import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;

public class UserTest {
	
	public static void main(String[] args) throws Exception {
		
		User user = new User();
		user.setEmail("test");
		user.setFirstName("aaa");
		user.setLastName("bbb");
		
		List<User> users = new ArrayList<User>();
		users.add(new User("ee", "bbbc"));
		users.add(new User("ff", "bbbc"));
		user.setFriends(users);
		
		Serializer<User> serializer = ProtostuffSerializer.of(user.cachedSchema());
		byte[] bytes = serializer.serialize(user);
		User v = serializer.deserialize(bytes);
		
		System.out.println(bytes);
		System.out.println(v.toString());
		System.out.println(serializer.toJson(v));
		
		
	}

}
