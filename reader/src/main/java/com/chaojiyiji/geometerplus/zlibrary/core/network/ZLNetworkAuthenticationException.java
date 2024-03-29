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

package com.chaojiyiji.geometerplus.zlibrary.core.network;

import com.chaojiyiji.geometerplus.zlibrary.core.resources.ZLResource;

public class ZLNetworkAuthenticationException extends ZLNetworkException {
	public static final String ERROR_AUTHENTICATION_FAILED = "authenticationFailed";

	public ZLNetworkAuthenticationException() {
		super(errorMessage(ERROR_AUTHENTICATION_FAILED));
	}

	public ZLNetworkAuthenticationException(String message) {
		super(message);
	}

	public ZLNetworkAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
