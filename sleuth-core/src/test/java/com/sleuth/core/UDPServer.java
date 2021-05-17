package com.sleuth.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

	public static void main(String args[]) {
		new UDPServer().lanchApp();
	}

	private void lanchApp() {
		SendThread th = new SendThread();
		th.start();
	}

	private class SendThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					BroadcastIP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void BroadcastIP() throws Exception {
			DatagramSocket dgSocket = new DatagramSocket();
			byte b[] = "大爷我在这里".getBytes();
			DatagramPacket dgPacket = new DatagramPacket(b, b.length, InetAddress.getByName("255.255.255.255"), 10000);
			dgSocket.send(dgPacket);
			dgSocket.close();
			System.out.println("send message is ok.");
		}
	}

}
