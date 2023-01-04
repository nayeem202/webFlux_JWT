package com.example.webflux_jwt.reqResonseModel;

public interface IReqResModel<T>{
    T getData();//return the data of the message
    String getMessage(); //Return a message String if there has been any error. on 404 error this needs to return not found
}
