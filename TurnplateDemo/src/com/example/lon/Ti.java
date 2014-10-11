package com.example.lon;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.lon.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Ti extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView List = (ListView)findViewById(R.id.listView1);
		//解决滑动时背景变黑
		List.setCacheColorHint(Color.TRANSPARENT);
		List.setAlwaysDrawnWithCacheEnabled(true);
		//生成list，加入数据
	    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	    for(int i=1;i<11;i++){
	    	HashMap<String, Object> map = new HashMap<String, Object>();
	    	map.put("listImage", R.drawable.icon);
	    	map.put("listTitle", "活动简介 "+i);
	    	map.put("listTime", "活动时间:2014年10月10日");
	    	map.put("listDistance", "距离您 "+i+" 米");
	    	map.put("listPhoneNumber", "联系电话：***********");
	    	map.put("listAddress", "地址：*********");
	    	list.add(map);
	    }

	    SimpleAdapter sa=new SimpleAdapter(this, list, R.layout.listview,
	    		new String[] {"listImage","listTitle", "listTime","listDistance","listPhoneNumber","listAddress"},
	    		new int[] {R.id.Image,R.id.Title, R.id.Time,R.id.Distance,R.id.PhoneNumber,R.id.Address});
	    //将适配器添加到List里面并显示
	  List.setAdapter(sa);
	  //为List添加监听事件
	  List.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			setTitle("点击了活动中第"+(position+1)+"个项目");
		}
	});
	//为List添加长按监听事件
	  List.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
		 	menu.setHeaderTitle("长按菜单");   
        	menu.add(0, 0, 0, "弹出菜单1");
        	menu.add(0, 1, 0, "弹出菜单2");   

		}
	});
	  
	}
           //长按响应事件
		public boolean onContextItemSelected(MenuItem item)
	  {
			setTitle("点击了长按菜单里面的第"+(item.getItemId()+1)+"个项目"); 
			//TRUE处理后面的事件可以执行，FALSE处理后面的事件不再执行
			//return super.onContextItemSelected(item);
			return true;
		}
		
}
