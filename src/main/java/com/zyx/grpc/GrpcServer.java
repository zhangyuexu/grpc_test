package com.zyx.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GrpcServer {

    private Server server;

    private void start() throws IOException {
        //初始化server对象
        this.server = ServerBuilder.forPort(8899).addService(new StudentServiceImpl()).build().start();

        System.out.println("server started!");

        //回调钩子
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("关闭JVM");
            //关闭jvm之前要做到事，即关闭jvm之前关闭socket
            GrpcServer.this.stop();
        })
        );
        System.out.println("执行到这里");

    }

    private void stop(){
        if(null != this.server){
            this.server.shutdown();
        }
    }

    /**
     * grpc跟thrift有点不一样，thrift是服务端启动之后就阻塞住了,一直等待客户端来连接，grpc不是这样，必须想办法让它阻塞住，不能让它启动之后就退出了，
     * grpc本身的server对象提供了这样的一个方法，叫做awaitTermination
     */
    private void awaitTermination() throws InterruptedException {
        if(null != server){
            this.server.awaitTermination();
            //this.server.awaitTermination(3000, TimeUnit.MILLISECONDS);
        }
    }

    public static void main(String[] args) throws IOException,InterruptedException{
        GrpcServer server = new GrpcServer();

        server.start();
        server.awaitTermination();

    }


}
