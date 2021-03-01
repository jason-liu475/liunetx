package org.liu.liunetx;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.cli.*;
import org.liu.liunetx.client.LiuNetxClient;

import java.util.Objects;

public class LiuNetxCilentMain {
    static Log log = LogFactory.get();
    public static void main(String[] args) throws Exception{
        Options options = new Options();
        options.addOption("h",false,"Help");
        options.addOption("s_addr",true,"LiuNetx server address");
        options.addOption("s_port",true,"LiuNetx server port");
        options.addOption("password",true,"LiuNetx server password");
        options.addOption("p_addr",true,"Proxy server address");
        options.addOption("p_port",true,"Proxy server port");
        options.addOption("r_port",true,"Proxy server remote port");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if(cmd.hasOption("h")){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("options",options);
        }else{
            String serverHost = cmd.getOptionValue("s_addr");
            if(Objects.isNull(serverHost)){
                log.error("s_addr cannot be null");
                return;
            }
            String serverPort = cmd.getOptionValue("s_port");
            if(Objects.isNull(serverPort)){
                log.error("s_port cannot be null");
                return;
            }
            String password = cmd.getOptionValue("password");
            String proxyHost = cmd.getOptionValue("p_addr");
            if(Objects.isNull(proxyHost)){
                log.error("p_addr cannot be null");
                return;
            }
            String proxyPort = cmd.getOptionValue("p_port");
            if(Objects.isNull(proxyPort)){
                log.error("p_port cannot be null");
                return;
            }
            String remotePort = cmd.getOptionValue("r_port");
            if(Objects.isNull(remotePort)){
                log.error("r_port cannot be null");
                return;
            }
            LiuNetxClient client = new LiuNetxClient();
            client.connect(serverHost,Integer.parseInt(serverPort),password,Integer.parseInt(remotePort),proxyHost,Integer.parseInt(proxyPort));
        }
    }
}
