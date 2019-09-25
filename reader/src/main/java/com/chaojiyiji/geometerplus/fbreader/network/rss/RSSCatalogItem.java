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

package com.chaojiyiji.geometerplus.fbreader.network.rss;

import java.util.HashSet;

import com.chaojiyiji.geometerplus.zlibrary.core.network.ZLNetworkException;
import com.chaojiyiji.geometerplus.zlibrary.core.network.ZLNetworkRequest;
import com.chaojiyiji.geometerplus.zlibrary.core.util.MimeType;

import com.chaojiyiji.geometerplus.fbreader.network.*;
import com.chaojiyiji.geometerplus.fbreader.network.tree.NetworkItemsLoader;
import com.chaojiyiji.geometerplus.fbreader.network.urlInfo.UrlInfoCollection;

public class RSSCatalogItem extends NetworkURLCatalogItem {
	static class State extends NetworkOperationData {
		public String LastLoadedId;
		public final HashSet<String> LoadedIds = new HashSet<String>();

		public State(RSSNetworkLink link, NetworkItemsLoader loader) {
			super(link, loader);
		}
	}
	private State myLoadingState;

	protected RSSCatalogItem(INetworkLink link, CharSequence title,
			CharSequence summary, UrlInfoCollection<?> urls,
			Accessibility accessibility, int flags) {
		super(link, title, summary, urls, accessibility, flags);
	}

	@Override
	public void loadChildren(NetworkItemsLoader loader) throws ZLNetworkException {

		final RSSNetworkLink rssLink = (RSSNetworkLink)Link;
		myLoadingState = rssLink.createOperationData(loader);

		doLoadChildren(rssLink.createNetworkData(getCatalogUrl(), myLoadingState));
	}

	private void doLoadChildren(ZLNetworkRequest networkRequest) throws ZLNetworkException {
		try {
			super.doLoadChildren(myLoadingState, networkRequest);
		} catch (ZLNetworkException e) {
			myLoadingState = null;
			throw e;
		}
	}

}
