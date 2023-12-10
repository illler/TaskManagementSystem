package com.example.taskmanagementsystem.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    EXECUTOR_READ("executor:read");


    @Getter
    private final String permission;

}
