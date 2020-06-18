package com.jdxl.common.utils;

//import com.caucho.hessian.io.Hessian2Input;
//import com.caucho.hessian.io.Hessian2Output;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * Created by kyle on 2017/12/9.
 */
public class SerializeUtil {
    private static final Log log = LogFactory.getLog(SerializeUtil.class);

    /**
     * 基于Java实现序列化和反序列化
     *
     * @param objContent
     * @return
     * @throws IOException
     */
    public static byte[] javaSerialize(final Object objContent) throws IOException {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream output = null;
        try {
            baos = new ByteArrayOutputStream(1024);
            output = new ObjectOutputStream(baos);
            output.writeObject(objContent);
        } catch (final IOException ex) {
            log.warn("IOException while JavaSerialize final:", ex);
            throw ex;
        } finally {
            if (output != null) {
                try {
                    output.close();
                    if (baos != null) {
                        baos.close();
                    }
                } catch (final IOException ex) {
                    log.warn("IOException while JavaSerialize final:", ex);
                }
            }
        }
        return baos != null ? baos.toByteArray() : null;
    }

    public static Object javaDeserialize(final byte[] objContent) throws IOException {
        Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(objContent);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (final IOException ex) {
            log.warn("IOException while JavaDeserialize:", ex);
            throw ex;
        } catch (final ClassNotFoundException ex) {
            log.warn("ClassNotFoundException while JavaDeserialize:", ex);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                    bais.close();
                } catch (final IOException ex) {
                    log.warn("IOException while JavaDeserialize final:", ex);
                }
            }
        }
        return obj;
    }

    /**
     * Kryo serialize
     *
     * @param source
     * @return
     * @throws IOException
     */
    public static byte[] KryoSerialize(Object source) throws IOException {

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            Kryo kryo = new Kryo();
            Output output = new Output(bos);
            kryo.writeObject(output, source);
            //byte[] outByteArray = output.toBytes();
            output.flush();
            output.close();
            return bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                log.warn("IOException while kryoWriteObject final:", e);
            }
        }
    }

    /**
     * Kryo deserialize
     *
     * @param bytes,Class
     * @return
     * @throws IOException
     */
    public static <T> T KryoDeserialize(byte[] bytes, Class<T> type) throws IOException {
        try {
            if (bytes == null) {
                return null;
            }
            Kryo kryo = new Kryo();
            Input input = new Input(bytes);
            T obj = kryo.readObject(input, type);
            input.close();
            return obj;
        } catch (Exception e) {
            log.warn("Exception while kryoReadObject final:", e);
            return null;
        }

    }

    /**
     * hessian2 deserialize
     *
     * @param bytes
     * @return
     * @throws IOException
     */
//    public static Object hessian2Deserialize(byte[] bytes) throws IOException {
//        if (bytes == null) {
//            return null;
//        }
//        ByteArrayInputStream bin = null;
//        try {
//            bin = new ByteArrayInputStream(bytes);
//            Hessian2Input in = new Hessian2Input(bin);
//            // in.startMessage();
//            Object obj = in.readObject();
//            // in.completeMessage();
//            in.close();
//            return obj;
//        } catch (IOException e) {
//            log.warn("IOException while hessianReadObject:", e);
//            throw e;
//        } finally {
//            try {
//                bin.close();
//            } catch (IOException e) {
//                log.warn("IOException while hessianReadObject final:", e);
//            }
//        }
//    }

    /**
     * hessian2 serialize
     *
     * @param source
     * @return
     * @throws IOException
     */
//    public static byte[] hessian2Serialize(Object source) throws IOException {
//        ByteArrayOutputStream bos = null;
//        try {
//            bos = new ByteArrayOutputStream();
//            Hessian2Output out = new Hessian2Output(bos);
//            // out.startMessage();
//            out.writeObject(source);
//            // out.completeMessage();
//            out.flush();
//            out.close();
//            return bos.toByteArray();
//        } catch (IOException e) {
//            log.warn("IOException while hessianWriteObject:", e);
//            throw e;
//        } finally {
//            try {
//                bos.close();
//            } catch (IOException e) {
//                log.warn("IOException while hessianWriteObject final:", e);
//            }
//        }
//    }

    public static void main(String[] args) throws IOException {
        System.out.println("start");
        byte[] ser = KryoSerialize(1L);
        System.out.println("KryoSerialize=====" + new String(ser));
        System.out.println("KryoDeserialize=====" + KryoDeserialize(ser, Long.class));
    }

}
