/*
 * This code is in the public domain.
 */

package com.chaojiyiji.geometerplus.android.fbreader.api;

import com.chaojiyiji.geometerplus.android.fbreader.api.ApiObject;

interface ApiInterface {
	ApiObject request(int method, in ApiObject[] parameters);
	List<ApiObject> requestList(int method, in ApiObject[] parameters);
	Map requestMap(int method, in ApiObject[] parameters);
}
