/*
 *				Twidere - Twitter client for Android
 * 
 * Copyright (C) 2012 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.loader;

import static org.mariotaku.twidere.util.Utils.getTwitterInstance;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class SavedSearchesLoader extends AsyncTaskLoader<ResponseList<SavedSearch>> {

	private final Twitter twitter;

	public SavedSearchesLoader(final Context context, final long account_id) {
		super(context);
		twitter = getTwitterInstance(context, account_id, false);
	}

	@Override
	public ResponseList<SavedSearch> loadInBackground() {
		if (twitter == null) return null;
		try {
			return twitter.getSavedSearches();
		} catch (final TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onStartLoading() {
		forceLoad();
	}

}
