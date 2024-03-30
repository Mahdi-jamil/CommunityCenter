package com.devesta.blogify.exception.exceptions.notfound;

public class FileNotFoundException extends NotFoundException{
     public FileNotFoundException(String msg){
         super(msg);
     }
     public FileNotFoundException(){
         super("file not found");
     }
}
