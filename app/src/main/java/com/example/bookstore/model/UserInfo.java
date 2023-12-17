package com.example.bookstore.model;

import com.example.bookstore.app_util.TypeLogin;

public class UserInfo {
   private int id;
   private String name;
   private String email;
   private String password;


   private TypeLogin typeLogin;

   public UserInfo(String name, String email, String password, TypeLogin typeLogin) {
      this.name = name;
      this.email = email;
      this.password = password;
      this.typeLogin = typeLogin;
   }

   public UserInfo(int id, String name, String email, String password, TypeLogin typeLogin) {
      this.id = id;
      this.name = name;
      this.email = email;
      this.password = password;
      this.typeLogin = typeLogin;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public TypeLogin getTypeLogin() {
      return typeLogin;
   }

   public void setTypeLogin(TypeLogin typeLogin) {
      this.typeLogin = typeLogin;
   }
}
