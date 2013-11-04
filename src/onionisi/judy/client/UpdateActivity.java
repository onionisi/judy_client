package onionisi.judy.client;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

// TODO: fixed up
import onionisi.judy.tool.JudyZmq;
import onionisi.judy.tool.Message.Update;
import onionisi.judy.provider.Menus;

public class UpdateActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: V2 -> more specific
		setTitle("数据同步");

		ListView listView = getListView();
		String[] items = {"update menutbl", "update tabletbl"};

		ListAdapter adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, items);

		listView.setAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
			case 0: 		// menu
				confirm(1);
				break;
			case 1: 		// table
				confirm(2);
				break;
			default:
				break;
		}
	}

	private void confirm(final int item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("真的需要更新么？")
			.setCancelable(false)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					if (item == 1) {
						updateMenu();
					} else {
						updateTable();
					}
				}
			}).setNegativeButton("取消", 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
			});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void updateMenu() {
		//String urlStr = HttpUtil.BASE_URL + "servlet/UpdateServlet";
		try {
			// TODO: fixed up get data for value
			//URL url = new URL(urlStr);
			//URLConnection conn = url.openConnection();
			//InputStream in = conn.getInputStream();
			String request = "pdt";
			String result = JudyZmq.Query(request);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(result);
			NodeList nl = doc.getElementsByTagName("menu");

			ContentResolver cr = getContentResolver();
			Uri uri1 = Menus.CONTENT_URI;
			cr.delete(uri1, null, null);

			// TODO: fixed no change handle values
			for (int i = 0; i < nl.getLength(); i++) {
				ContentValues values = new ContentValues();

				int id = Integer.parseInt(doc.getElementsByTagName("id")
						.item(i).getFirstChild().getNodeValue());
				String name = doc.getElementsByTagName("name").item(i)
						.getFirstChild().getNodeValue();
				String pic = doc.getElementsByTagName("pic").item(i)
						.getFirstChild().getNodeValue();
				int price = Integer.parseInt(doc.getElementsByTagName("price")
						.item(i).getFirstChild().getNodeValue());
				int typeId = Integer.parseInt(doc
						.getElementsByTagName("typeId").item(i).getFirstChild()
						.getNodeValue());
				String remark = doc.getElementsByTagName("remark").item(i)
						.getFirstChild().getNodeValue();
				
				values.put("_id", id);
				values.put("name", name);
				values.put("price", price);
				values.put("pic", pic);
				values.put("typeId", typeId);
				values.put("remark", remark);

				cr.insert(uri1, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateTable() {

	}
}
