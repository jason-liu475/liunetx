package org.liu.liunetx;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.liu.liunetx.server.LiuNetxServer;

@Slf4j
public class LiuNetxMain {
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("h",false,"Help");
        options.addOption("port",true,"LiuNetx server port");
        options.addOption("password",true,"LiuNetx server password");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if(cmd.hasOption("h")){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("options",options);
        }else{
            int port = Integer.parseInt(cmd.getOptionValue("port","6000"));
            String password = cmd.getOptionValue("password");
            LiuNetxServer liuNetxServer = new LiuNetxServer();
            liuNetxServer.start(port,password);
            log.info("LiuNetx server start on port:{}",port);
        }
    }
}
