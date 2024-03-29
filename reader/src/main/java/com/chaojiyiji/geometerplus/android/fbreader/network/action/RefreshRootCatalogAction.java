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

import com.chaojiyiji.geometerplus.fbreader.network.NetworkTree;
import com.chaojiyiji.geometerplus.fbreader.network.NetworkLibrary;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;

import com.chaojiyiji.geometerplus.android.fbreader.network.NetworkLibraryActivity;

public class RefreshRootCatalogAction extends RootAction {
	public RefreshRootCatalogAction(NetworkLibraryActivity activity) {
		super(activity, ActionCode.REFRESH, "refreshCatalogsList", R.drawable.ic_menu_refresh);
	}

	@Override
	public boolean isEnabled(NetworkTree tree) {
		return !myLibrary.isUpdateInProgress();
	}

	@Override
	public void run(NetworkTree tree) {
		myLibrary.runBackgroundUpdate(true);
		((NetworkLibraryActivity)myActivity).requestCatalogPlugins();
	}
}
