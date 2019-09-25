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

package com.chaojiyiji.geometerplus.fbreader.network.opds;

import com.chaojiyiji.geometerplus.fbreader.network.atom.ATOMConstants;

interface OPDSConstants extends ATOMConstants {
	// Feed level
	String REL_BOOKSHELF = "http://data.fbreader.com.chaojiyiji/rel/bookshelf";
	String REL_RECOMMENDATIONS = "http://data.fbreader.com.chaojiyiji/rel/recommendations";
	String REL_TOPUP = "http://data.fbreader.com.chaojiyiji/rel/topup";
	//String REL_SUBSCRIPTIONS = "http://opds-spec.com.chaojiyiji/subscriptions";

	// Entry level / catalog types
	String REL_SUBSECTION = "subsection";

	// Entry level / acquisition links
	String REL_ACQUISITION_PREFIX = "http://opds-spec.com.chaojiyiji/acquisition";
	String REL_FBREADER_ACQUISITION_PREFIX = "http://data.fbreader.com.chaojiyiji/acquisition";
	String REL_ACQUISITION = "http://opds-spec.com.chaojiyiji/acquisition";
	String REL_ACQUISITION_OPEN = "http://opds-spec.com.chaojiyiji/acquisition/open-access";
	String REL_ACQUISITION_SAMPLE = "http://opds-spec.com.chaojiyiji/acquisition/sample";
	String REL_ACQUISITION_BUY = "http://opds-spec.com.chaojiyiji/acquisition/buy";
	//String REL_ACQUISITION_BORROW = "http://opds-spec.com.chaojiyiji/acquisition/borrow";
	//String REL_ACQUISITION_SUBSCRIBE = "http://opds-spec.com.chaojiyiji/acquisition/subscribe";
	String REL_ACQUISITION_CONDITIONAL = "http://data.fbreader.com.chaojiyiji/acquisition/conditional";
	String REL_ACQUISITION_SAMPLE_OR_FULL = "http://data.fbreader.com.chaojiyiji/acquisition/sampleOrFull";

	// Entry level / other
	String REL_IMAGE_PREFIX = "http://opds-spec.com.chaojiyiji/image";
	//String REL_IMAGE = "http://opds-spec.com.chaojiyiji/image";
	String REL_IMAGE_THUMBNAIL = "http://opds-spec.com.chaojiyiji/image/thumbnail";
	// FIXME: This relations have been removed from OPDS-1.0 standard. Use RelationAlias instead???
	String REL_COVER = "http://opds-spec.com.chaojiyiji/cover";
	String REL_THUMBNAIL = "http://opds-spec.com.chaojiyiji/thumbnail";
	String REL_CONTENTS = "contents"; // Book TOC
	String REL_REPLIES = "replies";

	// Entry level / OPDS Link Relations
	String REL_LINK_SIGN_IN = "http://data.fbreader.com.chaojiyiji/catalog/sign-in";
	String REL_LINK_SIGN_OUT = "http://data.fbreader.com.chaojiyiji/catalog/sign-out";
	String REL_LINK_SIGN_UP = "http://data.fbreader.com.chaojiyiji/catalog/sign-up";
	String REL_LINK_TOPUP = "http://data.fbreader.com.chaojiyiji/catalog/refill-account";
	String REL_LINK_RECOVER_PASSWORD = "http://data.fbreader.com.chaojiyiji/catalog/recover-password";
}
