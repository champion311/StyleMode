package shinerich.com.stylemodel.network;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Administrator on 2016/12/8.
 */
public class WebSocketManager {

    private OkHttpClient okHttpClient;


    //http://10.17.1.51:8899
    private WebSocketCall webSocketCall;

    public WebSocketManager() {
        okHttpClient = OkHttpManager.getOkHttpClient();
    }


    public void buildRequest(final String url) {

        OkHttpClient client = new OkHttpClient.Builder().build();

        final Request request = new Request.Builder().url(url).build();

        webSocketCall = WebSocketCall.create(client, request);
        webSocketCall.enqueue(new WebSocketListener() {
            private final ExecutorService sendExexcutor = Executors.newSingleThreadExecutor();

            private WebSocket webSocket;


            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d("tag", "open");
                this.webSocket = webSocket;

            }

            @Override
            public void onFailure(IOException e, Response response) {
                Log.d("tag", "Failure");
            }

            @Override
            public void onMessage(ResponseBody message) throws IOException {
                Log.d("tag", "message");
                final RequestBody requestBody;
                if (message.contentType() == WebSocket.TEXT) {
                    requestBody = RequestBody.create(WebSocket.TEXT, "2333");
                } else {
                    BufferedSource source = message.source();
                    Log.d("tag", "message:" + source.readByteString());
                    requestBody = RequestBody.create(WebSocket.BINARY, source.readByteString());

                }
                message.source().close();
                sendExexcutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            webSocket.sendMessage(requestBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

            @Override
            public void onPong(Buffer payload) {
                Log.d("tag", "pong");
            }

            @Override
            public void onClose(int code, String reason) {
                Log.d("tag", "close");
                sendExexcutor.shutdown();
            }
        });
    }

}

