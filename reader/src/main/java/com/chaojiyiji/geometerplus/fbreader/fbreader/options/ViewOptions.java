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

package com.chaojiyiji.geometerplus.fbreader.fbreader.options;

import com.chaojiyiji.geometerplus.zlibrary.core.library.ZLibrary;
import com.chaojiyiji.geometerplus.zlibrary.core.options.*;
import com.chaojiyiji.geometerplus.zlibrary.text.view.style.ZLTextStyleCollection;

import com.chaojiyiji.geometerplus.fbreader.fbreader.FBView;

public class ViewOptions {
	public final ZLBooleanOption TwoColumnView;
	public final ZLIntegerRangeOption LeftMargin;
	public final ZLIntegerRangeOption RightMargin;
	public final ZLIntegerRangeOption TopMargin;
	public final ZLIntegerRangeOption BottomMargin;
	public final ZLIntegerRangeOption SpaceBetweenColumns;
	public final ZLIntegerRangeOption ScrollbarType;
	public final ZLIntegerRangeOption FooterHeight;
	public final ZLStringOption ColorProfileName;

	private ColorProfile myColorProfile;
	private ZLTextStyleCollection myTextStyleCollection;
	private FooterOptions myFooterOptions;

	public ViewOptions() {
		final ZLibrary zlibrary = ZLibrary.Instance();

		final int dpi = zlibrary.getDisplayDPI();
		final int x = zlibrary.getWidthInPixels();
		final int y = zlibrary.getHeightInPixels();
		final int horMargin = Math.min(dpi / 5, Math.min(x, y) / 30);

		TwoColumnView =
				new ZLBooleanOption("Options", "TwoColumnView", x * x + y * y >= 42 * dpi * dpi);
		LeftMargin =
				new ZLIntegerRangeOption("Options", "LeftMargin", 10, 100, horMargin);
		RightMargin =
				new ZLIntegerRangeOption("Options", "RightMargin", 10, 100, horMargin);
		TopMargin =
				new ZLIntegerRangeOption("Options", "TopMargin", 21, 100, 21);
		BottomMargin =
				new ZLIntegerRangeOption("Options", "BottomMargin", 10, 100, 0);
		SpaceBetweenColumns =
				new ZLIntegerRangeOption("Options", "SpaceBetweenColumns", 0, 200, horMargin);
		ScrollbarType =
				new ZLIntegerRangeOption("Options", "ScrollbarType", 0, 0, FBView.SCROLLBAR_SHOW_AS_FOOTER);
		FooterHeight =
				new ZLIntegerRangeOption("Options", "FooterHeight", 0, 0, 0);
		ColorProfileName =
				new ZLStringOption("Options", "ColorProfile", ColorProfile.DAY);
		ColorProfileName.setSpecialName("colorProfile");
	}

	/*public ViewOptions() {
		final ZLibrary zlibrary = ZLibrary.Instance();

		final int dpi = zlibrary.getDisplayDPI();
		final int x = zlibrary.getWidthInPixels();
		final int y = zlibrary.getHeightInPixels();
		final int horMargin = Math.min(dpi / 5, Math.min(x, y) / 30);

		TwoColumnView =
			new ZLBooleanOption("Options", "TwoColumnView", x * x + y * y >= 42 * dpi * dpi);
		LeftMargin =
			new ZLIntegerRangeOption("Options", "LeftMargin", 0, 100, horMargin);
		RightMargin =
			new ZLIntegerRangeOption("Options", "RightMargin", 0, 100, horMargin);
		TopMargin =
			new ZLIntegerRangeOption("Options", "TopMargin", 0, 100, 0);
		BottomMargin =
			new ZLIntegerRangeOption("Options", "BottomMargin", 0, 100, 4);
		SpaceBetweenColumns =
			new ZLIntegerRangeOption("Options", "SpaceBetweenColumns", 0, 300, 3 * horMargin);
//		ScrollbarType =
//			new ZLIntegerRangeOption("Options", "ScrollbarType", 0, 4, FBView.SCROLLBAR_SHOW_AS_FOOTER);
		//将上面两行修改如下，实现了屏蔽页脚进度条的功能
		ScrollbarType =
			new ZLIntegerRangeOption("Options", "ScrollbarType", 0, 4, 0);

		FooterHeight =
			new ZLIntegerRangeOption("Options", "FooterHeight", 8, dpi / 8, dpi / 20);
		ColorProfileName =
			new ZLStringOption("Options", "ColorProfile", ColorProfile.DAY);
		ColorProfileName.setSpecialName("colorProfile");
	}*/

	public ColorProfile getColorProfile() {
		final String name = ColorProfileName.getValue();
		if (myColorProfile == null || !name.equals(myColorProfile.Name)) {
			myColorProfile = ColorProfile.get(name);
		}
		return myColorProfile;
	}

	public ZLTextStyleCollection getTextStyleCollection() {
		if (myTextStyleCollection == null) {
			myTextStyleCollection = new ZLTextStyleCollection("Base");
		}
		return myTextStyleCollection;
	}

	public FooterOptions getFooterOptions() {
		if (myFooterOptions == null) {
			myFooterOptions = new FooterOptions();
		}
		return myFooterOptions;
	}
}
