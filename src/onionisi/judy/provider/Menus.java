package onionisi.judy.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Menus implements BaseColumns {
	// anth const
	public static final String AUTHORITY = "onionisi.judy.tools.MENUS";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/menu1");
	// sort order
	public static final String DEFAULT_SORT_ORDER = "typeId DESC";

	public static final String TYPE_ID = "typeId";
	public static final String NAME    = "name";
	public static final String PRICE   = "price";
	public static final String PIC     = "pic";
	public static final String REMARK  = "remark";
}
