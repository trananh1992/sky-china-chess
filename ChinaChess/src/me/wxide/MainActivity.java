package me.wxide;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AbsActivity {
	
    private Button button01,button02,button03;
    private TextView et01;
	protected int message;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(									//����Ϊȫ��ģʽ
        WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
      this.setContentView(R.layout.main);
      this.button01=(Button) this.findViewById(R.id.button1);
      this.button02=(Button) this.findViewById(R.id.button2);
      this.et01=(TextView) this.findViewById(R.id.textView1);
      
      this.currentIp=this.getIp();
      
      this.et01.setText("�ҵ�IP:"+currentIp);

     initListen();
     
    }
    /**
     * 	����2��button�ļ����¼�
     */
    public void initListen(){
    	 button02.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				startActivity(new Intent(MainActivity.this,JoinActivity.class));
    			}
    		});
    	      
    	      //������Ϸ
    	      this.button01.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				startActivity(new Intent(MainActivity.this,ReadyActivity.class));
    			}
    		});
    	
    }


   
}


