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

package com.chaojiyiji.geometerplus.fbreader.network.atom;

import com.chaojiyiji.geometerplus.zlibrary.core.xml.ZLStringMap;

public class ATOMCategory extends ATOMCommonAttributes {
	public static final String TERM = "term";
	public static final String SCHEME = "scheme";
	public static final String LABEL = "label";

	protected ATOMCategory(ZLStringMap source) {
		super(source);
		readAttribute(TERM, source);
		readAttribute(SCHEME, source);
		readAttribute(LABEL, source);
	}

	public final String getTerm() {
		return getAttribute(TERM);
	}

	public final String getScheme() {
		return getAttribute(SCHEME);
	}

	public final String getLabel() {
		return getAttribute(LABEL);
	}
}
