package com.api.spaceexplorer.controller.exceptions;

public class ExplorerException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    public ExplorerException(String msg){
        super(msg);
    }
}
