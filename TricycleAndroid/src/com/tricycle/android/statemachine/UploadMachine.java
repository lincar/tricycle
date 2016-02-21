package com.tricycle.android.statemachine;

import android.util.Log;

public class UploadMachine {
	
	private final static String TAG = "UploadMachine";

	public final static int UNSELECTED_PHOTO = 0;
	public final static int SELECTED_PHOTO = 1;
	public final static int UPLOADING_PHOTO = 2;
	
	int mState = UPLOADING_PHOTO;
	
	public void setState(int state) {
		mState = state;  
	}
	
	public void selectPhoto() {
		switch(mState) {
		case UNSELECTED_PHOTO:
			Log.i(TAG, "selected a photo");
			mState = SELECTED_PHOTO;
			break;
		case SELECTED_PHOTO:
			Log.w(TAG, "already selected a photo");
			break;
		case UPLOADING_PHOTO:
			Log.w(TAG, "uploading photos,please wait");
			break;
	    default:
	    	break;
		}
	}
	
	public void deletePhoto() {
		switch(mState) {
		case UNSELECTED_PHOTO:
			Log.w(TAG, "hadn't selected a photo");
			break;
		case SELECTED_PHOTO:
			Log.i(TAG, "deleted a photo");
			mState = UNSELECTED_PHOTO;
			break;
		case UPLOADING_PHOTO:
			Log.w(TAG, "uploading photos,please wait");
			break;
	    default:
	    	break;
		}
	}
	public void clickButton() {
		switch(mState) {
		case UNSELECTED_PHOTO:
			Log.w(TAG, "hadn't selected a photo");
			break;
		case SELECTED_PHOTO:
			Log.i(TAG, "clicked button");
			mState = UPLOADING_PHOTO;
			uploadPhoto();
			break;
		case UPLOADING_PHOTO:
			Log.w(TAG, "uploading photos,please wait");
			break;
	    default:
	    	break;
		}
	}
	
	public void uploadPhoto() {
		switch(mState) {
		case UNSELECTED_PHOTO:
			Log.w(TAG, "hadn't selected a photo");
			break;
		case SELECTED_PHOTO:
			Log.w(TAG, "hadn't clicked button");
			break;
		case UPLOADING_PHOTO:
			Log.i(TAG, "uploaded photos");
			mState = UNSELECTED_PHOTO;
			break;
	    default:
	    	break;
		}
	}
}
