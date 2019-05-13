package com.zyx.grpc;

import com.zyx.proto.*;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

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


    @Override
    public StreamObserver<StudentRequest> getStudentsWrapperByAges(StreamObserver<StudentResponseList> responseObserver) {

        //这个方法getStudentsWrapperByAges是通过回调实现的，下面这个return返回的方法全都是客户端的请求数据
        return new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest value) {
                System.out.println("value: "+value.getAge());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            //onCompleted表示当客户端全部把数据流发送完毕之后，服务端要把结果返回给客户端
            @Override
            public void onCompleted() {

                StudentResponse studentResponse = StudentResponse.newBuilder().setName("zhangsan").setAge(20).setCity("beijing").build();
                StudentResponse studentResponse2 = StudentResponse.newBuilder().setName("李四").setAge(20).setCity("上海").build();

                StudentResponseList studentResponseList = StudentResponseList.newBuilder().addStudentResponse(studentResponse).
                        addStudentResponse(studentResponse2).build();

                //服务器端要把最终的结果返回给客户端
                responseObserver.onNext(studentResponseList);
                responseObserver.onCompleted();
            }
        };
    }


    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            @Override
            public void onNext(StreamRequest value) {
                System.out.println(value.getRequestInfo());
                //因为是双向的数据传递，所以向客户端返回数据（即传过来一个我返回一个）
                responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                //客户端传输完毕数据后把流关闭了，那服务端也把流关闭
                responseObserver.onCompleted();
            }
        };
    }
}
