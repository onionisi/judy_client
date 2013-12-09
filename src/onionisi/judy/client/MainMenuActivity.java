package onionisi.judy.client;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

// TODO: fixed up
import onionisi.judy.tool.JudyZmq;
import onionisi.judy.tool.Message.ChangeTable;
import onionisi.judy.tool.Message.UnionTable;
import onionisi.judy.tool.Message.UnionTable2;


public class MainMenuActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("Order_system-Main menu");
		setContentView(R.layout.main_menu);
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
	}

	public class ImageAdapter extends BaseAdapter {
		// contruct context
		private Context mContext;
		public ImageAdapter(Context c) {
			mContext = c;
		}
		// get items arg
		public int getCount() {
			return mThumbIds.length;
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
		// get current view
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
				// set image attr
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}
			// set image resource
			imageView.setImageResource(mThumbIds[position]);
			// set listener for every image
			switch (position) {
				case 0:
					imageView.setOnClickListener(orderListener);
					break;
				case 1:
					imageView.setOnClickListener(unionTableListener);
					break;
				case 2:
					imageView.setOnClickListener(changeTableListener);
					break;
				case 3:
					imageView.setOnClickListener(checkTableListener);
					break;
				case 4:
					imageView.setOnClickListener(updateListener);
					break;
				case 6:
					imageView.setOnClickListener(exitListener);
					break;
				case 7:
					imageView.setOnClickListener(payListener);
					break;
				default:
					break;
			}

			return imageView;
		}

		// image source
		private Integer[] mThumbIds = {
			R.drawable.diancai, R.drawable.bingtai,
			R.drawable.zhuantai, R.drawable.chatai,
			R.drawable.gengxin, R.drawable.shezhi,
			R.drawable.zhuxiao, R.drawable.jietai
		};
	}

	// update listener
	OnClickListener updateListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			// start UpdateActivity
			intent.setClass(MainMenuActivity.this, UpdateActivity.class);
			startActivity(intent);
		}
	};

	// check listener
	OnClickListener checkTableListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			// start CheckTableActivity
			intent.setClass(MainMenuActivity.this, CheckTableActivity.class);
			startActivity(intent);
		}
	};

	// pay listener
	OnClickListener payListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			// start PayActivity
			intent.setClass(MainMenuActivity.this, PayActivity.class);
			startActivity(intent);
		}
	};

	// order listener
	OnClickListener orderListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			// start OrderActivity
			intent.setClass(MainMenuActivity.this, OrderActivity.class);
			startActivity(intent);
		}
	};

	// logout listener
	OnClickListener exitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			logout();
		}
	};

	// changeTable listener
	OnClickListener changeTableListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			changeTable();
		}
	};

	// unionTable listener
	OnClickListener unionTableListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			unionTable();
		}
	};

	// changeTable
	private void changeTable() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.change_table, null);

		final EditText et1 = (EditText) v.findViewById(R.id.change_table_order_number_EditText);
		final EditText et2 = (EditText) v.findViewById(R.id.change_table_no_EditText);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("真的要换桌位么?")
			.setCancelable(false)
			.setView(v)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					String orderId = et1.getText().toString();
					String tableId = et2.getText().toString();

					//TODO: fixed up query orderId tableId
					// and change table on server
					// return result
					/*
					   String queryString = "orderId="+orderId+"&tableId="+tableId;
					   String url = HttpUtil.BASE_URL+"servlet/ChangeTableSerlet?"+queryString;
					   String result = HttpUtil.queryStringForPost(url);
					   */
					ChangeTable.Builder change = ChangeTable.newBuilder();

					change.setOrderId(Integer.valueOf(orderId));
					change.setTableId(Integer.valueOf(tableId));

					String request = "cgt" + change.build().toString();
					String result = JudyZmq.Query(request);

					Toast.makeText(MainMenuActivity.this, result, Toast.LENGTH_LONG).show();
				}
			})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// unionTable
	private void unionTable() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.union_table, null);

		final Spinner spinner1 = (Spinner) v.findViewById(R.id.union_table_Spinner1);
		final Spinner spinner2 = (Spinner) v.findViewById(R.id.union_table_Spinner2);

		// TODO: fixed up ->no change lot get spinner item
		// 访问服务器的URL
		//String urlStr = HttpUtil.BASE_URL + "servlet/UnionTableServlet";
		try {
			// 实例化URL
			//URL url = new URL(urlStr);
			// URLConnection 实例
			//URLConnection conn = url.openConnection();
			// 获得输入流
			//InputStream in = conn.getInputStream();
			String request = "utb";
			String result = JudyZmq.Query(request);
			// 获得DocumentBuilderFactory对象
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// 获得DocumentBuilder对象
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 获得Document对象
			Document doc = builder.parse(result);
			// 获得节点列表
			NodeList nl = doc.getElementsByTagName("table");
			// Spinner数据
			List items = new ArrayList();

			// 获得XML数据
			for (int i = 0; i < nl.getLength(); i++) {
				// 桌位编号
				String id = doc.getElementsByTagName("id")
					.item(i).getFirstChild().getNodeValue();
				// 桌号
				int num = Integer.parseInt(doc.getElementsByTagName("num")
						.item(i).getFirstChild().getNodeValue());
				Map data = new HashMap();
				data.put("id", id);
				items.add(data);
			}

			// 获得SpinnerAdapter
			SpinnerAdapter as = new
				SimpleAdapter(this, items,
						android.R.layout.simple_spinner_item,
						new String[] { "id" }, new int[] { android.R.id.text1 });

			// 绑定数据
			spinner1.setAdapter(as);
			spinner2.setAdapter(as);
		} catch (Exception e) {
			e.printStackTrace();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("真的要并桌么?")
			.setCancelable(false)
			.setView(v)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					TextView tv1 = (TextView) spinner1.getSelectedView();
					TextView tv2 = (TextView) spinner2.getSelectedView();
					String tableId1 = tv1.getText().toString();
					String tableId2 = tv2.getText().toString();

					//TODO: fixed up query tableId1 tableId2
					// and union table on server
					// return result
					/*
					   String queryString = "tableId1="+tableId1+"&tableId2="+tableId2;
					   String url = HttpUtil.BASE_URL+"servlet/UnionTableServlet2?"+queryString;
					   String result = HttpUtil.queryStringForPost(url);
					   */
					UnionTable2.Builder union = UnionTable2.newBuilder();

					union.setTableId1(Integer.valueOf(tableId1));
					union.setTableId2(Integer.valueOf(tableId2));

					String request = "ut2" + union.build().toString();

					String result = JudyZmq.Query(request);
					Toast.makeText(MainMenuActivity.this, result, Toast.LENGTH_LONG).show();
				}
			})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// logout
	private void logout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("真的要推出系统么?")
			.setCancelable(false)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//TODO: V2 --> handle logout and login
					/*
					   SharedPreferences pres = getSharedPreferences("user_msg", MODE_WORLD_WRITEABLE);
					   SharedPreferences.Editor editor = pres.edit();
					   editor.putString("id", "");
					   editor.putString("name", "");
					   Intent intent = new Intent();
					   intent.setClass(MainMenuActivity.this, LoginActivity.class);
					   startActivity(intent);
					   */
					String result = "logout now!";
					Toast.makeText(MainMenuActivity.this, result, Toast.LENGTH_LONG).show();
				}
			})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
