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

package com.chaojiyiji.geometerplus.android.fbreader.preferences;

import android.content.Context;

import com.chaojiyiji.geometerplus.zlibrary.core.options.ZLBooleanOption;
import com.chaojiyiji.geometerplus.zlibrary.core.resources.ZLResource;

class ZLBooleanPreference extends ZLCheckBoxPreference {
	private final ZLBooleanOption myOption;

	ZLBooleanPreference(Context context, ZLBooleanOption option, ZLResource resource) {
		super(context, resource);
		myOption = option;
		setChecked(option.getValue());
	}

	@Override
	protected void onClick() {
		super.onClick();
		myOption.setValue(isChecked());
	}
}