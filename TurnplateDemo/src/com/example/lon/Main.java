package com.example.lon;
import com.example.lon.R;
import com.example.lon.RevolveView.OnTurnplateListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class Main extends Activity implements OnTurnplateListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics(); // 初始化获取屏幕分辨率信息的对象
		this.getWindowManager().getDefaultDisplay().getMetrics(dm); // 将屏幕的信息放入对象中
		// 注：构造函数DisplayMetrics 不需要传递任何参数；getDefaultDisplay() 方法
		// 将取得的宽高维度存放于DisplayMetrics 对象中，而取得的宽高维度是以像素为单位(Pixel) ，
		// “像素”所指的是“绝对像素”而非“相对像素”。
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		RevolveView view = new RevolveView(getApplicationContext(), width / 2,
				height / 2 - 80, 100);
		view.setOnTurnplateListener(this);
		// LayoutInflater inflater = getLayoutInflater();
		view.setBackgroundResource(R.drawable.menubkground);
		setContentView(view);
	}

	@Override
	public void onPointTouch(int flag) {
		if (flag == 1) {

			Intent in = new Intent();
			in.setClass(Main.this, Ti.class);
			Main.this.startActivity(in);
		}

	}
}