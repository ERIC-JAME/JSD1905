package tedu.mychat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	/*
	 * 运行在服务端的ServerSocket有两个作用： 1、向操作系统申请服务器端口，客户端就是通过 该端口连接服务器的
	 * 2、监听服务端口，一旦一个客户连接，就负责在服务端 这边创建一个Socket与该 客户端进行交互
	 */
	private ServerSocket server;
	/*
	 * 存放所有客户端的输出流，用于广播消息
	 */
	private ExecutorService threadPool;
	private Map<String, LogData> logData=new HashMap<String,LogData>();
	private Map<String, PrintWriter> allout=new HashMap<String,PrintWriter>();
	private Map<String, BufferedReader> allIn=new HashMap<String,BufferedReader>();
	
	public Server() throws Exception {
		try {
			/*
			 * 实例化的同时指定客户端口 客户端就是通过该端口与服务端建立连接的
			 */
			server = new ServerSocket(8098);
			allIn = new HashMap<String, BufferedReader>();
			allout = new HashMap<String, PrintWriter>();
			threadPool = Executors.newFixedThreadPool(20);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 将给定输出流从共享集合中加入
	 */
	private synchronized void addOut(String str, PrintWriter out,BufferedReader br,LogData log) {
		allout.put(str, out);
		allIn.put(str, br);
		logData.put(str, log);
	}
	/**
	 * 将给定输出流从共享集合中删除
	 */
	private synchronized void removeOut(String str) {
		allout.remove(str);
		allIn.remove(str);
		logData.remove(str);
	}
	/**
	 * 将给定消息通过遍历共享集合中的所有输出流 发送给所有用户
	 */
	public synchronized void sendMessage(String message) {
		Collection<PrintWriter> list = allout.values();
		for (Object o : list) {
			PrintWriter pw = (PrintWriter) o;
			pw.println(message);
		}
	}
	
	/**
	 * 检查是否有对应账户, 有账户返回账户对象
	 */
	public LogData check(String str) {
		try {
			FileInputStream fis = new FileInputStream("logIn.txt");
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String infos = null;
			String[] info = str.split(","); // 测试信息
			String[] info1 = null; // 日志信息
			while ((infos = br.readLine()) != null) {
				info1 = infos.split(",");
				if (info1[0].equals(info[0]) && info1[1].equals(info[1])) {
					return new LogData(info1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 启动服务器
	 */
	public void start() {
		try {
			/*
			 * ServerSocket提供方法： Socket accept() 该方法是一个堵塞方法，用于监听服务端
			 * 口，直到一个客户端的连接，当一个客户端连接了 该方法会返回一个Socket 通过这个Socket与客户端进行交互
			 */
			System.out.println("等待客户端连接..");
			while (true) {
				Socket socket = server.accept();
				System.out.println("一个客户端连上");
				threadPool.execute(new ClientHandler(socket));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("服务器启动失败");
		}
	}

	/**
	 * 私聊方法
	 */
	public void PrivateChat(String account1, String account2) {
		PrintWriter pw1 = allout.get(account1);
		BufferedReader br1 = allIn.get(account1);
		PrintWriter pw2 = allout.get(account2);
		BufferedReader br2 = allIn.get(account2);

		try {
			String message1 = null;
			String message2 = null;
			while (true) {
				if ((message1 = br1.readLine()) != null) {
					pw2.println(message1);
				}
				if ((message2 = br2.readLine()) != null) {
					pw1.println(message2);
				}
			}

		} catch (Exception e) {
			System.out.println("私聊异常");
		}
	}

	/**
	 * 聊天任务
	 *  
	 */
	class ClientHandler implements Runnable {
		private Socket socket;
		private String name;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			PrintWriter pw = null;
			LogData log = null;	
			String str = null;
			String message = null;
			String check = null;
			try {

				InputStream in = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(in, "UTF-8");
				BufferedReader br = new BufferedReader(isr);

				OutputStream out = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
				pw = new PrintWriter(osw, true);

				FileOutputStream fis = new FileOutputStream("logIn.txt", true);
				OutputStreamWriter osw1 = new OutputStreamWriter(fis);
				PrintWriter pw1 = new PrintWriter(osw1, true);

				while ((check = br.readLine()) != null) {
					if (check.equals("signIn")) {
						pw1.println(br.readLine());
					}
					if ((log = check(check)) == null)
						pw.println("ERROR");
					else {
						pw.println("OK");
						pw.println(log);
						System.out.println(log);
						break;
					}
				}
				name = log.getNickName();
				addOut(log.getAccount(), pw, br,log);
				Collection c=logData.values();
				for(Object o:c){
					LogData l=(LogData)o;
					if(!(l.equals(log))){
					pw.println(l);
					}
				}
				pw.println("OVER");
				
				sendMessage("ADDAFRIEND");
				sendMessage(log.toString());
				
				sendMessage(name + "上线了");
				
				while ((message = br.readLine()) != null) {
					
					if(message.equals("PRIVATECHAT")){
						//allout.get(message=br.readLine()).println("PRIVATECHAT");
						System.out.println(br.readLine());
						System.out.println(br.readLine());
						//allout.get(message).println(log.getAccount());
						//allout.get(message).println(name+":"+br.readLine());
					}else{
						sendMessage(name + ":" + message);
					}
				}
			} catch (Exception e) {
				
			} finally {
				sendMessage(name + "下线了");
				pw.println("REMOVEAFRIEND");
				sendMessage(log.toString());
				removeOut(log.getAccount());
				logData.remove(log.getAccount());
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}