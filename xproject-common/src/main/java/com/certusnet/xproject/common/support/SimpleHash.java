package com.certusnet.xproject.common.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.certusnet.xproject.common.util.Base64Utils;
import com.certusnet.xproject.common.util.ByteArrayUtils;
import com.certusnet.xproject.common.util.StringUtils;

public class SimpleHash {

	private static final int DEFAULT_ITERATIONS = 1;

	private static final String DEFAULT_CHARSET = "UTF-8";
	
    /**
     * The {@link java.security.MessageDigest MessageDigest} algorithm name to use when performing the hash.
     */
    private final String algorithmName;
    
    private byte[] salt;
    
    /**
     * Number of hash iterations to perform.  Defaults to 1 in the constructor.
     */
    private int iterations;
    
    /**
     * The hashed data
     */
    private byte[] bytes;
    
    /**
     * Cached value of the {@link #toHex() toHex()} call so multiple calls won't incur repeated overhead.
     */
    private transient String hexEncoded = null;

    /**
     * Cached value of the {@link #toBase64() toBase64()} call so multiple calls won't incur repeated overhead.
     */
    private transient String base64Encoded = null;

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = Math.max(DEFAULT_ITERATIONS, iterations);
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public SimpleHash(String algorithmName, String source, String salt, int hashIterations) {
		super();
		if (StringUtils.isEmpty(algorithmName)) {
            throw new NullPointerException("algorithmName argument cannot be null or empty.");
        }
        this.algorithmName = algorithmName;
        setIterations(hashIterations);
        byte[] sourceBytes = ByteArrayUtils.stringToByteArray(source, DEFAULT_CHARSET);
        byte[] saltBytes = null;
        if(salt != null){
        	saltBytes = ByteArrayUtils.stringToByteArray(salt, DEFAULT_CHARSET);
        	this.salt = saltBytes;
        }
        this.bytes = hash(sourceBytes, saltBytes, hashIterations);
	}
    
    protected MessageDigest getDigest(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            String msg = "No native '" + algorithmName + "' MessageDigest instance available on the current JVM.";
            throw new RuntimeException(msg, e);
        }
    }
	
	protected byte[] hash(byte[] bytes, byte[] salt, int hashIterations) {
        MessageDigest digest = getDigest(getAlgorithmName());
        if (salt != null) {
            digest.reset();
            digest.update(salt);
        }
        byte[] hashed = digest.digest(bytes);
        int iterations = hashIterations - DEFAULT_ITERATIONS; //already hashed once above
        //iterate remaining number:
        for (int i = 0; i < iterations; i++) {
            digest.reset();
            hashed = digest.digest(hashed);
        }
        return hashed;
    }
	
	public String toHex() {
        if (this.hexEncoded == null) {
            this.hexEncoded = ByteArrayUtils.byteArrayToHexString(bytes);
        }
        return this.hexEncoded;
    }

    public String toBase64() {
        if (this.base64Encoded == null) {
            this.base64Encoded = Base64Utils.encode(getBytes());
        }
        return this.base64Encoded;
    }

    public String toString() {
        return toHex();
    }
	
}
