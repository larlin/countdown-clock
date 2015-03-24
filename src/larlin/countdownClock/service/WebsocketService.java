package larlin.countdownClock.service;

import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.WebSocketConnectCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.WebSocket.StringCallback;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WebsocketService extends Service {
	private boolean running = true;
	private WebSocket socket;
	
	private static final String TAG = "WebsocketService";
	
	@Override
	public void onCreate(){
		AsyncHttpClient.getDefaultInstance().websocket("ws://ksc.nazar.so:8080/ws", null,
													   new WebSocketConnectCallback() {
				@Override
				public void onCompleted(Exception ex, WebSocket webSocket) {
					socket = webSocket;
			        webSocket.setStringCallback(new StringCallback() {
			            public void onStringAvailable(String s) {
			                Log.d(TAG, "We got a string!");
			            }
			        });
			        webSocket.setDataCallback(new DataCallback() {
						@Override
						public void onDataAvailable(DataEmitter emitter,
								ByteBufferList byteBufferList) {
			            	Log.d(TAG, "We got some binary data!?");
			                byteBufferList.recycle();
							
						}
			        });
			        webSocket.setEndCallback(new CompletedCallback() {
						
						@Override
						public void onCompleted(Exception ex) {
							Log.d(TAG, "socket ended!");
						}
					});
			        
			        Log.d(TAG, "Connection completed and callbacks set!");
			        
			        new Thread(new Runnable() {
						
						@Override
						public void run() {
							while(running){
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(socket != null){
									Log.d(TAG, "Pausing socket");
									socket.pause();
								}
								try {
									Thread.sleep(20000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(socket != null && socket.isPaused()){
									Log.d(TAG, "Unpausing socket");
									socket.resume();
								}
							}
						}
					}).start();
				}
			}
		);
		
	}
		
	@Override
    public void onDestroy() {
		running = false;
		socket.end();
		socket.close();
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
