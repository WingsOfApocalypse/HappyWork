package com.happy.common.util.memcache;

/**
 * Created by Derek on 16/9/11.
 */
public class SpyMemcacheServer {

    private String ip;

    private int port;

    private String username;

    private String password;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if(port < 0 && port > 65525){
            throw new RuntimeException("Port number must be between 0 to 65535");
        }
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return getIp() + ":" + getPort();
    }

}
