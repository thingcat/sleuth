package com.sleuth.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import org.apache.commons.io.IOUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class MsgUtil {
	
	/**
	 * 解压客户端发来的程序
	 * Unzip the program from the client-ends
	 * @param depressData
	 * @return
	 * @throws Exception
	 */
	public static String decompress(BinaryWebSocketFrame frameBinary) {
		ByteBufInputStream bufis = null;
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		GZIPInputStream gis = null;
		try {
			
			byte[] bytes = new byte[frameBinary.content().readableBytes()];
    		frameBinary.content().readBytes(bytes);
    		ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
    		byte[] depressData = new byte[byteBuf.readableBytes()];
			bufis = new ByteBufInputStream(byteBuf);
			bufis.read(depressData);
			
			bis = new ByteArrayInputStream(depressData);
			bos = new ByteArrayOutputStream();
			gis = new GZIPInputStream(bis);
			int count;
			byte data[] = new byte[1024];
			while ((count = gis.read(data, 0, 1024)) != -1) {
				bos.write(data, 0, count);
			}
			depressData = bos.toByteArray();
			bos.flush();
			return new String(depressData, "UTF-8");
		} catch (Exception e) {
			return "";
		} finally {
			IOUtils.closeQuietly(bufis);
			IOUtils.closeQuietly(gis);
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(bis);
		}
	}
	
	public static String decode(Object msg) {
		BinaryWebSocketFrame frameBinary = (BinaryWebSocketFrame) msg;
		byte[] bytes = new byte[frameBinary.content().readableBytes()];
		frameBinary.content().readBytes(bytes);
		ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
		String str = uncompress(byteBuf);
		return str;
	}

	private static String uncompress(ByteBuf buf) {
		try {
			byte[] temp = new byte[buf.readableBytes()];
			ByteBufInputStream bis = new ByteBufInputStream(buf);
			bis.read(temp);
			bis.close();
			Inflater decompresses = new Inflater(true);
			decompresses.setInput(temp, 0, temp.length);
			StringBuilder sb = new StringBuilder();
			byte[] result = new byte[1024];
			while (!decompresses.finished()) {
				int resultLength = decompresses.inflate(result);
				sb.append(new String(result, 0, resultLength, StandardCharsets.UTF_8));
			}
			decompresses.end();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
