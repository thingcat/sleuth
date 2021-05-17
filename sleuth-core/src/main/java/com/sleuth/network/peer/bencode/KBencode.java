package com.sleuth.network.peer.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 光蓝Comet
 * @description 一句话说明这个类的作用
 * @date 2018/8/21
 */
public class KBencode implements Bencode {

    @SuppressWarnings("unchecked")
	@Override
    public byte[] bencode(Object obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (obj instanceof String){
            return encoder(obj.toString(),baos);
        }else if (obj instanceof Integer){
            return encoder(Integer.parseInt(String.valueOf(obj)),baos);
        }else if (obj instanceof List){
            return encoder((List<Object>)obj,baos);
        }else if (obj instanceof Map){
            return encoder((Map<String,Object>)obj,baos);
        }
        return null;
    }

    /**
     * String Bencode
     * @param str
     * @param baos
     * @return
     */
    private byte[] encoder(String str,ByteArrayOutputStream baos) throws IOException {
        baos.write(Integer.valueOf(str.length()).toString().getBytes());
        baos.write((byte)':');
        baos.write(str.getBytes());
        return baos.toByteArray();
    }

    /**
     * Integer Bencode
     * @param i
     * @param baos
     * @return
     */
    private byte[] encoder(Integer i,ByteArrayOutputStream baos) throws IOException {
        baos.write((byte)'i');
        baos.write(i.toString().getBytes());
        baos.write((byte)'e');
        return baos.toByteArray();
    }

    /**
     * List Bencode
     * @param list
     * @param baos
     * @return
     */
    private byte[] encoder(List<Object> list,ByteArrayOutputStream baos) throws IOException {
        baos.write((byte)'l');
        for (Object obj : list){
            if (obj instanceof Integer){
                baos.write((byte)'i');
                baos.write(obj.toString().getBytes());
                baos.write((byte)'e');
            }else if (obj instanceof String){
                baos.write(Integer.valueOf(obj.toString().length()).toString().getBytes());
                baos.write((byte)':');
                baos.write(obj.toString().getBytes());
            }
        }
        baos.write((byte)'e');
        return baos.toByteArray();
    }

    /**
     * Map Bencode
     * @param map
     * @param baos
     * @return
     */
    @SuppressWarnings("unchecked")
	private byte[] encoder(Map<String,Object> map,ByteArrayOutputStream baos) throws IOException {
        baos.write((byte)'d');
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof String){
                  baos.write(Integer.valueOf(obj.toString().length()).toString().getBytes());
                  baos.write((byte)':');
                  baos.write(obj.toString().getBytes());
              }else if (obj instanceof Integer){
                  baos.write((byte)'i');
                  baos.write(obj.toString().getBytes());
                  baos.write((byte)'e');
              }else if (obj instanceof List){
                  baos.write(encoder((List<Object>)obj,baos));
              }else if (obj instanceof Map){
                  baos.write(encoder((Map<String,Object>)obj,baos));
              }
        }
        baos.write((byte)'e');
        return baos.toByteArray();
    }
}
