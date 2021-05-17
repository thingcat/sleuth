package com.sleuth.core.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 序列化工具类
 * 
 * @author Jonse
 * @date 2018年11月1日
 */
public class SerializeUtils {

	/**
	 * 反序列化
	 *
	 * @param bytes 对象对应的字节数组
	 * @return
	 */
	public static Object deserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		Input input = new Input(bytes);
		Object obj = new Kryo().readClassAndObject(input);
		input.close();
		return obj;
	}

	/**
	 * 序列化
	 *
	 * @param object 需要序列化的对象
	 * @return
	 */
	public static byte[] serialize(Object object) {
		if (object == null) {
			return null;
		}
		Output output = new Output(4096, -1);
		new Kryo().writeClassAndObject(output, object);
		byte[] bytes = output.toBytes();
		output.close();
		return bytes;
	}

}
