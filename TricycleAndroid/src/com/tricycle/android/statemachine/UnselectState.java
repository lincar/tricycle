package com.tricycle.android.statemachine;

import android.util.Log;

public class UnselectState implements State {
	
	private static final String TAG = "UnselectState";
	
	UploadMachine mUploadMachine;
	
	public UnselectState(UploadMachine uploadMachine) {
		mUploadMachine = uploadMachine;
	}

	@Override
	public void selectPhoto() {
		Log.i(TAG, "selected a photo");
		mUploadMachine.setState(UploadMachine.SELECTED_PHOTO);
	}

	@Override
	public void deletePhoto() {
		Log.w(TAG, "hadn't selected a photo");
	}

	@Override
	public void clickButton() {
		Log.w(TAG, "hadn't selected a photo");
	}

	@Override
	public void uploadPhoto() {
		Log.w(TAG, "hadn't selected a photo");
	}

}
