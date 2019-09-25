package com.ichi2yiji.anki.features.splash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaojiyiji.yiji.R;

public class SplashFragment extends Fragment {
	private static final String KEY_CONTENT = "SplashFragment:Position";
	protected static final int[] CONTENT = new int[] {
			R.drawable.welcome_pager_1,
			R.drawable.welcome_pager_2,
			R.drawable.welcome_pager_3,
			R.drawable.welcome_pager_4 };

	private int mPosition;

	public static SplashFragment newInstance(int position) {
		SplashFragment fragment = new SplashFragment();
		fragment.mPosition = position;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mPosition = savedInstanceState.getInt(KEY_CONTENT);
		}
		View root = inflater.inflate(R.layout.fragment_splash_layout, container, false);
		root.setBackgroundResource(CONTENT[mPosition]);
		return root;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_CONTENT, mPosition);
	}
}
