package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    private final BitSet bitSet;
    private final int size;
    private final String[] hashAlgs; // Array of hash function names

    public BloomFilter(int size, String... hashAlgs) {
        this.size = size;
        this.bitSet = new BitSet(size);
        this.hashAlgs = hashAlgs;
    }

    public void add(String word) {
        for (String alg : hashAlgs) {
        	int index = getHash(word, alg) % size;
        	if (index < 0) index += size;
            //
            //System.out.println(index);
            
            bitSet.set(index);
        }
    }


    public boolean contains(String word) {
        for (String alg : hashAlgs) {
            int hash = getHash(word, alg) % size;
            if (hash < 0) hash += size; // Ensure the index is positive
            if (!bitSet.get(hash)) {
                return false; // If any bit is not set, the word is definitely not in the set
            }
        }
        return true; // Possible membership (with potential for false positive)
    }

    private int getHash(String word, String algorithm) {
    	//
    	//System.out.println(word+" "+algorithm);
    	
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //md.update(word.getBytes());
            byte[] digest = md.digest(word.getBytes());
            BigInteger hash = new BigInteger(1, digest);
            //return hash.mod(BigInteger.valueOf(size)).intValue();
            return hash.intValue();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not supported: " + algorithm);
        }
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }


}

/*package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    private final BitSet bitSet;
    private final int size; // Maximum size
    private final String[] hashAlgs;

    public BloomFilter(int size, String... hashAlgs) {
        this.size = size;
        this.bitSet = new BitSet(); // Start with an empty BitSet
        this.hashAlgs = hashAlgs;
    }

    public void add(String word) {
        for (String alg : hashAlgs) {
            int index = getHash(word, alg) % size;
            if (index < 0) index += size; // Ensure index is positive
            
            bitSet.set(index); // BitSet grows as needed, but logical size is bounded by 'size'
        }
    }

    public boolean contains(String word) {
        for (String alg : hashAlgs) {
            int hash = getHash(word, alg) % size;
            if (hash < 0) hash += size;
            if (!bitSet.get(hash)) {
                return false; // Word definitely not present
            }
        }
        return true; // Word possibly present
    }

    private int getHash(String word, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(word.getBytes());
            BigInteger hash = new BigInteger(1, digest);
            return hash.intValue();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not supported: " + algorithm);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitSet.size(); i++) { // Limit the output to 'size', as logical size
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }
}*/

