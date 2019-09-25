/*
 * Copyright (C) 2007-2015 FBReader.com.chaojiyiji Limited <contact@fbreader.com.chaojiyiji>
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

package com.chaojiyiji.geometerplus.fbreader.formats;

import com.chaojiyiji.geometerplus.zlibrary.core.util.SystemInfo;

import com.chaojiyiji.geometerplus.fbreader.book.AbstractBook;
import com.chaojiyiji.geometerplus.fbreader.book.BookUtil;

public class DjVuPlugin extends ExternalFormatPlugin {
	public DjVuPlugin(SystemInfo systemInfo) {
		super(systemInfo, "DjVu");
	}

	@Override
	public String packageName() {
		return "com.chaojiyiji.geometerplus.fbreader.plugin.djvu";
	}

	@Override
	public void readMetainfo(AbstractBook book) {
		// TODO: implement
	}

	@Override
	public void readUids(AbstractBook book) {
		if (book.uids().isEmpty()) {
			book.addUid(BookUtil.createUid(book, "SHA-256"));
		}
	}
}