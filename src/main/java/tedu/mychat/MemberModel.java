package tedu.mychat;

import javax.swing.ImageIcon;  

import javax.swing.JPanel;  
import javax.swing.JButton;  
import java.awt.Color;  
import java.awt.Rectangle;  
import javax.swing.JLabel;  
import java.awt.Dimension;  
import java.awt.Font;  
public class MemberModel {
	 private static final long serialVersionUID = 1L;  
	  
	 public JButton jButton = null;//显示好友头像；  
	  
	 public JPanel jPanel = new JPanel();//模板容器；  
	  
	 private JLabel lb_nickName = null;//显示昵称；  
	  private String account=null;
	 private int pic;  
	  
	 private String nickname = null;  
	 private String mood = null;  
	 private JLabel lb_mood = null;//显示心情；  
	  
	   
	 public MemberModel(int pic, String nickname,String mood,String account) {  
	  super();  
	  this.pic = pic;//头像编（有多种方法可以实现，这种最简单）  
	  this.nickname = nickname;//昵称；  
	  this.mood=mood;
	  this.account=account;
	  initialize();  
	 }  
	  
	   
	 private void initialize() {  
	  lb_mood = new JLabel();  
	  lb_mood.setBounds(new Rectangle(50, 30, 200, 20));  
	  lb_mood.setFont(new Font("Dialog", Font.PLAIN, 12)); 
	  lb_mood.setForeground(new Color(110,110,110));
	  lb_mood.setText(mood);  
	  lb_mood.addMouseListener(new java.awt.event.MouseAdapter() {  
	   public void mouseEntered(java.awt.event.MouseEvent e) {  
	    exchangeEnter();  
	    lb_mood.setToolTipText(lb_mood.getText());  
	   }  
	   public void mouseExited(java.awt.event.MouseEvent e) {  
	    exchangeExited();  
	   }  
	  
	  });  
	  lb_nickName = new JLabel();  
	  lb_nickName.setBounds(new Rectangle(52, 10, 200, 20));  
	  lb_nickName.setFont(new Font("Dialog", Font.PLAIN, 15)); 
	  lb_nickName.setForeground(new Color(0,0,0));
	  lb_nickName.setText(nickname);
	  
	  jPanel.setSize(new Dimension(259, 60));  
	  jPanel.setLayout(null);  
	  jPanel.add(getJButton(), null);  
	  jPanel.add(lb_nickName, null);  
	  jPanel.add(lb_mood, null);  
	  jPanel.addMouseListener(new java.awt.event.MouseAdapter() {    
	   public void mouseExited(java.awt.event.MouseEvent e) {  
	    exchangeExited();//鼠标移出模板区，改变背景颜色；  
	   }  
	   public void mouseClicked(java.awt.event.MouseEvent e) {
		   if(!(nickname.equals("裤裆里有杀气(李毅)")||nickname.equals("巫妖国大轮明王(周梦)"))){
			new Client().frame(nickname,account);
		   }
		}
	   public void mouseEntered(java.awt.event.MouseEvent e) {  
	    exchangeEnter();//鼠标移进模板区，改变背景颜色；  
	   }  
	  });  
	 }  
	  
	 private void exchangeEnter() {  
	  jPanel.setBackground(new Color(206,230,247));  
	 }  
	  
	 private void exchangeExited() {  
	  jPanel.setBackground(null);  
	 }  
	  
	   
	 private JButton getJButton() {  
	  if (jButton == null) {  
	   jButton = new JButton();  
	   jButton.setBounds(new Rectangle(8, 10, 40, 40));  
	   jButton.setBackground(new Color(236, 255, 236));  
	   jButton.setIcon(new ImageIcon("img/"+pic + ".png"));  
	   jButton.addMouseListener(new java.awt.event.MouseAdapter() {    
	    public void mouseExited(java.awt.event.MouseEvent e) {     
	     exchangeExited();//鼠标移出模板区，改变背景颜色；  
	    }    
	    public void mouseEntered(java.awt.event.MouseEvent e) {     
	     exchangeEnter();//鼠标移进模板区，改变背景颜色；  
	    }  
	   });  
	    
	  }  
	  return jButton;  
	 }  
	}  