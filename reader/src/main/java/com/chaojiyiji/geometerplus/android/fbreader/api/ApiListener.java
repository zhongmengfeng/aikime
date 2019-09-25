/*
 * This code is in the public domain.
 */

package com.chaojiyiji.geometerplus.android.fbreader.api;

public interface ApiListener {
	String EVENT_READ_MODE_OPENED = "startReading";
	String EVENT_READ_MODE_CLOSED = "stopReading";

	void onEvent(int event);
}
