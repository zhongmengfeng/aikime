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

import com.chaojiyiji.geometerplus.fbreader.network.*;

public class ManageCatalogsItemTree extends NetworkTree {
	public ManageCatalogsItemTree(NetworkTree parent) {
		super(parent);
	}

	@Override
	public String getName() {
		return NetworkLibrary.resource().getResource("manageCatalogs").getValue();
	}

	@Override
	public String getSummary() {
		return NetworkLibrary.resource().getResource("manageCatalogs").getResource("summary").getValue();
	}

	@Override
	protected String getStringId() {
		return "@ManageCatalogs";
	}
}
