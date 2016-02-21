package com.tricycle.android.rxjava;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RxListView extends Activity implements OnItemClickListener {
	private static final String TAG = "RxListView";
	
	private ListView mListView = null;
	private Handler backgroundHandler;
	public Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		mListView = new ListView(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getArray());
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		setContentView(mListView);
		BackgroundThread backgroundThread = new BackgroundThread();
		backgroundThread.start();
		backgroundHandler = new Handler(backgroundThread.getLooper());
	}
	
	private List<String> getArray() {
		List<String> list = new ArrayList<String>();
		list.add("jjjjj");
		list.add("kkkkk");
		return list;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch(arg2) {
		case 0:
			onFirstClicked();
			break;
		case 1:
			onSecondClicked();
		default:
			break;
		}
	}
	
	private void onSecondClicked() {
		
	}
	
	private void onFirstClicked() {
		firstObservable
				.subscribeOn(HandlerScheduler.from(backgroundHandler))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(firstSubscriber);
	}
	
	Observable<String> firstObservable = Observable.create(
			new Observable.OnSubscribe<String>() {
				@Override
				public void call(Subscriber<? super String> sub) {
					try {
						sub.onNext("first");
						Thread.sleep(TimeUnit.SECONDS.toMillis(1));
						sub.onNext("second");
						Thread.sleep(TimeUnit.SECONDS.toMillis(2));
						sub.onNext("third");
//						Thread.sleep(TimeUnit.SECONDS.toMillis(3));
//						sub.onCompleted();
					}catch (Exception e) {
						sub.onError(e);
					}
					
				}
			});
	
	Subscriber<String> firstSubscriber = new Subscriber<String>() {
		@Override
		public void onNext(String s) {
			TextView tv = (TextView)mListView.getChildAt(0);
            tv.setText(s);
		}

		@Override
		public void onCompleted() {
			TextView tv = (TextView)mListView.getChildAt(0);
            tv.setText("completed");
		}

		@Override
		public void onError(Throwable arg0) {
			TextView tv = (TextView)mListView.getChildAt(0);
            tv.setText("error");
		}
	};

    void onRunSchedulerExampleButtonClicked() {
        sampleObservable()
                // Run on a background thread
                .subscribeOn(HandlerScheduler.from(backgroundHandler))
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                    }

                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }

                    @Override public void onNext(String string) {
                        Log.d(TAG, "onNext(" + string + ")");
                        Toast.makeText(mContext, string, Toast.LENGTH_LONG).show();
                        TextView tv = (TextView)mListView.getChildAt(1);
                        tv.setText(string);
                    }
                });
    }

    static Observable<String> sampleObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override public Observable<String> call() {
                try {
                    // Do some long running operation
                    Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                } catch (InterruptedException e) {
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }

    static class BackgroundThread extends HandlerThread {
        BackgroundThread() {
            super("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
        }
    }


	

}
