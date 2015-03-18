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
import com.m32dn.nrf24pi.exception.NrfOpenPipeException;
import com.m32dn.nrf24pi.exception.NrfPipeNotOpenException;
import com.m32dn.nrf24pi.exception.NrfUndeliveredMessageException;
import com.m32dn.nrf24pi.exception.NrfMessageTimeOutException;
import com.m32dn.nrf24pi.enums.AckState;
import com.m32dn.nrf24pi.exception.NrfIOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

/**
 *
 * @author majo
 */
public interface Nrf24Controller {

    public Nrf24Controller start() throws NrfIOException;

    public AckState sendSynchronized(Nrf24TxPacket packet) throws NrfIOException, NrfUndeliveredMessageException, NrfMessageTimeOutException;

    public AckState sendSynchronized(Nrf24Address address, ByteBuffer payload) throws NrfIOException, NrfUndeliveredMessageException, NrfMessageTimeOutException;

    public Future<AckState> send(Nrf24TxPacket packet) throws NrfIOException;

    public Future<AckState> send(Nrf24Address address, ByteBuffer payload) throws NrfIOException;

    public Nrf24Pipe openPipe(PipeName name, Nrf24Address address, int payload_width) throws NrfIOException, NrfOpenPipeException;

    public Nrf24Pipe getPipe(PipeName name) throws NrfPipeNotOpenException;

    public Nrf24Controller closePipe(PipeName name) throws NrfIOException, NrfPipeNotOpenException;

    public Nrf24Controller setRxMode() throws NrfIOException;
    
    public boolean isRxMode();

    public Nrf24Controller setTxMode() throws NrfIOException;

    public Nrf24Controller stop() throws NrfIOException;

}
