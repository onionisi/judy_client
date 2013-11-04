package onionisi.judy.client;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

// TODO: fixed up
import onionisi.judy.tool.JudyZmq;
import onionisi.judy.tool.Message.CheckTable;
import onionisi.judy.tool.CheckTables;


public class CheckTableActivity extends Activity {
	private GridView gv;
	private int count;
	private int num;
	private int flag;
	private List list = new ArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: V2 -> need more specific
		setTitle("结账");
		setContentView(R.layout.check_table);
		gv = (GridView) findViewById(R.id.check_table_gridview);

		getTableList();
		gv.setAdapter(new ImageAdapter(this));
	}

	// get table info
	private void getTableList() {
		// TODO: fixed up get info from protoc and zmq
		String request = "ckt";
		String result = JudyZmq.Query(request);

		try{  
			CheckTable check = CheckTable.parseFrom(result.getBytes());

			for (CheckTable.subCheckTable sub: check.getSubchecktableList()) {          
				num = sub.getNum();
				flag = sub.getFlag();

				// TODO: fixed up deal with list
				CheckTables ct = new CheckTables();
				ct.setFlag(flag);
				ct.setNum(num);
				list.add(ct);
			}
		}  
		catch(Exception ex){  
			System.out.println(ex.getMessage());  
		} 

	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return list.size();
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = 
				LayoutInflater.from(CheckTableActivity.this);
			View v = null;
			ImageView imageView = null;
			TextView tv = null;

			if (convertView == null) {
				v = inflater.inflate(R.layout.check_table_view, null);
				v.setPadding(8, 8, 8, 8);
			} else {
				v = (View) convertView;
			}
			imageView = (ImageView) v.findViewById(R.id.check_table_ImageView01);
			tv = (TextView) v.findViewById(R.id.check_tableTextView01);

			// get checktable obj
			CheckTables ct = (CheckTables) list.get(position);
			if (ct.getFlag() == 1) {
				imageView.setImageResource(R.drawable.youren);  // using
			} else {
				imageView.setImageResource(R.drawable.kongwei); // not use
			}

			tv.setText(ct.getNum()+"");
			return v;
		}
	}
}
