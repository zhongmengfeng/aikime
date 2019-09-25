/*
 * Copyright (C) 2010-2015 FBReader.com.chaojiyiji Limited <contact@fbreader.com.chaojiyiji>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.chaojiyiji.geometerplus.android.fbreader.network.action;

import android.app.Activity;
import android.content.Intent;

import com.chaojiyiji.geometerplus.zlibrary.core.network.ZLNetworkContext;
import com.chaojiyiji.geometerplus.zlibrary.core.network.ZLNetworkException;

import com.chaojiyiji.geometerplus.fbreader.network.NetworkTree;

import com.chaojiyiji.geometerplus.android.fbreader.network.NetworkBookInfoActivity;
import com.chaojiyiji.geometerplus.android.fbreader.network.NetworkLibraryActivity;

import com.chaojiyiji.geometerplus.android.util.OrientationUtil;
import com.chaojiyiji.geometerplus.android.util.UIUtil;

public class ShowBookInfoAction extends BookAction {
	private final ZLNetworkContext myNetworkContext;

	public ShowBookInfoAction(Activity activity, ZLNetworkContext nc) {
		super(activity, ActionCode.SHOW_BOOK_ACTIVITY, "bookInfo");
		myNetworkContext = nc;
	}

	@Override
	public void run(final NetworkTree tree) {
		if (getBook(tree).isFullyLoaded()) {
			showBookInfo(tree);
		} else {
			UIUtil.wait("loadInfo", new Runnable() {
				public void run() {
					getBook(tree).loadFullInformation(myNetworkContext);
					myActivity.runOnUiThread(new Runnable() {
						public void run() {
							showBookInfo(tree);
						}
					});
				}
			}, myActivity);
		}
	}

	private void showBookInfo(NetworkTree tree) {
		OrientationUtil.startActivityForResult(
			myActivity,
			new Intent(myActivity, NetworkBookInfoActivity.class)
				.putExtra(NetworkLibraryActivity.TREE_KEY_KEY, tree.getUniqueKey()),
			1
		);
	}
}
