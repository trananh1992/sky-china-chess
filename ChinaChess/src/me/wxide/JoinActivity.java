package me.wxide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.join);
		initListen();
		
		//initClient();
		handle=new Handler(){
			public void handleMessage(Message msg){
				System.out.println("receivce:"+msg);
				String message=(String)msg.obj;//obj��һ����String�࣬�����Ǳ���࣬���û������Ӧ��
				if(message.equals(MES_SET_CONN)){
				bt1.setText("�����ӣ��ȴ���ʼ");
				 }
				if(message.equals(MES_START_GAME)){
					Log.v("handle:", "start game ");
				startGame();
				}
				if(message.equals(MES_PDIALOG_UPDATE)){
				
					pDialog.setMessage(pDialogMessage);
				}
				if(message.equals(MES_SCAN_OVER)){
					Log.v("handle:", "scan over ");
					pDialog.dismiss();
					afterScan();
				}
				else{
					Log.v("handle:", "use other ");
					handleMes(message);
				}
				//����message�е���Ϣ�����̵߳�UI���иĶ�
				  //����                                                      }
				}
		};
		
	}
	
	  protected ProgressDialog pDialog;
	     protected static String pDialogMessage="����������ɨ��";

		protected void progessDialog(){
	    	 
			pDialog = new ProgressDialog(JoinActivity.this);  
	    	 pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//���÷��ΪԲ�ν�����  
	    	 pDialog.setTitle("��ʾ");//���ñ���  
          // mpDialog.setIcon(R.drawable.icon);//����ͼ��  
	    	 pDialog.setMessage("����������ɨ��");  
	    	 pDialog.setIndeterminate(false);//���ý������Ƿ�Ϊ����ȷ  
	    	 //pDialog.setCancelable(true);//���ý������Ƿ���԰��˻ؼ�ȡ��  
	    	 pDialog.setButton("ȡ��", new DialogInterface.OnClickListener(){  

               @Override  
               public void onClick(DialogInterface dialog, int which) {  
                   dialog.cancel();  
                     
               }  
                 
           });  
	    	 pDialog.show();  
	     }
	
	class ScanThread extends Thread{
		
		 String ip="10.1.8.";
		 Integer index=254;
		 int tcout=0;
		
		 public void reset(){
			 ip="10.1.8.";
		 index=254;
		 tcout=0;
		 }
	
		@Override
		public void run() {
			System.out.print("will start scan!");
			// TODO Auto-generated method stub
			while(true){
				if(index<2)	break;
				new Thread(new Runnable(){
				
					@Override
					public void run() {
						System.out.print("Scan thread start scan!");
						// TODO Auto-generated method stub
						if(tcout<70){
									String curIp=null;
										synchronized (index) {
											curIp=ip+index;
											index--;
										}
									try {
									tcout++;							
									pDialogMessage="����ɨ��:"+curIp;
									System.out.println(pDialogMessage);
									Message message=Message.obtain();
									message.obj=MES_PDIALOG_UPDATE;
									 handle.sendMessage(message);
									 
										Socket s = new Socket(curIp,2378);
										//s.sendUrgentData(0xff);
										BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
										BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
									bw.write("ASK");
									bw.newLine();
									bw.flush();
									String backmsg=br.readLine();
									if(backmsg.equals("EJCC"))
										serverList.add(curIp);
											
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										//e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										//e.printStackTrace();
									}
										tcout--;
								
			
						
					}//if
					
					}//run
				}).start();
			
			}//while
			
			Message message=Message.obtain();
			message.obj=MES_SCAN_OVER;
			handle.sendMessage(message);
			
				System.out.println("all over!"+serverList.size());
		}//run
		
		
	}//class
	
	private void initClient(){
		new Thread(new Runnable(){
		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if(client==null||client.isClosed()){
					System.out.println("initclient"+targetIp);
					try {
						client=new Socket(targetIp,PORT);
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
							Log.v("SUCC", "BLK1");
							redSide=false;
							
							Message message=Message.obtain();
				    	 	message.obj=MES_START_GAME;
							 handle.sendMessage(message);
						
						}else{
						
							ShowMsg("���ӳ���");
						}
					}//else send 'JOIN' success
					
				} catch (IOException e) {
					Log.v("ERROR", "����ʧ��!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					
				Log.v("initclient over!","1");
					
					
				}//run
				
						
			
			
		}).start();
		
		
	}
	
	private void afterScan(){
		if(serverList.size()==1){
			
			et01.setText(serverList.get(0));
		}else{
			
		}
		
	}
	
	
	//init set widget listen
	private void initListen(){
		bt1=(Button) this.findViewById(R.id.button1);
		bt2=(Button) this.findViewById(R.id.button2);
		et01=(EditText)this.findViewById(R.id.editText1);
		

	bt1.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if(isCorrectIp(et01.getText().toString())){	//���ip
				targetIp=et01.getText().toString().trim();
				Log.v("SET", targetIp);
				initClient();	
			
			}else{
			// TODO Auto-generated method stub
			ShowMsg("��������ȷ��ip��ַ.");
			}
		}
	});
	
	bt2.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		
			serverList.clear();
			scan=new ScanThread();
			scan.start();
			progessDialog();

			
		}
	});
	
	}
}
