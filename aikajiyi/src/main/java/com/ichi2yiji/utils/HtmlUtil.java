/****************************************************************************************
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

package com.ichi2yiji.utils;

import android.text.Html;

import java.text.Normalizer;

/**
 * Static helper containing functions to deal with HTML text.
 */
public class HtmlUtil {

    private HtmlUtil() {
    }


    /**
     * Unescapes all sequences within the given string of text, interpreting them as HTML escaped characters.
     * <p/>
     * Not that this code strips any HTML tags untouched, so if the text contains any HTML tags, they will be ignored.
     *
     * @param htmlText the text to convert
     * @return the unescaped text
     */
    public static String unescape(String htmlText) {
        return Html.fromHtml(htmlText).toString();
    }


    /*
     *  Return the input string in the Unicode normalized form. This helps with text comparisons, for example a ü
     *  stored as u plus the dots but typed as a single character compare as the same.
     *
     * @param txt Text to be normalized
     * @return The input text in its NFC normalized form form.
    */
    public static String nfcNormalized(String txt) {
        if (!Normalizer.isNormalized(txt, Normalizer.Form.NFC)) {
            return Normalizer.normalize(txt, Normalizer.Form.NFC);
        }
        return txt;
    }

}
