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

package com.chaojiyiji.geometerplus.fbreader.network.tree;

import com.chaojiyiji.geometerplus.zlibrary.core.image.ZLImage;
import com.chaojiyiji.geometerplus.zlibrary.core.network.ZLNetworkException;
import com.chaojiyiji.geometerplus.zlibrary.core.money.Money;

import com.chaojiyiji.geometerplus.fbreader.network.TopUpItem;
import com.chaojiyiji.geometerplus.fbreader.network.NetworkTree;
import com.chaojiyiji.geometerplus.fbreader.network.authentication.NetworkAuthenticationManager;

public class TopUpTree extends NetworkTree {
	public final TopUpItem Item;

	TopUpTree(NetworkCatalogTree parentTree, TopUpItem item) {
		super(parentTree);
		Item = item;
	}

	@Override
	public String getName() {
		return Item.Title.toString();
	}

	@Override
	public String getSummary() {
		final NetworkAuthenticationManager mgr = getLink().authenticationManager();
		try {
			if (mgr != null && mgr.isAuthorised(false)) {
				final Money account = mgr.currentAccount();
				final CharSequence summary = Item.getSummary();
				if (account != null && summary != null) {
					return summary.toString().replace("%s", account.toString());
				}
			}
		} catch (ZLNetworkException e) {
		}
		return null;
	}

	@Override
	protected ZLImage createCover() {
		return createCoverForItem(Library, Item, true);
	}

	@Override
	protected String getStringId() {
		return "@TopUp Account";
	}
}
