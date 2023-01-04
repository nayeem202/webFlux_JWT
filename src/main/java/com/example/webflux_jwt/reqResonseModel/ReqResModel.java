package com.example.webflux_jwt.reqResonseModel;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReqResModel<T> implements IReqResModel{
    private T data;
    private  String message;


    @Override
    public Object getData() {
        return this.data;
    }

    @Override
    public String getMessage() {
        return this.message;
    }



}
