package me.wxide;

import java.util.ArrayList;

import android.util.Log;

/**
 * @author f
 *
 */
public class QiPan {
	
	public byte [][] data;

	public QiPan(){
		this.data=new byte[9][10];
		init();
		
	}
	
	public String data2String(){
		StringBuilder sb=new StringBuilder(100);
		
		
		return sb.toString();
	}
	
	public void init(){
		data[0][0]=1;		//0
		data[1][0]=2;
		data[2][0]=3;
		data[3][0]=4;
		data[4][0]=5;	//��
		data[5][0]=4;
		data[6][0]=3;
		data[7][0]=2;
		data[8][0]=1;
		
		data[1][2]=6;		
		data[7][2]=6;
		
		data[0][3]=7;
		data[2][3]=7;
		data[4][3]=7;
		data[6][3]=7;
		data[8][3]=7;
		
		data[0][6]=-7;		//			6      13
		data[2][6]=-7;		//           13
		data[4][6]=-7;			
		data[6][6]=-7;
		data[8][6]=-7;
		
		data[1][7]=-6;
		data[7][7]=-6;
		
		data[0][9]=-1;
		data[1][9]=-2;
		data[2][9]=-3;
		data[3][9]=-4;
		data[4][9]=-5;	//��
		data[5][9]=-4;
		data[6][9]=-3;
		data[7][9]=-2;
		data[8][9]=-1;
	}


	//���᷽���y��y2�Ƿ���ͨ
	public boolean XisLink(int x,int y,int y2,int time){
		int t=0;
	
		if(Math.abs(y-y2)==1)	return true;
		int s,e;	//@start @end,@end must great than @start
		if(y2>y){s=y;e=y2;	}
		else {	s=y2; e=y;}
		for(int i=s+1;i<e;i++){
		
			if(data[x][i]!=0)	t++;
		}

		return t==time;	
		
	}
	
	//���᷽���x��x2�Ƿ���ͨ
	public boolean YisLink(int y,int x,int x2,int time){
		int t=0;	//�м�е�����
	
		int s,e;
		if(x2>x){s=x;e=x2;	}
		else {	s=x2; e=x;}
		if(Math.abs(x-x2)==1)	return true;
		for(int i=s+1;i<e;i++){
			if(data[i][y]!=0)	t++;
		}
		return t==time;
		
	}
	
	
	
	public boolean redTure=true;//true=>Ӧ�ú췽�ߣ�false �ڷ���
	private boolean hasSource=false;	//�ƶ������Ƿ���ԴĿ��
	private int lx,ly;
	
	public boolean selectPlace(int x,int y){
		
	boolean result;
	if(hasSource)	result= setTarget(x,y);	//
	else result= setSource(x,y);	// Դ����
	if(!result){
		cleanSource();
	}
	return result;
}
	private void cleanSource(){
		lx=-1;ly=-1;
		hasSource=false;
		
	}
	private boolean setSource(int x,int y){
		Log.v("SELECT", String.format("\tSOURCE:%s:%d,%d",this.getType(x, y), x,y));
		if(Judge(x,y)){
			lx=x;ly=y;
			 hasSource=true;
			 return true;
		}	
		return false;
	}
	//����һ�������ƶ���Ŀ��λ�ã�������ƶ�
	private boolean setTarget(int x,int y ){
		Log.v("set target", String.format("%d,%d-->>:%s:%d,%d",lx,ly,this.getType(x, y), x,y));
		if(JudgeOther(x,y)){
			Move(lx, ly, x, y);
			return true;
		}	
		
		return false;
	}
	//�ж��� ��ȷ��Դ���ӣ���redTrue==true  ����>0������
	private boolean Judge(int x,int y){
		boolean flag=false;
		if(data[x][y]>0)	flag=true;
		else if(data[x][y]<0)	flag=false;
		else return false;	//data[x][y]==0
		return (flag==redTure);
	}
	
	private boolean JudgeOther(int x,int y){
		boolean flag;
		if(data[x][y]>0)	flag=true;
		else if(data[x][y]<0)	flag=false;
		else return true;
		return flag!=redTure;
	}
	public boolean isHasSource() {
		return hasSource;
	}


	public void setHasSource(boolean hasSource) {
		this.hasSource = hasSource;
	}
	//�ж�data[i][j]���Ƿ���ѡ�е�����
	public boolean isTheSource(int i,int j){
	if(i==lx&&j==ly)	return true;
	return false;
	}
	
	
	
	
	//�� ���߷��ж�
	public boolean Rule1(int x1,int y1,int x2,int y2,int time){
		if(x1==x2&&y1==y2)	return false;
		if(x1!=x2&&y1!=y2)	return false;
		if(x1==x2)	{
				return XisLink(x1,y1,y2,time);
		}else{
			return YisLink(y1,x1,x2,time);
		}
		
	}
	//�ж����ܷ��ƶ���Ŀ��
	private boolean Rule2(int x,int y,int x1,int y1){
		int lx=Math.abs(x-x1);
		int ly=Math.abs(y-y1);
		
		if(lx==0||ly==0||(lx+ly)!=3)	return false;
		if(lx==2&&(data[(x+x1)/2][y]==0))	return true;
		if(ly==2&&(data[x][(y+y1)/2]==0))	return true;
		return false;
	}
	//�ж����ܷ��ƶ���Ŀ��
	private boolean Rule3(int x,int y,int x1,int y1){
		int lx=Math.abs(x-x1);
		int ly=Math.abs(y-y1);
		if(lx!=2||ly!=2)	return false;//�Ƿ�������
		if(data[(x+x1)/2][(y+y1)/2]!=0)	return false;//���Ƿ�����
		return true;
	}
	//�ж����ܷ��ƶ���Ŀ��
	private boolean Rule4(int x,int y,int x1,int y1){
		if(x1<3||x1>6)	return false;
		if(y>2&&y<7)	return false;
		int lx=Math.abs(x-x1);
		int ly=Math.abs(y-y1);
		if(lx!=1||ly!=1)	return false;//�Ƿ�������

		return true;
	}
	//�ж�˧�ܷ��ƶ���Ŀ��
	private boolean Rule5(int x,int y,int x1,int y1){
		int lx=Math.abs(x-x1);
		int ly=Math.abs(y-y1);
		if(Math.abs(lx+ly)!=1)	return false;
		
		return true;
	}
	//�ж����ܷ��ƶ���Ŀ��
	private boolean Rule6(int x,int y,int x1,int y1){
		if(data[x1][y1]==0){	//�ƶ�
			return Rule1(x,y,x1,y1,0);
		}else{					//����,
			return Rule1(x,y,x1,y1,1);
		}
	}
	//�жϱ��ܷ��ƶ���Ŀ��
	private boolean Rule7(int x,int y,int x1,int y1){
		if(Math.abs(x-x1+y-y1)!=1)	return false;//�ƶ��ľ��벻��1
		if(data[x][y]>0){	//�������һ��
			if(y<5&&y1-y==1)	return true;	
			if(y>4)	return true;
			return false;
		}
		if(data[x][y]<0){
			if(y<7&&y1-y==-1)	return true;	
			if(y<5)	return true;
			return false;
		}
		return false;
	}
	public synchronized void Move(int x,int y,int x1,int y1){
		boolean flag;
		switch(Math.abs(data[x][y])){
		case 1:flag=this.Rule1(x, y, x1, y1,0);break;
		case 2:flag=this.Rule2(x, y, x1, y1);break;
		case 3:flag=this.Rule3(x, y, x1, y1);break;
		case 4:flag=this.Rule4(x, y, x1, y1);break;
		case 5:flag=this.Rule5(x, y, x1, y1);break;
		case 6:flag=this.Rule6(x, y, x1, y1);break;
		case 7:flag=this.Rule7(x, y, x1, y1);break;
			default:flag=false;break;
		}
		
		if(flag){
			data[x1][y1]=data[x][y];
			data[x][y]=0;
			Log.v("MOVE", String.format("type:%d ture:%s s:%d,%d-->%d,%d",Math.abs(data[x][y]),redTure,x,y,x1,y1));
			this.redTureToggle();
		}
		cleanSource();
	}
	

	//toggle the redTure
	public void redTureToggle(){
			redTure=!redTure;
	}
	
	private String getType(int i,int j){
		String p;
		switch (Math.abs(data[i][j])) {
		case 1:	p="��";	break;
		case 2:	p="��";break;
		case 3: p="��";break;
		case 4: p="ʿ";break;
		case 5: p="˧";break;
		case 6: p="��";break;
		case 7: p="��";break;
		default:p="һ";
			break;
		}
		return p;
	}

	/**
	 * @param i
	 * @param j
	 * @return	judge the side of the place,null:
	 * @throws ChessException 
	 */
	private boolean getSide(int i,int j) throws ChessException{
		if(data[i][j]>0)	return true;
		if(data[i][j]<0)	return false;
		ChessException.noSide(); 
		return true;
	}
	

	//debug the map
	public void dumpMap(){
		for(int j=0;j<10;j++)
		{
			for (int i = 0; i < 9; i++) {
				String p;
				if(data[i][j]==0) p="__";
				p=getType(i,j);
				System.out.print(p+" ");
			
			}
	System.out.println();	
			
		}
		
	
		
	}
}
