/*
 * 				Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2013 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.view;

import org.mariotaku.twidere.Constants;
import org.mariotaku.twidere.view.iface.IExtendedView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;

public class ProfileBannerImageView extends ClickableImageView implements IExtendedView, Constants {

	private static final int[] COLORS = new int[] { 0xFFFFFFFF, 0x00FFFFFF };
	private static final int[] COLORS_REVERSED = new int[] { 0x00FFFFFF, 0xFFFFFFFF };
	private static final float[] POSITIONS = new float[] { 0.0f, 1.0f };
	private static final PorterDuffXfermode DST_IN = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
	private final Paint mPaint = new Paint();

	private LinearGradient mShader;
	private OnSizeChangedListener mOnSizeChangedListener;

	public ProfileBannerImageView(final Context context) {
		this(context, null);
	}

	public ProfileBannerImageView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProfileBannerImageView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		ViewCompat.setLayerType(this, LAYER_TYPE_SOFTWARE, null);
		final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		;
		final boolean is_dark_theme = prefs.getBoolean(PREFERENCE_KEY_DARK_THEME, false);
		COLORS_REVERSED[1] = is_dark_theme ? 0xFF000000 : 0xFFFFFFFF;
	}

	@Override
	public final void setOnSizeChangedListener(final OnSizeChangedListener listener) {
		mOnSizeChangedListener = listener;
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		final int width = getWidth(), height = getHeight();
		if (mShader == null) return;
		super.onDraw(canvas);
		mPaint.setShader(mShader);
		if (supportXfermode()) {
			mPaint.setXfermode(DST_IN);
		}
		canvas.drawRect(0, 0, width, height, mPaint);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		final int width = MeasureSpec.getSize(widthMeasureSpec), height = width / 2;
		setMeasuredDimension(width, height);
		if (width > 0) {
			final int[] colors = supportXfermode() ? COLORS : COLORS_REVERSED;
			mShader = new LinearGradient(width / 2, 0, width / 2, height, colors, POSITIONS, Shader.TileMode.CLAMP);
		}
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}

	@Override
	protected final void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mOnSizeChangedListener != null) {
			mOnSizeChangedListener.onSizeChanged(this, w, h, oldw, oldh);
		}
	}

	private static boolean supportXfermode() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}
}
