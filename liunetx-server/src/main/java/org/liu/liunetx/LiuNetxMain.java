package org.liu.liunetx;

import org.apache.commons.cli.Options;

public class LiuNetxMain {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h",false,"Help");
        options.addOption("port",true,"LiuNetx server port");
        options.addOption("password",true,"LiuNetx server password");
        
    }
}
