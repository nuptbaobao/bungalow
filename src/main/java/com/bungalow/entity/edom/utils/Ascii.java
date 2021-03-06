package com.bungalow.entity.edom.utils;

/**
 * Created by ChengXi on 2016/9/12.
 */
public final class Ascii {
    /*
     * Character translation tables.
     */

    private static final byte[] toUpper = new byte[256];
    private static final byte[] toLower = new byte[256];

    /*
     * Character type tables.
     */

    private static final boolean[] isAlpha = new boolean[256];
    private static final boolean[] isUpper = new boolean[256];
    private static final boolean[] isLower = new boolean[256];
    private static final boolean[] isWhite = new boolean[256];
    private static final boolean[] isDigit = new boolean[256];

    /*
     * Initialize character translation and type tables.
     */

    static {
        for (int i = 0; i < 256; i++) {
            toUpper[i] = (byte) i;
            toLower[i] = (byte) i;
        }

        for (int lc = 'a'; lc <= 'z'; lc++) {
            int uc = lc + 'A' - 'a';

            toUpper[lc] = (byte) uc;
            toLower[uc] = (byte) lc;
            isAlpha[lc] = true;
            isAlpha[uc] = true;
            isLower[lc] = true;
            isUpper[uc] = true;
        }

        isWhite[' '] = true;
        isWhite['\t'] = true;
        isWhite['\r'] = true;
        isWhite['\n'] = true;
        isWhite['\f'] = true;
        isWhite['\b'] = true;

        for (int d = '0'; d <= '9'; d++) {
            isDigit[d] = true;
        }
    }

    /**
     * Returns the upper case equivalent of the specified ASCII character.
     */

    public static int toUpper(int c) {
        return toUpper[c & 0xff] & 0xff;
    }

    /**
     * Returns the lower case equivalent of the specified ASCII character.
     */

    public static int toLower(int c) {
        return toLower[c & 0xff] & 0xff;
    }

    /**
     * Returns true if the specified ASCII character is upper or lower case.
     */

    public static boolean isAlpha(int c) {
        return isAlpha[c & 0xff];
    }

    /**
     * Returns true if the specified ASCII character is upper case.
     */

    public static boolean isUpper(int c) {
        return isUpper[c & 0xff];
    }

    /**
     * Returns true if the specified ASCII character is lower case.
     */

    public static boolean isLower(int c) {
        return isLower[c & 0xff];
    }

    /**
     * Returns true if the specified ASCII character is white space.
     */

    public static boolean isWhite(int c) {
        return isWhite[c & 0xff];
    }

    /**
     * Returns true if the specified ASCII character is a digit.
     */

    public static boolean isDigit(int c) {
        return isDigit[c & 0xff];
    }

    /**
     * Parses an unsigned integer from the specified subarray of bytes.
     *
     * @param b   the bytes to parse
     * @param off the start offset of the bytes
     * @param len the length of the bytes
     * @throws NumberFormatException if the integer format was invalid
     */
    public static int parseInt(byte[] b, int off, int len)
            throws NumberFormatException {
        int c;

        if (b == null || len <= 0 || !isDigit(c = b[off++])) {
            throw new NumberFormatException();
        }

        int n = c - '0';

        while (--len > 0) {
            if (!isDigit(c = b[off++])) {
                throw new NumberFormatException();
            }
            n = n * 10 + c - '0';
        }

        return n;
    }

    public static int parseInt(char[] b, int off, int len)
            throws NumberFormatException {
        int c;

        if (b == null || len <= 0 || !isDigit(c = b[off++])) {
            throw new NumberFormatException();
        }

        int n = c - '0';

        while (--len > 0) {
            if (!isDigit(c = b[off++])) {
                throw new NumberFormatException();
            }
            n = n * 10 + c - '0';
        }

        return n;
    }

    /**
     * Parses an unsigned long from the specified subarray of bytes.
     *
     * @param b   the bytes to parse
     * @param off the start offset of the bytes
     * @param len the length of the bytes
     * @throws NumberFormatException if the long format was invalid
     */
    public static long parseLong(byte[] b, int off, int len)
            throws NumberFormatException {
        int c;

        if (b == null || len <= 0 || !isDigit(c = b[off++])) {
            throw new NumberFormatException();
        }

        long n = c - '0';
        long m;

        while (--len > 0) {
            if (!isDigit(c = b[off++])) {
                throw new NumberFormatException();
            }
            m = n * 10 + c - '0';

            if (m < n) {
                // Overflow
                throw new NumberFormatException();
            } else {
                n = m;
            }
        }

        return n;
    }

    public static long parseLong(char[] b, int off, int len)
            throws NumberFormatException {
        int c;

        if (b == null || len <= 0 || !isDigit(c = b[off++])) {
            throw new NumberFormatException();
        }

        long n = c - '0';
        long m;

        while (--len > 0) {
            if (!isDigit(c = b[off++])) {
                throw new NumberFormatException();
            }
            m = n * 10 + c - '0';

            if (m < n) {
                // Overflow
                throw new NumberFormatException();
            } else {
                n = m;
            }
        }

        return n;
    }

}
