package me.wxide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReadyActivity extends AbsActivity {
	
	private TextView tv1,tv2,tv3,tv4,tv5;
	private Button bt1,bt2;
	private Handler handle;	
	protected void initServer(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
			try {
				if(server==null||server.isClosed())	server=new ServerSocket(PORT);
	
				while(true){
					client=server.accept();
					String msg=getMsg();
					if(msg.equals("JOIN")){	//������Ϸ
						String ss="RED";
						if(redSide)	{
							ss="BLK";
						}
						sendMsg(ss);
						
						targetIp=client.getInetAddress().getHostAddress();
						
						Message message=Message.obtain();
						message.obj=MES_SETOTHER;
						handle.sendMessage(message);
						break;	//����	ѭ��				
					}//JOIN
					if(msg.equals("ASK")){
						sendMsg("EJCC");
					}
					
				}
				
						
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			}
			
		}).start();
	
		
	}//initServer
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ready);
		handle=new Handler(){
			public void handleMessage(Message msg){
				String message=(String)msg.obj;//obj��һ����String�࣬�����Ǳ���࣬���û������Ӧ��
				 Log.v("R_MESSAGE", message);
				if(message.equals(MES_SETOTHER)){
					 setOtherSide();
				 }
				
				//����message�е���Ϣ�����̵߳�UI���иĶ�
				  //����                                                      }
				}
		};
		
		
		
		  initListen();
		  //choiceRed(true);	//��ǰΪ��
	    //  choiceRed(true);//�赱ǰΪ�췽
		initServer();
		  
	}

	
	
	

	private void initListen(){
		
		
		tv2=(TextView) findViewById(R.id.textView2);
		tv4=(TextView) findViewById(R.id.textView4);
		tv5=(TextView) findViewById(R.id.textView5);
		bt2=(Button) findViewById(R.id.button2);
		bt1=(Button) findViewById(R.id.button1);
		
		bt1.setOnClickListener(new View.OnClickListener() {
			//��ʼ��Ϸ
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				tv5.setText("�ȴ�������Ҽ���!");
				startGame();
				
			}
		});
		
		bt2.setOnClickListener(new View.OnClickListener() {
			//����
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				choiceRed(!redSide);
			}
		});
		
		choiceRed(redSide);
	}
	
	
	
	
    private void choiceRed(boolean flag){
    	if(flag)	{
    		tv2.setText(this.getIp());
    		tv4.setText("�ȴ���...");
    	}	else{
    		tv4.setText(this.getIp());
    		tv2.setText("�ȴ���...");
    	}
    	
    	redSide=flag;
    }
    

	protected void setOtherSide(){
    	
    	if(redSide){
    		tv4.setText(targetIp);
    	}else{
    		tv2.setText(targetIp);
    	}
    	bt1.setText("��ʼ");
    }
    
  
}
