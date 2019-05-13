package com.zyx.grpc;

import com.zyx.proto.*;
import io.grpc.stub.StreamObserver;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    @Override
    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("接收到客户端信息："+request.getUsername());

        //responseObserver有3个on方法，它们都是回调方法，就是在某个事件发生的时候会调用。
        //onNext标识接下来要做什么事情
        responseObserver.onNext(MyResponse.newBuilder().setRealname("张三").build());//这个表示服务端把结果构造完，并且返回给客户端
        //标识这个方法调用结束，它只能调用一次
        responseObserver.onCompleted();//这个表示告诉客户端，我执行完了



        //标识发生错误的时候调用
        //responseObserver.onError();
    }

    @Override
    public void getStudentsByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        System.out.println("接收到客户端信息："+request.getAge());

        responseObserver.onNext(StudentResponse.newBuilder().setName("张三").setAge(20).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("李四").setAge(30).setCity("上海").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("王五").setAge(40).setCity("广州").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("赵六").setAge(50).setCity("深圳").build());

        responseObserver.onCompleted();

    }
}
