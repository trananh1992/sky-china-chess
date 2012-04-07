package me.wxide;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;




public class AbsActivity extends Activity {
	protected final static int PORT=2378;
	protected Handler handle;
	protected static boolean redSide=true;//��ǰ�����ִ�����Ǻ췽���Ǻڷ�
	
	protected static GameView gv;		
	protected static ServerSocket server;		
	protected static Socket client;
	protected static String targetIp;		//�Լ�ip
	protected static String currentIp;		//�������ip
	protected static QiPan qipan=new QiPan();
	
	protected static ArrayList<String> serverList=new ArrayList<String>();
	protected final static String MES_SET_CONN="SUCCESS CONNECT TARGET";
	protected final static String MES_START_GAME="START_GAME";
	protected final static String MES_SETOTHER="SET_OTHER_SIDE";
	protected final static String MES_SURE_EXIT="SURE_EXIT";
	protected final static String MES_SCAN_OVER="SCAN_OVER";
	protected final static String MES_PDIALOG_UPDATE="PDIALOG_UPDATE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	
		
	}
	
	
	

	
	
	protected void handleMes(String msg){
	
		if(msg.equals(MES_SURE_EXIT)){
			this.dialog();
		}
		if(msg.equals(MES_SCAN_OVER)){
			
		}
		
	}
	
	
	public static String getMsg() throws IOException{
		 if(client==null||client.isClosed()){
			 throw new IOException();//�����ѱ��ر�
		  }
		 BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
		 
		 return br.readLine();
	}
	
    public boolean sendMsg(String msg) throws IOException{
    	
		  if(client==null||client.isClosed()){
			  
			 return false;//�����ѱ��ر�
		  }
		  		
		  		BufferedWriter bw;
				bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				bw.write(msg);
			  	bw.newLine();
			  	bw.flush();

		  return true;
		  
	  } 


	  protected String getIp(){
	    	WifiManager wifi=(WifiManager)getSystemService(WIFI_SERVICE);
	    	WifiInfo wifiinfo=wifi.getConnectionInfo();
	    	int ip=wifiinfo.getIpAddress();
	    	return intToIp(ip);
	    }
	    
	   protected String intToIp(int ip){
	    	StringBuilder sb=new StringBuilder(15);
	    	sb.append((ip)&0xff);
	    	sb.append(".");
	    	sb.append((ip>>8)&0xff);
	    	sb.append(".");
	    	sb.append((ip>>16)&0xff);
	    	sb.append(".");
	    	sb.append((ip>>24)&0xff);
	    	return sb.toString();
	    }

	   protected void startGame(){
		   
	    	Log.v("STARTGAME!", "init start game!");
	    	if(gv==null)	{gv=new GameView(this,this);}
	          setContentView(gv);
	         qipan.init();
	      
	    }
	   
	   	public static boolean isClientClose(){
	   			try {
					client.sendUrgentData(0xff);
					return false;
				} catch (IOException e) {
					return true;
					// TODO Auto-generated catch block
				}
	   		
	   	}
	   	public void ShowMsg(String msg){
			Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
		}
	 	public void ShowMsg(String msg,boolean flag){
			if(flag)
	 		Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}   	
	      public int getWidth(){
	    	  return getWindowManager().getDefaultDisplay().getWidth();  
	    	  
	      }

	      public int getHeight(){
	    	 return getWindowManager().getDefaultDisplay().getHeight();
	      }
	      /**
	       * 	�ж� ip�Ƿ�����ȷ��ʽ��ip
	       * @param ip
	       * @return
	       */
	     public static boolean isCorrectIp(String ip){
	    	  String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}"; 
		       Pattern pattern = Pattern.compile(regex); 
		        Matcher matcher = pattern.matcher(ip); 
		        return matcher.matches(); 
	     }
	     
	     protected void dialog() {
	    	  AlertDialog.Builder builder = new Builder(AbsActivity.this);
	    	  builder.setMessage("ȷ���˳���");
	    	  builder.setTitle("��ʾ");
	    	  
	    	  builder.setPositiveButton("ȷ��", new OnClickListener() {
	    	   @Override
	    	   public void onClick(DialogInterface dialog, int which) {
	    	   // dialog.dismiss();
	    	   // AbsActivity.this.finish();
	    		   android.os.Process.killProcess(android.os.Process.myPid());
	    		  // System.exit(0); 

	    	   }
	    	  });
	    	  
	    	  builder.setNegativeButton("ȡ��", new OnClickListener() {
	    	   @Override
	    	   public void onClick(DialogInterface dialog, int which) {
	    	    dialog.dismiss();
	    	   }
	    	  });
	    	  builder.create().show();
	     }
	   
	     protected void sendMessageToHandler(String msg){
	    	 	Message message=Message.obtain();
	    	 	message.obj=msg;
				 handle.sendMessage(message);
	     }
	 	@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			menu.add(0, 1, 1, "�˳�");
			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			dialog();
			
			return super.onOptionsItemSelected(item);
		}
	   
}
