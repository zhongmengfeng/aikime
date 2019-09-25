/****************************************************************************************
 * Copyright (c) 2013 Bibek Shrestha <bibekshrestha@gmail.com>                          *
 * Copyright (c) 2013 Zaur Molotnikov <qutorial@gmail.com>                              *
 * Copyright (c) 2013 Nicolas Raoul <nicolas.raoul@gmail.com>                           *
 * Copyright (c) 2013 Flavio Lerda <flerda@gmail.com>                                   *
 *                                                                                      *
 * This program is free software; you can redistribute it and/or modify it under        *
 * the terms of the GNU General Public License as published by the Free Software        *
 * Foundation; either version 3 of the License, or (at your option) any later           *
 * version.                                                                             *
 *                                                                                      *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY      *
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      *
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.             *
 *                                                                                      *
 * You should have received a copy of the GNU General Public License along with         *
 * this program.  If not, see <http://www.gnu.org/licenses/>.                           *
 ****************************************************************************************/

package com.ichi2yiji.anki.multimediacard.language;

import android.content.Context;


import java.util.Locale;

/**
 * This one listers services in beolingus.
 * <p>
 * It is used to load pronunciation.
 */
public class LanguageListerBeolingus extends LanguageListerBase {

    public LanguageListerBeolingus(Context context) {
        super();

        addLanguage((new Locale("eng")).getDisplayLanguage() , "en-de");
        addLanguage((new Locale("deu")).getDisplayLanguage(), "deen");
        addLanguage((new Locale("spa")).getDisplayLanguage(), "es-de");
        // Seems to have no pronunciation yet
        // addLanguage(context.getString(R.string.multimedia_editor_languages_portuguese), "pt-de");
    }

}
