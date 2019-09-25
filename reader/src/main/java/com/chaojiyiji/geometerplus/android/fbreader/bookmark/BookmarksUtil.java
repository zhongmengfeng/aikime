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

package com.chaojiyiji.geometerplus.android.fbreader.bookmark;

import yuku.ambilwarna.widget.AmbilWarnaPrefWidgetView;

import com.chaojiyiji.geometerplus.zlibrary.core.util.ZLColor;
import com.chaojiyiji.geometerplus.zlibrary.ui.android.util.ZLAndroidColorUtil;
import com.chaojiyiji.geometerplus.fbreader.book.HighlightingStyle;

abstract class BookmarksUtil {
	static void setupColorView(AmbilWarnaPrefWidgetView colorView, HighlightingStyle style) {
		Integer rgb = null;
		if (style != null) {
			final ZLColor color = style.getBackgroundColor();
			if (color != null) {
				rgb = ZLAndroidColorUtil.rgb(color);
			}
		}

		if (rgb != null) {
			colorView.showCross(false);
			colorView.setBackgroundColor(rgb);
		} else {
			colorView.showCross(true);
			colorView.setBackgroundColor(0);
		}
	}
}
