package onionisi.judy.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Tables implements BaseColumns {
	// anth const
	public static final String AUTHORITY = "onionisi.judy.tools.TABLES";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/table");
	// sort order
	public static final String DEFAULT_SORT_ORDER = "num DESC";
	public static final String NUM = "num";
	public static final String DESCRIPTION = "description";
}
