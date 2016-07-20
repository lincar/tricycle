package com.tricycle.news.newsdetail;

import com.tricycle.news.R;

import android.R.color;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow.OnDismissListener;

public class CommentPopupWindow implements OnClickListener, OnDismissListener {

	Context mContext;
	OnCommentSetListener mlistener;
	Button mSubmitButton;
	EditText mCommentEditText;
	String mComment = "";
	PopupWindow popupWindow;
	public CommentPopupWindow(Context context, OnCommentSetListener listener) {
		mContext = context;
		mlistener = listener;
	}
	public void showPopupWindow(View view) {
		View contentView = LayoutInflater.from(mContext).inflate(R.layout.comment_popupwindow, null);
		popupWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		//popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(false);
		// 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());	
		//软键盘不会挡着popupwindow
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(this);
		popupWindow.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        mSubmitButton = (Button)contentView.findViewById(R.id.comment_popup_button);
        mSubmitButton.setOnClickListener(this);
        mCommentEditText = (EditText)contentView.findViewById(R.id.comment_popup_edittext);
        mCommentEditText.setText(mComment);
        View rootview = LayoutInflater.from(mContext).inflate(R.layout.news_activity, null);
        // 设置好参数之后再show
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.comment_popup_button:
			String comment = mCommentEditText.getText().toString();
			mlistener.onCommentSet(comment);
			mComment = "";
			mCommentEditText.setText("");
			popupWindow.dismiss();
			break;

		default:
			break;
		}
	}
	@Override
	public void onDismiss() {
		mComment = mCommentEditText.getText().toString();
	}
}
