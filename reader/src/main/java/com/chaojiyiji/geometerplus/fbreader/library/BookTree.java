/*
 * Copyright (C) 2009-2015 FBReader.com.chaojiyiji Limited <contact@fbreader.com.chaojiyiji>
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

package com.chaojiyiji.geometerplus.fbreader.library;

import com.chaojiyiji.geometerplus.zlibrary.core.image.ZLImage;

import com.chaojiyiji.geometerplus.fbreader.book.*;
import com.chaojiyiji.geometerplus.fbreader.formats.PluginCollection;
import com.chaojiyiji.geometerplus.fbreader.tree.FBTree;

public class BookTree extends LibraryTree {
	public final Book Book;

	BookTree(IBookCollection<Book> collection, PluginCollection pluginCollection, Book book) {
		super(collection, pluginCollection);
		Book = book;
	}

	BookTree(LibraryTree parent, Book book) {
		super(parent);
		Book = book;
	}

	BookTree(LibraryTree parent, Book book, int position) {
		super(parent, position);
		Book = book;
	}

	@Override
	public String getName() {
		return Book.getTitle();
	}

	@Override
	public String getSummary() {
		return "";
	}

	@Override
	public Book getBook() {
		return Book;
	}

	@Override
	protected String getStringId() {
		return "@BookTree " + getName();
	}

	@Override
	protected ZLImage createCover() {
		return CoverUtil.getCover(Book, PluginCollection);
	}

	@Override
	public boolean containsBook(Book book) {
		return Collection.sameBook(book, Book);
	}

	@Override
	protected String getSortKey() {
		return Book.getSortKey();
	}

	@Override
	public int compareTo(FBTree tree) {
		final int cmp = super.compareTo(tree);
		if (cmp == 0 && tree instanceof BookTree) {
			final Book b = ((BookTree)tree).Book;
			if (Book != null && b != null) {
				return Book.getPath().compareTo(b.getPath());
			}
		}
		return cmp;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof BookTree)) {
			return false;
		}
		return Book.equals(((BookTree)object).Book);
	}
}
