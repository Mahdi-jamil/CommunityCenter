package com.devesta.blogify.exception.exceptions;

public class FileNotFoundException extends RuntimeException{
     public FileNotFoundException(String msg){
         super(msg);
     }
     public FileNotFoundException(){
         super("file not found");
     }
}
