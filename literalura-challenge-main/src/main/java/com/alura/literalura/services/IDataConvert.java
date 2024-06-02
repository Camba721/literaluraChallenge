package com.alura.literalura.services;

public interface IDataConvert {
    <T> T getData(String json, Class<T> tClass);
}
