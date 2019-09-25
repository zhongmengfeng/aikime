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

import com.chaojiyiji.geometerplus.zlibrary.core.network.ZLNetworkContext;
import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;

import com.chaojiyiji.geometerplus.fbreader.network.*;
import com.chaojiyiji.geometerplus.fbreader.network.tree.NetworkCatalogTree;
import com.chaojiyiji.geometerplus.fbreader.network.urlInfo.UrlInfo;

import com.chaojiyiji.geometerplus.android.fbreader.network.NetworkLibraryActivity;

public class ReloadCatalogAction extends CatalogAction {
	private final ZLNetworkContext myNetworkContext;

	public ReloadCatalogAction(NetworkLibraryActivity activity, ZLNetworkContext nc) {
		super(activity, ActionCode.RELOAD_CATALOG, "reload", R.drawable.ic_menu_refresh);
		myNetworkContext = nc;
	}

	@Override
	public boolean isVisible(NetworkTree tree) {
		if (!super.isVisible(tree)) {
			return false;
		}
		final NetworkCatalogItem item = ((NetworkCatalogTree)tree).Item;
		if (!(item instanceof NetworkURLCatalogItem)) {
			return false;
		}
		return ((NetworkURLCatalogItem)item).getUrl(UrlInfo.Type.Catalog) != null;
	}

	@Override
	public boolean isEnabled(NetworkTree tree) {
		return myLibrary.getStoredLoader(tree) == null;
	}

	@Override
	public void run(NetworkTree tree) {
		if (myLibrary.getStoredLoader(tree) != null) {
			return;
		}
		((NetworkCatalogTree)tree).clearCatalog();
		((NetworkCatalogTree)tree).startItemsLoader(myNetworkContext, false, false);
	}
}
