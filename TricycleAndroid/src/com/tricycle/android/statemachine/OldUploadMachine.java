package com.tricycle.android.statemachine;

import android.util.Log;

public class OldUploadMachine {
	
	final static String TAG = "UploadMachine";

	final static int UNSELECTED_PHOTO = 0;
	final static int SELECTED_PHOTO = 1;
	final static int UPLOADING_PHOTO = 2;
	
	int state = UPLOADING_PHOTO;
	
	public void selectPhoto() {
		switch(state) {
		case UNSELECTED_PHOTO:
			Log.i(TAG, "selected a photo");
			state = SELECTED_PHOTO;
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
		switch(state) {
		case UNSELECTED_PHOTO:
			Log.w(TAG, "hadn't selected a photo");
			break;
		case SELECTED_PHOTO:
			Log.i(TAG, "deleted a photo");
			state = UNSELECTED_PHOTO;
			break;
		case UPLOADING_PHOTO:
			Log.w(TAG, "uploading photos,please wait");
			break;
	    default:
	    	break;
		}
	}
	public void clickButton() {
		switch(state) {
		case UNSELECTED_PHOTO:
			Log.w(TAG, "hadn't selected a photo");
			break;
		case SELECTED_PHOTO:
			Log.i(TAG, "clicked button");
			state = UPLOADING_PHOTO;
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
		switch(state) {
		case UNSELECTED_PHOTO:
			Log.w(TAG, "hadn't selected a photo");
			break;
		case SELECTED_PHOTO:
			Log.w(TAG, "hadn't clicked button");
			break;
		case UPLOADING_PHOTO:
			Log.i(TAG, "uploaded photos");
			state = UNSELECTED_PHOTO;
			break;
	    default:
	    	break;
		}
	}
}
