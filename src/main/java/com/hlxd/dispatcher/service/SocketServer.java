package com.hlxd.dispatcher.service;

import cn.hutool.json.JSONUtil;
import com.hlxd.dispatcher.entity.AppInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Data
@Component
@NoArgsConstructor
public class SocketServer {

    private final int port = 9999;
    private boolean started;
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public static final List<Socket> socketList = new ArrayList<>();
    @Resource
    private DispatcherService dispatcherService;


    public void start() {
        log.info("port: {}", port);
        try {
            serverSocket = new ServerSocket(port);
            started = true;
            log.info("Socket服务已启动，占用端口： {}", serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("端口冲突,异常信息：{}", e);
            System.exit(0);
        }

        while (started) {
            try {
                Socket socket = serverSocket.accept();
                socketList.add(socket);
                Runnable runnable = () -> {
                    try {
//                        //接收客户端数据
//                        StringBuilder returnStr = onMessage(socket);
////                        //更新应用目录
////                        assert returnStr != null;
////                        List<AppInfo> appList = JSONUtil.toList(returnStr.toString(), AppInfo.class);
////                        boolean status = dispatcherService.registerOrUpdate(appList);
//                        log.info(returnStr.toString());
////                        //返回给客户端
////                        if (status) {
////                            sendMessage(socket, "success");
////                        } else {
////                            sendMessage(socket, "fail");
////                        }
//                        sendMessage(socket, returnStr.toString());

                        while(true) {
                            InputStream inputStream =  socket.getInputStream();
                            byte[] b = new byte[1024];
                            int len;
                            StringBuffer sb = new StringBuffer();
                            //一次交互完成后，while循环过来，在此阻塞，即监听
                            while ((len = inputStream.read(b)) != -1) {
                                sb.append(new String(b, 0, len));
                                //单次交互结束标识，跳出监听
                                if(new String(b, 0, len).contains("over")){
                                    break;
                                }
                            }
                            String content = sb.toString();
                            System.out.println("接收到客户端消息" + content.substring(0,content.length()-4));

//                            //往客户端发送数据
//                            long nowtime = (new Date()).getTime();
//                            socket.getOutputStream().write((nowtime+"over").getBytes("UTF-8"));
//                            socket.getOutputStream().flush();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        try {
                            socket.close();
                            socketList.clear();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //接收线程返回结果
                threadPool.submit(runnable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static StringBuilder onMessage(Socket socket) {
        byte[] bytes = new byte[1024];
        int len;
        try {
            // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
            InputStream inputStream = socket.getInputStream();
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
            }
            //此处，需要关闭服务器的输出流，但不能使用inputStream.close().
            socket.shutdownInput();
            return sb;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendMessage(Socket socket, String message) {
        try {
            //向客户端返回数据
            OutputStream outputStream = socket.getOutputStream();
            //首先需要计算得知消息的长度
            byte[] sendBytes = message.getBytes(StandardCharsets.UTF_8);
            //然后将消息的长度优先发送出去
//            outputStream.write(sendBytes.length >> 8);
//            outputStream.write(sendBytes.length);
            //然后将消息再次发送出去
            outputStream.write(sendBytes);
            outputStream.flush();
//            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
