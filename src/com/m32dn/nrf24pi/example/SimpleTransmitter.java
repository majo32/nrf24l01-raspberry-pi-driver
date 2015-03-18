/*
 * The MIT License
 *
 * Copyright 2015 majo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.m32dn.nrf24pi.example;

import com.m32dn.nrf24pi.Nrf24Controller;
import com.m32dn.nrf24pi.Nrf24Factory;
import com.m32dn.nrf24pi.exception.NrfException;
import com.m32dn.nrf24pi.impl.AddressImpl;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author majo
 */
public class SimpleTransmitter {
    public static void main(String[] args) {
        try {
            Process p = Runtime.getRuntime().exec("gpio load spi");
            p.waitFor();
            byte[] a = {(byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41,
                (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41,
                (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41,
                (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41,
                (byte) 0x41};
            Nrf24Controller c = Nrf24Factory.getInstance();
            c.start();
            c.sendSynchronized(new AddressImpl(0x41,0x41,0x41,0x41,0x41),ByteBuffer.wrap(a));
            c.stop();
        } catch (NrfException | IOException | InterruptedException ex) {
            Logger.getLogger(SimpleTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
