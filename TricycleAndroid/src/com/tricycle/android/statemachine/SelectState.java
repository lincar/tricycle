package com.tricycle.android.statemachine;

import android.util.Log;

public class SelectState implements State {
	
	private static final String TAG = "SelectState";
	
	UploadMachine mUploadMachine;
	
	public SelectState(UploadMachine uploadMachine) {
		mUploadMachine = uploadMachine;
	}

	@Override
	public void selectPhoto() {
		Log.w(TAG, "already selected a photo");
	}

	@Override
	public void deletePhoto() {
		Log.i(TAG, "deleted a photo");
		mUploadMachine.setState(UploadMachine.UNSELECTED_PHOTO);
	}

	@Override
	public void clickButton() {
		Log.i(TAG, "clicked button");
		mUploadMachine.setState(UploadMachine.UPLOADING_PHOTO);
		mUploadMachine.uploadPhoto();
	}

	@Override
	public void uploadPhoto() {
		Log.w(TAG, "hadn't clicked button");
	}

}
