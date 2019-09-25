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

package com.chaojiyiji.geometerplus.zlibrary.core.view;

import com.chaojiyiji.geometerplus.zlibrary.core.library.ZLibrary;
import com.chaojiyiji.geometerplus.zlibrary.core.util.ZLColor;

public abstract class SelectionCursor {
	public enum Which {
		Left,
		Right
	}

	public static void draw(ZLPaintContext context, Which which, int x, int y, ZLColor color) {
		context.setFillColor(color);
		final int dpi = ZLibrary.Instance().getDisplayDPI();
		final int unit = dpi / 160;
		final int xCenter = which == Which.Left ? x - unit - 1 : x + unit + 1;
		context.fillRectangle(xCenter - unit, y + dpi / 8, xCenter + unit, y - dpi / 8);
		if (which == Which.Left) {
			context.fillCircle(xCenter, y - dpi / 8, unit * 6);
		} else {
			context.fillCircle(xCenter, y + dpi / 8, unit * 6);
		}
	}

	/*public static void draw(ZLPaintContext context, Which which, int x, int y, ZLColor color) {
		context.setFillColor(color);
		final int dpi = ZLibrary.Instance().getDisplayDPI();
		final int unit = dpi / 120;
		final int xCenter = which == Which.Left ? x - unit - 1 : x + unit + 1;
		context.fillRectangle(xCenter - unit, y + dpi / 8, xCenter + unit, y - dpi / 8);
		if (which == Which.Left) {
			context.fillCircle(xCenter, y - dpi / 8, unit * 6);
		} else {
			context.fillCircle(xCenter, y + dpi / 8, unit * 6);
		}
	}*/
}
