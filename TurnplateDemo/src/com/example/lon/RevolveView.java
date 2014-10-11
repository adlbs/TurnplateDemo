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
	
	//Turnplate：  转盘
	private OnTurnplateListener onTurnplateListener;
	public void setOnTurnplateListener(OnTurnplateListener onTurnplateListener) {
		this.onTurnplateListener = onTurnplateListener;
	}
	
	class Point {
		int flag;//标识
		Bitmap bitmap;//图片
		int angle;//角度
		float x;//横坐标
		float y;//纵坐标
		float x_c;//半径中点x坐标
		float y_c;//半径中点y坐标
	}
	/**
	 * 画笔：点、线
	 */
	private Paint mPaint = new Paint();
	/**
	 * 图标列表
	 */
	private Bitmap[] icons = new Bitmap[10];
	/**
	 * point列表
	 */
	private Point[] points;
	/**
	 * 数目
	 */
	private static final int PONIT_NUM = 5;
	
	/**
	 * 圆心坐标
	 */
	private int pointX=0, pointY=0;
	/**
	 * 半径
	 */
	private int Radius = 0;
	/**
	 * 每两个点间隔的角度
	 */
	private int degreeDelta;
	/**
	 * 每次转动的角度差
	 */
	private int tempDegree = 0;
	//判断选择的是那个按钮
	private int chooseBtn = 0;
	//构造函数
	public RevolveView(Context context, int px, int py, int radius) {
		super(context);		
		//mPaint.setColor(Color.RED);
	//	mPaint.setStrokeWidth(2);
		//设置画笔的一些参数
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true); //消除锯齿  
		mPaint.setStyle(Paint.Style.STROKE); //绘制空心圆 
		//绘制虚线   参数：{实线，空白，实线，空白，.....},偏移量
		PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);  
		mPaint.setPathEffect(effects);
		//mPaint.setAlpha(50);  //设置透明度
		pointX = px;
		pointY = py;
		Radius = radius;
		//读取全部图标
		loadIcons();
		//初始化所涉及到的每个点
		initPoints();
		//计算所涉及到的每个点的坐标
		calculateCoordinates();
		
		//接下来将执行重写的onDraw方法
		//执行监听手势dispatchTouchEvent
	}
	
	/**
	 * 装载图标
	 */
	public void loadBitmaps(int key,Drawable d){
		//创建位图
		Bitmap bitmap = Bitmap.createBitmap(60,60,Bitmap.Config.ARGB_8888);
		//创建画布
		Canvas canvas = new Canvas(bitmap);
		//设置画布大小
		d.setBounds(0, 0, 60, 60);
		//画出来
		d.draw(canvas);
		icons[key]=bitmap;
	}
	/**
	 * 方法名：loadIcons 获取所有图标
	 */
	public void loadIcons(){
		Resources r = getResources();	//得到资源文件
		loadBitmaps(0, r.getDrawable(R.drawable.message));
		loadBitmaps(1, r.getDrawable(R.drawable.home));
		loadBitmaps(2, r.getDrawable(R.drawable.controls));
		loadBitmaps(3, r.getDrawable(R.drawable.map));
		loadBitmaps(4, r.getDrawable(R.drawable.settings));
	}

	
	/**
	 * 方法名：initPoints 初始化所涉及到的每个点
	 */
	private void initPoints() {
		points = new Point[PONIT_NUM];
		Point point;
		int angle = 0;
		degreeDelta = 360/PONIT_NUM;
		
		for(int index=0; index<PONIT_NUM; index++) {
			point = new Point();
			point.angle = angle;//设置角度
			angle += degreeDelta;
			point.bitmap = icons[index];//设置图片
			point.flag=index;//设置标识
			points[index] = point;
		}
	}
	
	/**
	 * 方法名：calculateCoordinates 计算每个点的坐标
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
	 * 方法名：resetPointAngle 重新计算每个点的角度
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
	 * 方法名：calculationDeviationAngle 计算偏移角度
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
	 * 方法名：checkCurrentDistance  寻找用户点的那个按钮的标识
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
		//寻找用户点的那个按钮的标识
		checkCurrentDistance(event.getX(), event.getY());
		//调用重写的onPointTouch方法，目的就是传出去
		onTurnplateListener.onPointTouch(chooseBtn);
		
	}
	
	
	/**
	 * 方法名：drawInCenter 把点放到图标中心处
	 */
	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,int flag) {
		canvas.drawPoint(left, top, mPaint);
			canvas.drawBitmap(bitmap, left-bitmap.getWidth()/2, top-bitmap.getHeight()/2, mPaint);
	}
	
	//画出图片区域设置图片的位置
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
		mPaint.setStyle(Paint.Style.STROKE);//绘制空心圆
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {		
		 int action = event.getAction();
		 //判断是哪一种动作
	        switch (action) {
	        case MotionEvent.ACTION_DOWN:
	            break;
	        case MotionEvent.ACTION_MOVE:
	        	resetPointAngle(event.getX(), event.getY());
	        	calculateCoordinates();
	    		invalidate();//初始化系统View自带的一些设置参数
	            break;
	        case MotionEvent.ACTION_UP:
	        	switchScreen(event);
	        	tempDegree = 0;
	        	invalidate();
	            break;
	        case MotionEvent.ACTION_CANCEL:
	        	//系统在运行到一定程度下无法继续响应你的后续动作时会产生此事件。
	        	//一般仅在代码中将其视为异常分支情况处理
	            break;
	        }
		return true;
	}

	 public static interface OnTurnplateListener {
	       /*
	        *定义的新方法 onPointTouch 识别用户点的是哪个按钮，及一些后续的交互
	        */
	     public void onPointTouch(int flag);           	        
	 }

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}
	
	
}
