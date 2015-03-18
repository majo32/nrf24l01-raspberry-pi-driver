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

import com.m32dn.nrf24pi.Nrf24Pipe;
import com.m32dn.nrf24pi.Nrf24RxPacket;
import java.nio.ByteBuffer;

/**
 *
 * @author majo
 */
public class RxPacketImpl implements Nrf24RxPacket {

    private final Nrf24Pipe pipe;
    private final ByteBuffer payload;

    public RxPacketImpl(Nrf24Pipe pipe, ByteBuffer payload) {
        this.pipe = pipe;
        this.payload = payload;
    }

    @Override
    public Nrf24Pipe getPipe() {
        return pipe;
    }

    @Override
    public ByteBuffer getPayload() {
        return payload;
    }

}
