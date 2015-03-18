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

package com.m32dn.nrf24pi.impl;

import com.m32dn.nrf24pi.Nrf24Address;
import com.m32dn.nrf24pi.enums.AddressLengthIdentifier;
import java.nio.ByteBuffer;

/**
 *
 * @author majo
 */
public class AddressImpl implements Nrf24Address{
    private byte data []; 
    
    public AddressImpl(byte ... d){
        data = d;
    }
    
    public AddressImpl(int ... d){
        data = new byte [d.length];
        for(int i=0;i<d.length;i++){
            data[i] = (byte) d[i];
        }
    }
    
    @Override
    public ByteBuffer renderByteArray(AddressLengthIdentifier type) {
        return ByteBuffer.wrap(data,0,type.getLength());
    }

    @Override
    public boolean isEqual(Nrf24Address b, AddressLengthIdentifier type) {
        ByteBuffer d = b.renderByteArray(type);
        for(int i = 0;i< type.getLength(); i++){
            if(d.get(i) != data[i]){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDerivated(Nrf24Address b, AddressLengthIdentifier type) {
        ByteBuffer d = b.renderByteArray(type);
        for(int i = 0;i< type.getLength() - 1; i++){
            if(d.get(i) != data[i]){
                return false;
            }
        }
        return true;
    }

    @Override
    public byte getLastByte(AddressLengthIdentifier type) {
        return data[type.getLength() - 1];
    }

    @Override
    public Nrf24Address renderDerivatedAddress(AddressLengthIdentifier type, byte lastByte) {
        byte [] d = new byte [type.getLength()];
        System.arraycopy(data, 0, d, 0, type.getLength() - 1);
        d[type.getLength() - 1] = lastByte;
        return new AddressImpl(d);
    }
    
}
