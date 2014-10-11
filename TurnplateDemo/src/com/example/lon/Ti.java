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
		//�������ʱ�������
		List.setCacheColorHint(Color.TRANSPARENT);
		List.setAlwaysDrawnWithCacheEnabled(true);
		//����list����������
	    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	    for(int i=1;i<11;i++){
	    	HashMap<String, Object> map = new HashMap<String, Object>();
	    	map.put("listImage", R.drawable.icon);
	    	map.put("listTitle", "���� "+i);
	    	map.put("listTime", "�ʱ��:2014��10��10��");
	    	map.put("listDistance", "������ "+i+" ��");
	    	map.put("listPhoneNumber", "��ϵ�绰��***********");
	    	map.put("listAddress", "��ַ��*********");
	    	list.add(map);
	    }

	    SimpleAdapter sa=new SimpleAdapter(this, list, R.layout.listview,
	    		new String[] {"listImage","listTitle", "listTime","listDistance","listPhoneNumber","listAddress"},
	    		new int[] {R.id.Image,R.id.Title, R.id.Time,R.id.Distance,R.id.PhoneNumber,R.id.Address});
	    //����������ӵ�List���沢��ʾ
	  List.setAdapter(sa);
	  //ΪList��Ӽ����¼�
	  List.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			setTitle("����˻�е�"+(position+1)+"����Ŀ");
		}
	});
	//ΪList��ӳ��������¼�
	  List.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
		 	menu.setHeaderTitle("�����˵�");   
        	menu.add(0, 0, 0, "�����˵�1");
        	menu.add(0, 1, 0, "�����˵�2");   

		}
	});
	  
	}
           //������Ӧ�¼�
		public boolean onContextItemSelected(MenuItem item)
	  {
			setTitle("����˳����˵�����ĵ�"+(item.getItemId()+1)+"����Ŀ"); 
			//TRUE���������¼�����ִ�У�FALSE���������¼�����ִ��
			//return super.onContextItemSelected(item);
			return true;
		}
		
}
