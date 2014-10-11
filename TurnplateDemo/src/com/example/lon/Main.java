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
		DisplayMetrics dm = new DisplayMetrics(); // ��ʼ����ȡ��Ļ�ֱ�����Ϣ�Ķ���
		this.getWindowManager().getDefaultDisplay().getMetrics(dm); // ����Ļ����Ϣ���������
		// ע�����캯��DisplayMetrics ����Ҫ�����κβ�����getDefaultDisplay() ����
		// ��ȡ�õĿ��ά�ȴ����DisplayMetrics �����У���ȡ�õĿ��ά����������Ϊ��λ(Pixel) ��
		// �����ء���ָ���ǡ��������ء����ǡ�������ء���
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