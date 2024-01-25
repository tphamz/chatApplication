package com.example.api_server.persistence;

public class UserScope {
    public static interface External{}
    public static interface Internal extends External{}
    public static interface UserSummary{}
    public static interface UserDetail extends UserSummary{}
}
