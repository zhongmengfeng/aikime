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

package com.chaojiyiji.geometerplus.zlibrary.core.filesystem;

import java.util.*;

import com.chaojiyiji.geometerplus.zlibrary.core.library.ZLibrary;

public abstract class ZLResourceFile extends ZLFile {
	private static Map<String,ZLResourceFile> ourCache =
		Collections.synchronizedMap(new HashMap<String,ZLResourceFile>());

	public static ZLResourceFile createResourceFile(String path) {
		ZLResourceFile file = ourCache.get(path);
		if (file == null) {
			file = ZLibrary.Instance().createResourceFile(path);
			ourCache.put(path, file);
		}
		return file;
	}

	static ZLResourceFile createResourceFile(ZLResourceFile parent, String name) {
		return ZLibrary.Instance().createResourceFile(parent, name);
	}

	private final String myPath;

	protected ZLResourceFile(String path) {
		myPath = path;
		init();
	}

	@Override
	public String getPath() {
		return myPath;
	}

	@Override
	public String getLongName() {
		return myPath.substring(myPath.lastIndexOf('/') + 1);
	}

	@Override
	public ZLPhysicalFile getPhysicalFile() {
		return null;
	}
}
