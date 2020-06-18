package com.jdxl.shardingjdbc.keygen;

import com.jdxl.common.utils.IPUtils;
import io.shardingjdbc.core.keygen.DefaultKeyGenerator;
import io.shardingjdbc.core.keygen.KeyGenerator;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 * 根据IP生成Snowflake算法的WorkerId
 *
 * <pre>
 * Snowflake算法的workerId最大限制是2^10，我们生成的workerId只要满足小于最大workerId即可。
 * 1.针对IPV4:
 *   IP最大 255.255.255.255。而（255+255+255+255) < 1024。
 *   因此采用IP段数值相加即可生成唯一的workerId，不受IP位限制。
 *   私网规则（172.16.0.0~172.31.0.0）
 * 2.针对IPV6: 还没有更好的思路，可以暂时使用IPV4
 *   IP最大ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
 *   为了保证相加生成出的workerId < 1024,思路是将每个bit位的后6位相加。这样在一定程度上也可以满足workerId不重复的问题。
 * </pre>
 * 使用这种IP生成workerId的方法,必须保证IP段相加不能重复
 *
 * @author moccanism
 */
@Slf4j
public class IPSectionKeyGenerator implements KeyGenerator {

    private final DefaultKeyGenerator kg = new DefaultKeyGenerator();

    static {
        initWorkerId();
    }

    static void initWorkerId() {
        InetAddress address  = IPUtils.getInetAddress();
        if (null == address) {
            throw new IllegalStateException("Cannot get InetAddress, please check your network!");
        }
//        try {
//            address = InetAddress.getLocalHost();
//        } catch (final UnknownHostException e) {
//            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
//        }
        byte[] ipAddressByteArray = address.getAddress();
        long workerId = 0L;
        // IPV4
        if (ipAddressByteArray.length == 4) {
            for (byte byteNum : ipAddressByteArray) {
                workerId += (byteNum & 0xFF);
            }
            // IPV6
        } else if (ipAddressByteArray.length == 16) {
            for (byte byteNum : ipAddressByteArray) {
                workerId += (byteNum & 0B111111);
            }
        } else {
            throw new IllegalStateException("Bad LocalHost InetAddress, please check your network!");
        }
        log.info("set KeyGenerator workerId " + workerId);
        DefaultKeyGenerator.setWorkerId(workerId);
    }

//    /**
//     * 获取本地IP地址
//     *
//     * @throws SocketException
//     */
//    private static InetAddress getLocalIP() throws UnknownHostException {
//        if (isWindowsOS()) {
//            return InetAddress.getLocalHost();
//        } else if (isDarwin()) {
//            return getLinuxLocalIp();
//        } else {
//            return getLinuxLocalIp();
//        }
//    }
//
//    /**
//     * 判断操作系统是否是Windows
//     *
//     * @return 是否是Windows
//     */
//    private static boolean isWindowsOS() {
//        return System.getProperty("os.name").toLowerCase().contains("windows");
//    }
//
//    /**
//     * 判断操作系统是否是MacOS
//     *
//     * @return 是否是MacOS
//     */
//    private static boolean isDarwin() {
//        return System.getProperty("os.name").toLowerCase().contains("darwin");
//    }
//
//    /**
//     * 获取Linux下的IP地址
//     *
//     * @return IP地址
//     */
//    private static InetAddress getLinuxLocalIp() {
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
//                NetworkInterface intf = en.nextElement();
//                String name = intf.getName();
//                if (!name.contains("docker") && !name.contains("lo")) {
//                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
//                        InetAddress inetAddress = enumIpAddr.nextElement();
//                        if (!inetAddress.isLoopbackAddress()) {
//                            String ipaddress = inetAddress.getHostAddress().toString();
//                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
//                                return inetAddress;
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
//        }
//    }


    /**
     * Generate a key.
     *
     * @return generated key
     */
    @Override
    public Number generateKey() {
        return kg.generateKey();
    }
}
