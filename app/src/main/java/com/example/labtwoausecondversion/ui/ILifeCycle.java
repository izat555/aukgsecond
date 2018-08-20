package com.example.labtwoausecondversion.ui;

public interface ILifeCycle<T> {
    void bind(T view);
    void unbind();
}
