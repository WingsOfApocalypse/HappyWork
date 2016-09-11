package com.happy.common.util.memcache;

import net.spy.memcached.*;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.transcoders.Transcoder;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Derek on 16/9/11.
 */
public class SpyMemcacheManager {

    private static final int        DEFAULT_TIMEOUT  = 5;

    private static final TimeUnit   DEFAULT_TIMEUNIT = TimeUnit.SECONDS;

    private List<SpyMemcacheServer> servers;

    private MemcachedClient         memClient;

    public SpyMemcacheManager(List<SpyMemcacheServer> servers) {
        this.servers = servers;
    }

    public void connect() throws IOException {
        if (memClient != null) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (SpyMemcacheServer server : servers) {
            sb.append(server.toString()).append(" ");
        }
        if (StringUtils.isNotBlank(servers.get(0).getUsername())
            && StringUtils.isNotBlank(servers.get(0).getPassword())) {
            // 通过用户名密码访问
            AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" },
                                                   new PlainCallbackHandler(servers.get(0).getUsername(),
                                                                            servers.get(0).getPassword()));
            memClient = new MemcachedClient(
                                            new ConnectionFactoryBuilder().setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                                                                          .setAuthDescriptor(ad)
                                                                          .setFailureMode(FailureMode.Cancel.Retry)
                                                                          .build(),
                                            AddrUtil.getAddresses(sb.toString()));
        } else {
            memClient = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(sb.toString()));
        }
    }

    public void disconnect() {
        if (memClient == null) {
            return;
        }
        memClient.shutdown();
    }

    public void addObserver(ConnectionObserver obs) {
        memClient.addObserver(obs);
    }

    public void removeObserver(ConnectionObserver obs) {
        memClient.removeObserver(obs);
    }

    // ---- Basic Operation Start ----//
    public boolean set(String key, Object value, int expire) {
        Future<Boolean> future = memClient.set(key, expire, value);
        return getBooleanValue(future);
    }

    public Object get(String key) {
        return memClient.get(key);
    }

    public Object asyncGet(String key) {
        Object obj = null;
        Future<Object> future = memClient.asyncGet(key);
        try {
            obj = future.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
        } catch (Exception e) {
            future.cancel(false);
        }
        return obj;
    }

    public boolean add(String key, Object value, int expire) {
        Future<Boolean> future = memClient.set(key, expire, value);
        return getBooleanValue(future);
    }

    public boolean replace(String key, Object value, int expire) {
        Future<Boolean> future = memClient.replace(key, expire, value);
        return getBooleanValue(future);
    }

    public boolean delete(String key) {
        Future<Boolean> future = memClient.delete(key);
        return getBooleanValue(future);
    }

    public boolean flush() {
        Future<Boolean> future = memClient.flush();
        return getBooleanValue(future);
    }

    public Map<String, Object> getMulti(Collection<String> keys) {
        return memClient.getBulk(keys);
    }

    public Map<String, Object> getMulti(String[] keys) {
        return memClient.getBulk(keys);
    }

    public Map<String, Object> asyncGetMulti(Collection<String> keys) {
        Map<String, Object> map = null;
        Future<Map<String, Object>> future = memClient.asyncGetBulk(keys);
        try {
            map = future.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
        } catch (Exception e) {
            future.cancel(false);
        }
        return map;
    }

    public Map<String, Object> asyncGetMulti(String[] keys) {
        Map<String, Object> map = null;
        Future<Map<String, Object>> future = memClient.asyncGetBulk(keys);
        try {
            map = future.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
        } catch (Exception e) {
            future.cancel(false);
        }
        return map;
    }

    // ---- Basic Operation End ----//

    // ---- increment & decrement Start ----//
    public long increment(String key, int by, long defaultValue, int expire) {
        return memClient.incr(key, by, defaultValue, expire);
    }

    public long increment(String key, int by) {
        return memClient.incr(key, by);
    }

    public long decrement(String key, int by, long defalutValue, int expire) {
        return memClient.decr(key, by, defalutValue, expire);
    }

    public long decrement(String key, int by) {
        return memClient.decr(key, by);
    }

    public long asyncInvrement(String key, int by) {
        Future<Long> future = memClient.asyncIncr(key, by);
        return getLongValue(future);
    }

    public long asyncDecrement(String key, int by) {
        Future<Long> future = memClient.asyncDecr(key, by);
        return getLongValue(future);
    }

    // ---- increment & decrement End ----//

    public void printStats() throws IOException {
        printStats(null);
    }

    public void printStats(OutputStream stream) throws IOException {
        Map<SocketAddress, Map<String, String>> statMap = memClient.getStats();
        if (stream == null) {
            stream = System.out;
        }
        StringBuffer buf = new StringBuffer();
        Set<SocketAddress> addrSet = statMap.keySet();
        Iterator<SocketAddress> iter = addrSet.iterator();
        while (iter.hasNext()) {
            SocketAddress addr = iter.next();
            buf.append(addr.toString()).append("/n");
            Map<String, String> stat = statMap.get(addr);
            Set<String> keys = stat.keySet();
            Iterator<String> keyIter = keys.iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                String value = stat.get(key);
                buf.append("  key=" + key + ";value=" + "/n");
            }
            buf.append("/n");
        }
        stream.write(buf.toString().getBytes());
        stream.flush();
    }

    public List<String> getAllKeys() {
        List<String> list = new ArrayList<String>();
        Map<SocketAddress, Map<String, String>> statMap = memClient.getStats("items");
        Set<SocketAddress> addrSet = statMap.keySet();
        Iterator<SocketAddress> iter = addrSet.iterator();
        while (iter.hasNext()) {
            SocketAddress addr = iter.next();
            Map<String, String> stat = statMap.get(addr);
            Set<String> keys = stat.keySet();
            Iterator<String> keyIter = keys.iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                if (key != null && key.contains("number")) {
                    String[] args = key.split(":");
                    String param = "cachedump " + args[1] + " " + stat.get(key);
                    Map<SocketAddress, Map<String, String>> itemKeyMap = memClient.getStats(param);
                    Set<SocketAddress> itemKeyAddrSet = itemKeyMap.keySet();
                    Iterator<SocketAddress> itemIter = itemKeyAddrSet.iterator();
                    while (itemIter.hasNext()) {
                        SocketAddress itemAddr = itemIter.next();
                        Map<String, String> itemStat = itemKeyMap.get(itemAddr);
                        Set<String> itemKeys = itemStat.keySet();
                        list.addAll(itemKeys);
                    }
                }
            }
        }
        return list;
    }

    public Transcoder<Object> getTranscoder() {
        return memClient.getTranscoder();
    }

    public Object atomGet(String key) {
        CASValue<Object> result = gets(key);
        return result == null ? null : result.getValue();
    }

    public CASResponse cas(String key, Object value) {
        CASValue<Object> casValue = gets(key);
        return memClient.cas(key, casValue.getCas(), value);
    }

    public void cas(String key, String value, int expire) {
        CASResponse response = null;
        if (!add(key, value, expire)) {
            do {
                CASValue<Object> casValue = gets(key);
                response = memClient.cas(key, casValue.getCas(), expire, casValue.getValue() + "," + value,
                                         getTranscoder());
            } while (!CASResponse.OK.equals(response));
        }
    }

    public void increment(String key, Integer value) {
        CASResponse response = null;
        do {
            CASValue<Object> casValue = gets(key);
            response = memClient.cas(key, casValue.getCas(), (Integer) casValue.getValue() + value);
        } while (!CASResponse.OK.equals(response));
    }

    public void increment(String key, Integer value, int expire) {
        CASResponse response = null;
        do {
            CASValue<Object> casValue = gets(key);
            response = memClient.cas(key, casValue.getCas(), (Integer) casValue.getValue() + value, getTranscoder());
        } while (!CASResponse.OK.equals(response));
    }

    public void decrement(String key, Integer value) {
        CASResponse response = null;
        do {
            CASValue<Object> casValue = gets(key);
            response = memClient.cas(key, casValue.getCas(), (Integer) casValue.getValue() - value);
        } while (!CASResponse.OK.equals(response));
    }

    public void decrement(String key, Integer value, int expire) {
        CASResponse response = null;
        do {
            CASValue<Object> casValue = gets(key);
            response = memClient.cas(key, casValue.getCas(), expire, (Integer) casValue.getValue() - value,
                                     getTranscoder());
        } while (!CASResponse.OK.equals(response));
    }

    private boolean getBooleanValue(Future<Boolean> future) {
        try {
            Boolean bool = future.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
            return bool.booleanValue();
        } catch (Exception e) {
            future.cancel(false);
            return false;
        }
    }

    private long getLongValue(Future<Long> future) {
        try {
            Long l = future.get(DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
            return l.longValue();
        } catch (Exception e) {
            future.cancel(false);
        }
        return -1;
    }

    private CASValue<Object> gets(String key) {
        return memClient.gets(key);
    }
}
