/*
 * Copyright (C) 2012-2015 FBReader.com.chaojiyiji Limited <contact@fbreader.com.chaojiyiji>
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

package com.chaojiyiji.geometerplus.android.fbreader;

import com.chaojiyiji.geometerplus.fbreader.book.Book;
import com.chaojiyiji.geometerplus.fbreader.book.BookUtil;
import com.chaojiyiji.geometerplus.fbreader.fbreader.FBReaderApp;

public class ShareBookAction extends FBAndroidAction {
	ShareBookAction(FBReader baseActivity, FBReaderApp fbreader) {
		super(baseActivity, fbreader);
	}

	@Override
	public boolean isVisible() {
		final Book book = Reader.getCurrentBook();
		return book != null && BookUtil.fileByBook(book).getPhysicalFile() != null;
	}

	@Override
	protected void run(Object ... params) {
		FBUtil.shareBook(BaseActivity, Reader.getCurrentBook());
	}
}
