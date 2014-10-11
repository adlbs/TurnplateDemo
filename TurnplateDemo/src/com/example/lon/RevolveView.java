package com.example.lon;

import com.example.lon.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;

public class RevolveView extends View implements  OnTouchListener{
	
	//Turnplate��  ת��
	private OnTurnplateListener onTurnplateListener;
	public void setOnTurnplateListener(OnTurnplateListener onTurnplateListener) {
		this.onTurnplateListener = onTurnplateListener;
	}
	
	class Point {
		int flag;//��ʶ
		Bitmap bitmap;//ͼƬ
		int angle;//�Ƕ�
		float x;//������
		float y;//������
		float x_c;//�뾶�е�x����
		float y_c;//�뾶�е�y����
	}
	/**
	 * ���ʣ��㡢��
	 */
	private Paint mPaint = new Paint();
	/**
	 * ͼ���б�
	 */
	private Bitmap[] icons = new Bitmap[10];
	/**
	 * point�б�
	 */
	private Point[] points;
	/**
	 * ��Ŀ
	 */
	private static final int PONIT_NUM = 5;
	
	/**
	 * Բ������
	 */
	private int pointX=0, pointY=0;
	/**
	 * �뾶
	 */
	private int Radius = 0;
	/**
	 * ÿ���������ĽǶ�
	 */
	private int degreeDelta;
	/**
	 * ÿ��ת���ĽǶȲ�
	 */
	private int tempDegree = 0;
	//�ж�ѡ������Ǹ���ť
	private int chooseBtn = 0;
	//���캯��
	public RevolveView(Context context, int px, int py, int radius) {
		super(context);		
		//mPaint.setColor(Color.RED);
	//	mPaint.setStrokeWidth(2);
		//���û��ʵ�һЩ����
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true); //�������  
		mPaint.setStyle(Paint.Style.STROKE); //���ƿ���Բ 
		//��������   ������{ʵ�ߣ��հף�ʵ�ߣ��հף�.....},ƫ����
		PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);  
		mPaint.setPathEffect(effects);
		//mPaint.setAlpha(50);  //����͸����
		pointX = px;
		pointY = py;
		Radius = radius;
		//��ȡȫ��ͼ��
		loadIcons();
		//��ʼ�����漰����ÿ����
		initPoints();
		//�������漰����ÿ���������
		calculateCoordinates();
		
		//��������ִ����д��onDraw����
		//ִ�м�������dispatchTouchEvent
	}
	
	/**
	 * װ��ͼ��
	 */
	public void loadBitmaps(int key,Drawable d){
		//����λͼ
		Bitmap bitmap = Bitmap.createBitmap(60,60,Bitmap.Config.ARGB_8888);
		//��������
		Canvas canvas = new Canvas(bitmap);
		//���û�����С
		d.setBounds(0, 0, 60, 60);
		//������
		d.draw(canvas);
		icons[key]=bitmap;
	}
	/**
	 * ��������loadIcons ��ȡ����ͼ��
	 */
	public void loadIcons(){
		Resources r = getResources();	//�õ���Դ�ļ�
		loadBitmaps(0, r.getDrawable(R.drawable.message));
		loadBitmaps(1, r.getDrawable(R.drawable.home));
		loadBitmaps(2, r.getDrawable(R.drawable.controls));
		loadBitmaps(3, r.getDrawable(R.drawable.map));
		loadBitmaps(4, r.getDrawable(R.drawable.settings));
	}

	
	/**
	 * ��������initPoints ��ʼ�����漰����ÿ����
	 */
	private void initPoints() {
		points = new Point[PONIT_NUM];
		Point point;
		int angle = 0;
		degreeDelta = 360/PONIT_NUM;
		
		for(int index=0; index<PONIT_NUM; index++) {
			point = new Point();
			point.angle = angle;//���ýǶ�
			angle += degreeDelta;
			point.bitmap = icons[index];//����ͼƬ
			point.flag=index;//���ñ�ʶ
			points[index] = point;
		}
	}
	
	/**
	 * ��������calculateCoordinates ����ÿ���������
	 */
	private void calculateCoordinates() {
		Point point;
		for(int index=0; index<PONIT_NUM; index++) {
			point = points[index];
			point.x = pointX+ (float)(Radius * Math.cos(point.angle*Math.PI/180));
			point.y = pointY+ (float)(Radius * Math.sin(point.angle*Math.PI/180));	
			point.x_c = pointX+(point.x-pointX)/2;
			point.y_c = pointY+(point.y-pointY)/2;
		}
	}
	/**
	 * ��������resetPointAngle ���¼���ÿ����ĽǶ�
	 */	
	private void resetPointAngle(float x, float y) {
		float distance = (float)Math.sqrt(((x-pointX)*(x-pointX) + (y-pointY)*(y-pointY)));
		if(Radius+50>=distance)
		{
		int degree = calculationDeviationAngle(x, y);
		for(int index=0; index<PONIT_NUM; index++) {
			//degree+=DegreeDelta;
			points[index].angle += degree;		
			if(points[index].angle>360){
				points[index].angle -=360;
			}else if(points[index].angle<0){
				points[index].angle +=360;
			}	
		}
		}
	}
	
	/**
	 * ��������calculationDeviationAngle ����ƫ�ƽǶ�
	 */
	private int calculationDeviationAngle(float x, float y) {
		int a=0;
		float distance = (float)Math.sqrt(((x-pointX)*(x-pointX) + (y-pointY)*(y-pointY)));
		int degree = (int)(Math.acos((x-pointX)/distance)*180/Math.PI);
		if(y < pointY) {
			degree = -degree;
		}	
		if(tempDegree!=0){
			a = degree - tempDegree;
		}
		tempDegree=degree;	
		return a;
	}
	
	/**
	 * ��������checkCurrentDistance  Ѱ���û�����Ǹ���ť�ı�ʶ
	 */
	private void checkCurrentDistance(float x, float y) {
		for(Point point:points){
			float distance = (float)Math.sqrt(((x-point.x)*(x-point.x) + (y-point.y)*(y-point.y)));			
			if(distance<40){
				chooseBtn =  point.flag;
				break;
			}
		}	
	
	}
	
	private void switchScreen(MotionEvent event){
		//Ѱ���û�����Ǹ���ť�ı�ʶ
		checkCurrentDistance(event.getX(), event.getY());
		//������д��onPointTouch������Ŀ�ľ��Ǵ���ȥ
		onTurnplateListener.onPointTouch(chooseBtn);
		
	}
	
	
	/**
	 * ��������drawInCenter �ѵ�ŵ�ͼ�����Ĵ�
	 */
	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,int flag) {
		canvas.drawPoint(left, top, mPaint);
			canvas.drawBitmap(bitmap, left-bitmap.getWidth()/2, top-bitmap.getHeight()/2, mPaint);
	}
	
	//����ͼƬ��������ͼƬ��λ��
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawCircle(pointX, pointY, Radius-70, mPaint);
	 // mPaint.setStyle(Point.Style.FILL);
		canvas.drawPoint(pointX, pointY, mPaint);
		mPaint.setStyle(Paint.Style.FILL);
		for(int index=0; index<PONIT_NUM; index++) {
			canvas.drawCircle(points[index].x_c, points[index].y_c,2, mPaint);
			drawInCenter(canvas, points[index].bitmap, points[index].x, points[index].y,points[index].flag);
		}
		mPaint.setStyle(Paint.Style.STROKE);//���ƿ���Բ
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {		
		 int action = event.getAction();
		 //�ж�����һ�ֶ���
	        switch (action) {
	        case MotionEvent.ACTION_DOWN:
	            break;
	        case MotionEvent.ACTION_MOVE:
	        	resetPointAngle(event.getX(), event.getY());
	        	calculateCoordinates();
	    		invalidate();//��ʼ��ϵͳView�Դ���һЩ���ò���
	            break;
	        case MotionEvent.ACTION_UP:
	        	switchScreen(event);
	        	tempDegree = 0;
	        	invalidate();
	            break;
	        case MotionEvent.ACTION_CANCEL:
	        	//ϵͳ�����е�һ���̶����޷�������Ӧ��ĺ�������ʱ��������¼���
	        	//һ����ڴ����н�����Ϊ�쳣��֧�������
	            break;
	        }
		return true;
	}

	 public static interface OnTurnplateListener {
	       /*
	        *������·��� onPointTouch ʶ���û�������ĸ���ť����һЩ�����Ľ���
	        */
	     public void onPointTouch(int flag);           	        
	 }

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}
	
	
}
