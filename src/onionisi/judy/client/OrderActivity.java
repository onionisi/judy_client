package onionisi.judy.client;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

// TODO: fixed up
import onionisi.judy.tool.JudyZmq;
import onionisi.judy.tool.Message.StartTable;
import onionisi.judy.tool.Message.OrderDetail;

public class OrderActivity extends Activity
{
	private Spinner tableNoSpinner;            // table number droplist
	private Button orderBtn, addBtn, startBtn; // function button
	private EditText personNumEt;              // customer number
	private ListView lv;                       // order list
	private String orderId;                    // order id
	private List data = new ArrayList();       // data in order_list
	private Map map;                           // specific data in order_list
	private SimpleAdapter sa;                  // ListView Adapter
	private int[] to = new int[5];             // TextView Drawable ID
	private String[] from = { "id" , "name", "num", "price", "remark" };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

		tableNoSpinner = (Spinner) findViewById(R.id.tableNoSpinner);
		setTableAdapter();

		startBtn = (Button) findViewById(R.id.startButton02);
		startBtn.setOnClickListener(startListener);

		addBtn = (Button) findViewById(R.id.addMealButton01);
		addBtn.setOnClickListener(addListener);

		orderBtn = (Button) findViewById(R.id.orderButton02);
		orderBtn.setOnClickListener(orderListener);

		personNumEt = (EditText) findViewById(R.id.personNumEditText02);

		lv = (ListView) findViewById(R.id.orderDetailListView01);
		setMenusAdapter();
	}

	// binding data to ListView:lv
	private void setMenusAdapter() {
		to[0] = R.id.id_ListView;
		to[1] = R.id.name_ListView;
		to[2] = R.id.num_ListView;
		to[3] = R.id.price_ListView;
		to[4] = R.id.remark_ListView;

		sa = new SimpleAdapter(this, data, R.layout.listview, from, to);
		lv.setAdapter(sa);
	}

	// binding data to table number drop_list:tableNoSpinner
	private void setTableAdapter() {
		// get sqlite tabletbl uri
		Uri uri = Uri.parse("content://onionisi.judy.provider.TABLES/table");
		String[] projection = { "_id", "num", "description" };
		Cursor c = managedQuery(uri, projection, null, null, null);

		SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(this, 
				android.R.layout.simple_spinner_item, c,
				new String[] { "_id" }, new int[] { android.R.id.text1 });

		tableNoSpinner.setAdapter(adapter2);
	}

	// start listener
	private OnClickListener startListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yy:mm:dd hh:MM");
			String orderTime = sdf.format(date);

			// TODO: V2 -> user_id suport 
			//SharedPreferences pres = getSharedPreferences("user_msg", MODE_WORLD_READABLE);
			//String userId = pres.getString("id", "");

			TextView tv = (TextView) tableNoSpinner.getSelectedView();
			String tableId = tv.getText().toString();            // tableID
			String personNum = personNumEt.getText().toString(); // persons

			//TODO: fixed up start table -> conn
			StartTable.Builder start = StartTable.newBuilder();

			start.setOrderTime(orderTime);
			start.setTableId(tableId);
			start.setPersonNum(personNum);

			String request = "stb" + start.build().toString();

			String result = JudyZmq.Query(request);

			Toast.makeText(OrderActivity.this, result, Toast.LENGTH_LONG).show();
		}
	};

	// add_meal listener
	private OnClickListener addListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			addMeal();
		}
	};

	private void addMeal() {
		LayoutInflater inflater = LayoutInflater.from(this);
		final View v = inflater.inflate(R.layout.order_detail, null);
		final Spinner menuSpinner = (Spinner) v.findViewById(R.id.menuSpinner);
		EditText numEt = (EditText) v.findViewById(R.id.numEditText);
		EditText remarkEt = (EditText) v.findViewById(R.id.add_remarkEditText);

		// operate local sqlite MenuTbl
		Uri uri = Uri.parse("content://onionisi.judy.provider.MENUS/menu1");
		String[] projection = { "_id", "name", "price" };
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(uri, projection, null, null, null);
		SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(this, 
				R.layout.spinner_lo, c,
				new String[] { "_id", "price", "name" }, new int[] {
					R.id.id_TextView01, R.id.price_TextView02,
							R.id.name_TextView03, });

		menuSpinner.setAdapter(adapter2);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("请点菜:")
			.setView(v)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					LinearLayout v1 = (LinearLayout) menuSpinner.getSelectedView();          // get customize view
					TextView id_tv = (TextView) v1.findViewById(R.id.id_TextView01);         // dish id
					TextView price_tv = (TextView) v1.findViewById(R.id.price_TextView02);   // dish price
					TextView name_tv = (TextView) v1.findViewById(R.id.name_TextView03);     // dish name
					EditText num_et = (EditText) v.findViewById(R.id.numEditText);           // dish number
					EditText remark_et = (EditText) v.findViewById(R.id.add_remarkEditText); // remarks

					String idStr = id_tv.getText().toString();
					String priceStr = price_tv.getText().toString();
					String nameStr = name_tv.getText().toString();
					String numStr = num_et.getText().toString();
					String remarkStr = remark_et.getText().toString();

					map = new HashMap();

					map.put("id", idStr);
					map.put("name", nameStr);
					map.put("num", numStr);
					map.put("price", priceStr);
					map.put("remark", remarkStr);

					data.add(map);

					to[0] = R.id.id_ListView;
					to[1] = R.id.name_ListView;
					to[2] = R.id.price_ListView;
					to[3] = R.id.remark_ListView;

					sa = new SimpleAdapter(OrderActivity.this, data, 
							R.layout.listview, from, to);
					lv.setAdapter(sa);
				}
			}).setNegativeButton("取消", null);
		AlertDialog alert = builder.create();
		alert.show();
	}

	// order listener
	private OnClickListener orderListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			for (int i = 0; i < data.size(); i++) {
				Map map = (Map) data.get(i);

				String menuId = (String) map.get("id");
				String num = (String) map.get("num");
				String remark = (String) map.get("remark");
				String myOrderId = orderId;

				// TODO: fixed up table order -> conn
				OrderDetail.Builder order = OrderDetail.newBuilder();

				order.setMenuId(menuId);
				order.setOrderId(myOrderId);
				order.setNum(num);
				order.setRemark(remark);

				String request = "odt" + order.build().toString();

				String result = JudyZmq.Query(request);
				
				Toast.makeText(OrderActivity.this, result, Toast.LENGTH_LONG).show();

				finish();
			}
		}
	};
}
