package onionisi.judy.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// TODO: fixed up
import onionisi.judy.tool.JudyZmq;

public class PayActivity extends Activity {
	// TODO: no web
	private WebView wv;
	private Button queryBtn, payBtn;
	private EditText orderIdEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pay);
		wv = (WebView) findViewById(R.id.pay_webview);
		queryBtn = (Button) findViewById(R.id.pay_query_Button01);
		payBtn = (Button) findViewById(R.id.pay_Button01);
		orderIdEt = (EditText) findViewById(R.id.pay_order_number_EditText01);

		queryBtn.setOnClickListener(queryListener);
		payBtn.setOnClickListener(payListener);
	}

	OnClickListener queryListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String orderId = orderIdEt.getText().toString();
			// TODO: fixed up need to fix this, no web!!!!
			//String url = HttpUtil.BASE_URL+"servlet/PayServlet?id="+orderId;
			String request = "pay";
			String url = JudyZmq.Query(request);
			wv.loadUrl(url);
		}
	};

	OnClickListener payListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String orderId = orderIdEt.getText().toString();
			// TODO: fixed up payment server
			//String url = HttpUtil.BASE_URL+"servlet/PayMoneyServlet?id="+orderId;
			//String result = HttpUtil.queryStringForPost(url);
			String request = "pmy";
			String result = JudyZmq.Query(request);
			Toast.makeText(PayActivity.this, result, Toast.LENGTH_LONG).show();
			payBtn.setEnabled(false);
		}
	};
}
