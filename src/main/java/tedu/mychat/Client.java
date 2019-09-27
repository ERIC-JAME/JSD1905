package tedu.mychat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.StyledDocument;

/**
 * 聊天室客户端
 * 
 * @author Administrator
 * 
 */
public class Client implements MouseListener {

	private Socket socket;
	/*
	 * 聊天室组件
	 */
	private Map<String,JTextPane> receiveMap=new HashMap<String,JTextPane>();
	private Map<String,JButton> sendMap=new HashMap<String,JButton>();
	private Map<String,JTextField> inputMap=new HashMap<String,JTextField>();
	private JFrame frame;
	private JTextPane viewArea;
	private Box box;
	private JButton sendButton;
	private JButton clearButton;
	private JLabel onLineLb;
	private JTextField inputArea;
	private JButton pictureButton;
	private StyledDocument doc = null;

	/*
	 * 登录界面组件
	 */
	private JFrame logInFrame; // 登录框
	private Box logBox; // 登陆界面容器
	private JButton logInButton; // 登录按钮
	private JButton signButton; // 注册按钮
	private JLabel SuccessLog;// 判断登录成功
	private JLabel logLabel; // 登录标题
	private JLabel acLabel; // 登录标题
	private JLabel pwLabel; // 密码标题
	private JTextField account; // 登陆账号
	private JPasswordField password; // 登录密码

	/*
	 * 注册界面组件
	 */
	private JFrame signInFrame; // 注册框
	private Box signBox;
	private JButton okButton;
	private JLabel acLabel1; // 登录标题
	private JLabel pwLabel1; // 密码
	private JLabel ageLabel; // 年龄	
	private JLabel signLabel;
	private JLabel successSign;
	private JTextField name;
	private JTextField age;
	private JTextField mood;
	private JTextField account1; // 登陆账号
	private JPasswordField password1; // 登录密码
	
	/*
	 * 好友列表
	 */
	private JPanel fPanel =null;
	private JFrame jFrame = null;
	private JPanel jContentPane = null;
	private JScrollPane scrollPane = null;
	private JLabel bgLabel = null;
	private JLabel moodLabel; // 心情
	private JLabel nameLabel; // 姓名
	private Map<String,LogData> logMap=new HashMap<String,LogData>();
	private JLabel groupChat= null;
	private int clickF = 0;
	private int clickB = 0;
	private Map<String,JLabel> map=new HashMap<String,JLabel>();
	public int pic=0;
	private String myName;
	private String myMood;
	
	
	public Client(String str) throws Exception {
		try {
			/*
			 * 创建Socket时，需传入两个参数 分别是服务端的IP，端口 IP：通过IP可以找到服务端的计算机
			 * 端口：通过端口可以找到运行在服务端 计算机的服务端应用程序 实例化Socket的过程就是连接的过程 所以若连接失败这里会抛出异常
			 */

			System.out.println("正在连接服务端 ");
			socket = new Socket("localhost", 8098);
			System.out.println("已连接服务端");
			logIn();

		} catch (Exception e) {
			throw e;
		}
	}
	public Client(){
		
	}
	/**
	 * 客户端聊天窗口初始化
	 * 参数为聊天室名字
	 */
	public void frame(String str,String account) {

		/*
		 * 使用Windows的界面风格
		 */
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * 新建各组件
		 */
		frame = new JFrame(str);
		viewArea = new JTextPane(); // 聊天记录框
		sendButton = new JButton("发送");
		clearButton = new JButton("清屏");
		pictureButton = new JButton("图片");
		inputArea = new JTextField();
		onLineLb = new JLabel();
		JScrollPane sp = new JScrollPane(viewArea);
		doc = viewArea.getStyledDocument();
		/*
		 * 设置各组件
		 */
		viewArea.setEditable(false); // 设置聊天窗是否能编辑
		viewArea.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 20));
		// 设置聊天记录框字体
		sendButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		clearButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		pictureButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		onLineLb.setText("在线");
		inputArea.setColumns(100); // 发送条大小
		inputArea.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 20));
		/*
		 * 构局组件布置
		 */
		box = Box.createVerticalBox();
		Box box1 = Box.createHorizontalBox();
		Box box2 = Box.createHorizontalBox();
		box.add(box1);
		box.add(box2);
		box.setBorder(BorderFactory.createEmptyBorder(6, 10, 15, 8)); // 8个的边距
		box1.add(sp);
		box1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		box2.add(inputArea);
		box2.add(Box.createHorizontalStrut(8));
		box2.add(sendButton);
		box2.add(Box.createHorizontalStrut(8));
		box2.add(clearButton);
		box2.add(Box.createHorizontalStrut(8));
		box2.add(pictureButton);
		box2.add(Box.createHorizontalStrut(8));
		/*
		 * 设置框架
		 */
		frame.getContentPane().add(sp);
		frame.getContentPane().add(box, BorderLayout.SOUTH);
		frame.setSize(700, 500); // 设置窗口的大小
		frame.setAlwaysOnTop(true);// 设置窗口一直在最上面
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);// 设置窗口居中显示
		frame.setVisible(true);// 设置窗口可见
		/*
		 * 监听
		 */
		sendButton.addMouseListener((MouseListener) this);
		clearButton.addMouseListener((MouseListener) this);
		pictureButton.addMouseListener((MouseListener) this);
//		inputArea.addKeyListener(new KeyAdapter() {
//			public void keyPressed(KeyEvent e) {
//				try {
//					OutputStream out = socket.getOutputStream();
//					OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
//					PrintWriter pw = new PrintWriter(osw, true);
//					String message = "";
//					message = inputArea.getText();
//					if (e.getKeyCode() == 10) {
//						pw.println(message);
//						inputArea.setText("");
//					}
//				} catch (Exception ee) {
//					ee.printStackTrace();
//				}
//			}
//		});
		
		sendMap.put(account, sendButton);
		inputMap.put(account, inputArea);
		receiveMap.put(account,viewArea);
	}

	/**
	 * 启动登录界面
	 */
	public void logIn() {
		try {
			/*
			 * 新建和设置各个组件
			 */
			logInFrame = new JFrame("登录");

			logInButton = new JButton("登录");
			logInButton.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE,18));

			signButton = new JButton("注册");
			signButton.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE,18));

			logLabel = new JLabel();
			logLabel.setText("登录");
			logLabel.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 30));

			SuccessLog = new JLabel();
			SuccessLog.setText("请输入账号密码！");

			acLabel = new JLabel();
			acLabel.setText("账号:");
			acLabel.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 16));

			pwLabel = new JLabel();
			pwLabel.setText("密码:");
			pwLabel.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 16));

			account = new JTextField();
			account.setColumns(50);
			account.setFont(new Font(Font.DIALOG_INPUT, Font.ROMAN_BASELINE,24));

			password = new JPasswordField();
			password.setColumns(50);
			password.setFont(new Font(Font.MONOSPACED, Font.ROMAN_BASELINE, 24));

			/*
			 * 布局组件
			 */
			logBox = Box.createVerticalBox();
			Box box1 = Box.createHorizontalBox();
			Box box2 = Box.createHorizontalBox();
			Box box3 = Box.createHorizontalBox();
			Box box4 = Box.createHorizontalBox();
			Box box5 = Box.createHorizontalBox();
			logBox.add(box1);
			logBox.add(box2);
			logBox.add(box3);
			logBox.add(box4);
			logBox.add(box5);
			logBox.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1)); // 8个的边距
			box1.add(logLabel);
			box2.setBorder(BorderFactory.createEmptyBorder(9, 20, 1, 20));
			box2.add(acLabel);
			box2.setBorder(BorderFactory.createEmptyBorder(5, 20, 20, 20));
			box2.add(account);
			box3.add(pwLabel);
			box3.add(password);
			box3.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			box4.add(SuccessLog);
			box5.add(logInButton);
			box5.add(Box.createHorizontalStrut(15));
			box5.add(signButton);
			box5.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

			/*
			 * 设置框架
			 *  */
			logInFrame.getContentPane().add(logBox, BorderLayout.SOUTH);
			logInFrame.setSize(500, 350); // 设置窗口的大小
			logInFrame.setAlwaysOnTop(true);// 设置窗口一直在最上面
			logInFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			logInFrame.setLocationRelativeTo(null);// 设置窗口居中显示
			logInFrame.setVisible(true);// 设置窗口可见

			/*
			 * 鼠标监听
			 */
			logInButton.addMouseListener((MouseListener) this);
			signButton.addMouseListener((MouseListener) this);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("注册表登录失败 ！");
		}
	}

	/**
	 * 注册界面
	 * 
	 * @param args
	 */
	public void signIn() {
		try {
			/*
			 * 新建注册界面组件
			 */
			signInFrame = new JFrame("注册");
			okButton = new JButton("确认");
			signLabel = new JLabel();
			successSign = new JLabel();
			successSign.setText("请注册");
			acLabel1 = new JLabel();
			pwLabel1 = new JLabel();
			nameLabel = new JLabel();
			moodLabel = new JLabel();
			ageLabel = new JLabel();
			
			moodLabel = new JLabel();
			account1 = new JTextField();
			name = new JTextField();
			age = new JTextField();
			mood = new JTextField();
			password1 = new JPasswordField();

			/*
			 * 设置组件属性
			 */
			signLabel.setText("注册");
			signLabel.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE,30));

			acLabel1.setText("账号:");
			acLabel1.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 16));

			pwLabel1.setText("密码:");
			pwLabel1.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 16));

			nameLabel.setText("昵称:");
			nameLabel.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE,16));

			ageLabel.setText("年龄:");
			ageLabel.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 16));
			
			moodLabel.setText("签名:");
			moodLabel.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 16));

			account1.setColumns(50);
			account1.setFont(new Font(Font.DIALOG_INPUT, Font.ROMAN_BASELINE,24));

			password1.setColumns(50);
			password1.setFont(new Font(Font.MONOSPACED, Font.ROMAN_BASELINE, 24));

			okButton.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 18));

			name.setColumns(50);
			name.setFont(new Font(Font.DIALOG_INPUT, Font.ROMAN_BASELINE, 24));

			age.setColumns(50);
			age.setFont(new Font(Font.MONOSPACED, Font.ROMAN_BASELINE, 24));
			
			mood.setColumns(50);
			mood.setFont(new Font(Font.MONOSPACED, Font.ROMAN_BASELINE, 24));

			/*
			 * 布局
			 */
			signBox = Box.createVerticalBox();
			Box box1 = Box.createHorizontalBox();
			Box box2 = Box.createHorizontalBox();
			Box box3 = Box.createHorizontalBox();
			Box box4 = Box.createHorizontalBox();
			Box box5 = Box.createHorizontalBox();
			Box box6 = Box.createHorizontalBox();
			Box box7 = Box.createHorizontalBox();
			Box box8 = Box.createHorizontalBox();
			signBox.add(box1);
			signBox.add(box2);
			signBox.add(box3);
			signBox.add(box6);
			signBox.add(box7);
			signBox.add(box8);
			signBox.add(box5);
			signBox.add(box4);
			signBox.setBorder(BorderFactory.createEmptyBorder(1, 1, 10, 1)); // 8个的边距
			box1.add(signLabel);
			box1.setBorder(BorderFactory.createEmptyBorder(3, 25, 3, 25));
			box2.add(acLabel1);
			box2.add(account1);
			box2.setBorder(BorderFactory.createEmptyBorder(3, 25, 3, 25));
			box3.add(pwLabel1);
			box3.add(password1);
			box3.setBorder(BorderFactory.createEmptyBorder(3, 25, 3, 25));
			box6.setBorder(BorderFactory.createEmptyBorder(3, 25, 3, 25));
			box7.setBorder(BorderFactory.createEmptyBorder(3, 25, 3, 25));
			box8.setBorder(BorderFactory.createEmptyBorder(3, 25, 3, 25));
			box5.add(successSign);
			box6.add(nameLabel);
			box6.add(name);
			box7.add(ageLabel);
			box7.add(age);
			box8.add(moodLabel);
			box8.add(mood);
			box4.add(okButton);
			box4.setBorder(BorderFactory.createEmptyBorder(3, 25, 3, 25));

			/*
			 * 设置框架
			 */
			signInFrame.getContentPane().add(signBox, BorderLayout.SOUTH);
			signInFrame.setSize(500, 400); // 设置窗口的大小
			signInFrame.setAlwaysOnTop(true);// 设置窗口一直在最上面
			signInFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			signInFrame.setLocationRelativeTo(null);// 设置窗口居中显示
			signInFrame.setVisible(true);// 设置窗口可见

			okButton.addMouseListener((MouseListener) this);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("注册表登录失败 ！");
		}
	}

	/**
	 * 好友界面
	 * 
	 * @param args
	 */
	public void friendList(){
		fPanel = new JPanel();
		fPanel.setLayout(new BoxLayout(fPanel, BoxLayout.Y_AXIS));
		jContentPane=new JPanel();
		bgLabel = new JLabel();
		bgLabel.setIcon(new ImageIcon("img/bg2.png"));
		bgLabel.setBorder(BorderFactory.createEmptyBorder(-50, 2, 0, 2));
		
		nameLabel = new JLabel();
		nameLabel.setText(myName);
		nameLabel.setForeground(new Color(255,10,10));
		nameLabel.setFont(new Font("Dialog", Font.BOLD, 19));
		nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
		
		moodLabel = new JLabel();
		moodLabel.setText(myMood);
		moodLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		moodLabel.setForeground(new Color(110,110,110));
		moodLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
		
		groupChat = new JLabel();
		groupChat.setText("群聊");
		groupChat.setIcon(new ImageIcon("img/ico2.png"));
		groupChat.setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 2));
		groupChat.addMouseListener((MouseListener) this);
		
		jContentPane=new JPanel();
		jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.Y_AXIS));
		jContentPane.setSize(200, 408);
		jContentPane.setLocation(20, 5);
		jContentPane.add(bgLabel, null);
		jContentPane.add(nameLabel, null);
		jContentPane.add(moodLabel, null);
		jContentPane.add(groupChat, null);
		jContentPane.add(fPanel, null);
		
		friend();  //添加好友
		addJLabel();  //添加黑名单 
		jFrame = new JFrame();  
		jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
		jFrame.setSize(285, 700); 
		jFrame.setLocation(1200,180); 
		jFrame.setTitle("模拟实现QQ面板功能");  
		scrollPane=new JScrollPane(jContentPane);  
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );//不显示水平滚动条；  
		jFrame.setContentPane(scrollPane);  
		jFrame.setVisible(true);
		
	}

	public void addFriend(LogData log){
		pic=(++pic)%10;
		JLabel jLabel1 = new JLabel();
		jLabel1.setText(log.getNickName());
		jLabel1.setIcon(new ImageIcon("img/bg.png"));
		jLabel1.add(new MemberModel(pic, log.getNickName(),log.getMood(),log.getAccount()).jPanel);
		jLabel1.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		jLabel1.setVisible(false);
		
		map.put(log.getAccount(),jLabel1);
		if(fPanel!=null){
		logMap.put(log.getAccount(), log);	
		fPanel.add(jLabel1);
		jContentPane.updateUI();
		}
	}
	private void friend() {// 添加黑名单的内容
		final JLabel jLabel=new JLabel();
		jLabel.setText("我的好友");
		jLabel.setIcon(new ImageIcon("img/ico2.png"));
		jLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		jLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				clickF += 1;
				if (clickF % 2 == 1) {
					jLabel.setIcon(new ImageIcon("img/ico.png"));
					Collection c=map.values();
					for (Object o:c) {
						JLabel jb=(JLabel)o;
						jb.setVisible(true);
					}
					jContentPane.updateUI();
				} else {
					jLabel.setIcon(new ImageIcon("img/ico2.png"));
					Collection c=map.values();
					for (Object o:c) {
						JLabel jb=(JLabel)o;
						jb.setVisible(false);
					}
					jContentPane.updateUI();
				}

			}
		});
		fPanel.add(jLabel, null);
		Collection c=map.values();
		for (Object o:c) {
			JLabel jb=(JLabel)o;
			
			fPanel.add(jb, null);
		}

	}
	private void addJLabel() {// 添加黑名单的内容；
		final JLabel[] jb = new JLabel[3];
		String[] nickName={"","裤裆里有杀气(李毅)","巫妖国大轮明王(周梦)"};
		String[] mood={"","世界上最遥远的距离不是生与死，而是我站在你面前你却不知道我爱你","考研，加油！"};
		jb[0] = new JLabel();
		jb[0].setText("黑名单");
		jb[0].setIcon(new ImageIcon("img/ico2.png"));
		jb[0].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		jb[0].addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {

				clickB += 1;
				if (clickB % 2 == 1) {
					jb[0].setIcon(new ImageIcon("img/ico.png"));
					jb[1].setVisible(true);
					jb[2].setVisible(true);
					jContentPane.updateUI();
				} else {
					jb[0].setIcon(new ImageIcon("img/ico2.png"));
					jb[1].setVisible(false);
					jb[2].setVisible(false);
					jContentPane.updateUI();
				}

			}
		});
		jContentPane.add(jb[0], null);
		for (int i = 1; i < jb.length; i++) {
			jb[i] = new JLabel();
			jb[i].setIcon(new ImageIcon("img/bg.png"));
			jb[i].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			jb[i].add(new MemberModel((i + 8),nickName[i],mood[i],"blackChat"+i).jPanel);
			jb[i].setVisible(false);
			jContentPane.add(jb[i], null);
		}

	}
	public static void main(String[] args) {
		try {
			Client client = new Client("chat");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("启动异常失败");
		}
	}

	/**
	 * 该线程任务用于循环接受服务端发送过来的所有消息 并输出到客户端控制台
	 * 
	 */
	class ServerHandler implements Runnable {

		public void run() {

			try {
				InputStream in = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(in, "UTF-8");
				BufferedReader br = new BufferedReader(isr);

				String message = null;

				while ((message = br.readLine()) != null && jContentPane!=null) {
					if(message.equals("ADDAFRIEND")){
						LogData log=new LogData(message=br.readLine());
						addFriend(log);
					}else if(message.equals("REMOVEAFRIEND")){
						LogData log=new LogData(br.readLine());
						map.remove(log.getAccount());
						sendMap.remove(log.getAccount());
						receiveMap.remove(log.getAccount());
						inputMap.remove(log.getAccount());
						logMap.remove(log.getAccount());
						jContentPane.updateUI();
					}else if(message.equals("PRIVATECHAT")){
						String account = br.readLine();
						if((!receiveMap.containsKey(account))||receiveMap.get(account)==null){
							frame(logMap.get(account).getNickName(),account);
						}
						JTextPane jp=receiveMap.get(account);
						jp.setText(jp.getText()+message + "\n");
					}else{
						if(receiveMap.containsKey("GROUPCHAT")&&receiveMap.get("GROUPCHAT")!=null){
						JTextPane jp=receiveMap.get("GROUPCHAT");
						jp.setText(jp.getText()+message + "\n");
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (Exception e) {

				}
			}
			
		}
	}

	
	public void mouseClicked(MouseEvent e) {
		try {
			/*
			 * Socket提供的方法： OutputStream getOutputStream ()
			 * 获取一个字节输出流，通过该输出流写出去的任何 字节都会发送给远端计算机，这与客户端这边而言 远端计算机就是服务端
			 */
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
			PrintWriter pw = new PrintWriter(osw, true);
			
			InputStream in = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			String message = "";
			String str = "";

			// 发送信息键
			Set<String> send=sendMap.keySet();
			for(String s:send){
				JButton jb=sendMap.get(s);
				if(jb!=null&&e.getSource() == jb){
					JTextField jf=inputMap.get(s);
					message = jf.getText();
					if(!(s.equals("GROUPCHAT"))){
					pw.println("PRIVATECHAT");
					pw.println(s);
					receiveMap.get(s).setText(receiveMap.get(s).getText()+message+"\n");
					System.out.println(message);
					}
					pw.println(message);
					
					jf.setText("");
				}
			}			
				// 清屏键
			
			if (e.getSource() == clearButton) {
				viewArea.setText("");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// 图片键
			} else if (e.getSource() == pictureButton) {
				JFileChooser f = new JFileChooser(); // 查找文件
				f.showOpenDialog(null);
				insertIcon(f.getSelectedFile());

				// 登录键
			} else if (e.getSource() == logInButton) {
				for (char ch : password.getPassword()) {
					str += ch;
				}
				message = account.getText() + "," + str;
				pw.println(message);
				String s = null;

				if ((s = br.readLine()).equals("OK")){
					SuccessLog.setText("登陆成功");
					logInFrame.dispose();
					String my=br.readLine();
					String[] myInfo=my.split(",");
					myName=myInfo[2];
					myMood=myInfo[4];
					//把我的信息放入
					logMap.put("GROUPCHAT",new LogData(my));
					while(!(s=br.readLine()).equals("OVER")){
						LogData ls=new LogData(s);
						addFriend(ls);
					}
					ServerHandler hander = new ServerHandler();
					Thread t = new Thread(hander);
					t.start();
					friendList();
					
				} else {
					System.out.println(s);
					SuccessLog.setText("登录失败");
					
				}

				// 注册键
			} else if (e.getSource() == signButton) {
				signIn();

				// 注册确认
			} else if (e.getSource() == okButton) {
				if (account1.getText().length() == 0
						|| password1.getPassword().length == 0) {
					successSign.setText("请正确输入");
				} else {
					str = "";
					for (char ch : password1.getPassword()) {
						str += ch;
					}
					pw.println("signIn");
					pw.println(account1.getText() + "," + str + ","
							+ name.getText() + "," + age.getText()+","+mood.getText());
					signInFrame.dispose();
				}
			}else if(e.getSource()==groupChat){
				frame("群 聊","GROUPCHAT");
			}

		} catch (Exception ee) {
			
			ee.printStackTrace();
		}

	}

	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	private void insertIcon(File file) {
		if (file != null) {
			viewArea.setCaretPosition(doc.getLength()); // 设置插入位置
			viewArea.insertIcon(new ImageIcon(file.getPath())); // 插入图片
		}
	}
}


