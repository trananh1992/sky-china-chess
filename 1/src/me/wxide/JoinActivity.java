package me.wxide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
	






public class JoinActivity extends AbsActivity {

	private Handler handle;
	private EditText et01;
	private Button bt1,bt2;
	private ScanThread scan;
	private TextView tv1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.join);
		initListen();
		
		//initClient();
		handle=new Handler(){
			public void handleMessage(Message msg){
				String message=(String)msg.obj;//obj不一定是String类，可以是别的类，看用户具体的应用
				 Log.v("SET_CONN", message);
				if(message.equals(MES_SET_CONN)){
				bt1.setText("已连接，等待开始");
				 }
				if(message.equals(MES_START_GAME)){
				startGame();
				}
				
				//根据message中的信息对主线程的UI进行改动
				  //……                                                      }
				}
		};
		
	}
	
	
	
	private void initClient(){
		new Thread(new Runnable(){
		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if(client==null||client.isClosed()){
					System.out.println("initclient"+targetIp);
					try {
						client=new Socket(targetIp,PORT);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
					}//if over
				
				try {
					boolean flag=sendMsg("JOIN");
					Log.v("SUCC", "JOIN");
					System.out.println("initclient"+flag);
					if(!flag) Log.v("MERROR","sendMsg ERROR!");
					else{
						String msg=AbsActivity.getMsg();
						System.out.println(msg);
						if(msg.equals("RED")){
							redSide=true;
							Log.v("SUCC", "RED");
							Message message=Message.obtain();
							message.obj=MES_START_GAME;
							handle.sendMessage(message);
						}
						else if(msg.equals("BLK")){
							Log.v("SUCC", "RED");
							redSide=false;
							
							Message message=Message.obtain();
							message.obj=MES_START_GAME;
							handle.sendMessage(message);
							
						}else{
							Log.v("SUCC", "ERROR");
							ShowMsg("连接出错！");
						}
					}//else voer
					
				} catch (IOException e) {
					Log.v("ERROR", "连接失败!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					
					System.out.println("will sendMsg");
			
					Message message=Message.obtain();
					message.obj=MES_SET_CONN;
					handle.sendMessage(message);
					
					//setOtherSide(targetIp);
					Log.v("target", targetIp);
					
					
				}//JOIN
				
						
			
			
		}).start();
		
		
	}
	
	//init set widget listen
	private void initListen(){
		bt1=(Button) this.findViewById(R.id.button1);
		bt2=(Button) this.findViewById(R.id.button2);
		et01=(EditText)this.findViewById(R.id.editText1);
		tv1=(TextView) this.findViewById(R.id.textView1);

	bt1.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if(isCorrectIp(et01.getText().toString())){	//检测ip
				targetIp=et01.getText().toString();
				Log.v("SET", targetIp);
				initClient();	
			}else{
			// TODO Auto-generated method stub
			ShowMsg("请输入正确的ip地址.");
			}
		}
	});
	
	bt2.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			StringBuilder sb=new StringBuilder(100);
			scan=new ScanThread();
			//progessDialog();
			scan.start();
	while(true){
				
				try {
					Thread.sleep(300);
					if(!scan.isAlive()){
						for(int i=0;i<scan.list.size();i++){
							
							sb.append(scan.list.get(i));
							sb.append("\n");
						}
						tv1.setText(sb.toString());
						
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	});
	
	}
}
