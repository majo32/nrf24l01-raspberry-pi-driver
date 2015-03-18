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

package com.m32dn.nrf24pi;

import com.m32dn.nrf24pi.enums.PipeName;
import com.m32dn.nrf24pi.event.PacketListener;
import com.m32dn.nrf24pi.exception.NrfIOException;
import com.m32dn.nrf24pi.exception.NrfPipeNotOpenException;
import java.util.concurrent.Future;

/**
 *
 * @author majo
 */
public interface Nrf24Pipe {
    public int getPayloadWidth();
 
    public Future<Nrf24RxPacket> getWaitingForPacketFuture();
    public Nrf24Pipe addPacketListener(PacketListener listener);
    public Nrf24Pipe close() throws NrfIOException, NrfPipeNotOpenException;
    public Nrf24Controller getController();
    public PipeName getPipeName();
    public Nrf24Address getAddress();
}
