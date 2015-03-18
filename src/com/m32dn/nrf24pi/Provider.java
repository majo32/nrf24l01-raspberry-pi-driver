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
import com.m32dn.nrf24pi.enums.AddressLengthIdentifier;
import com.m32dn.nrf24pi.response.Status;
import com.m32dn.nrf24pi.exception.NrfIOException;
import java.nio.ByteBuffer;

/**
 *
 * @author majo
 */
public interface Provider {
    public Provider setChannel(byte channel);
    public Provider setRFOutputPower(byte outputPower);
    public Provider setSpeedRate(byte speedRate);
    public Provider useCRC(boolean useCRC);
    public Provider setCRCLen(byte CRCLen);
    public Provider setRetransmissionDelay(byte delay);
    public Provider setRetransmissionTrials(byte trials);
    public Provider setAddressLength(AddressLengthIdentifier al);
    
    public Provider setTxAddress(Address address);
    
    public Provider setRxAddress(PipeName pipe,Address address);
    public Provider setRxAddressLsByte(PipeName pipe, Byte lsByte);
    public Provider setAutoAcknowledgement(PipeName pipe,boolean useAA);
    public Provider useRxPipe(PipeName pipe, boolean usePipe);
    public Provider setPayloadWidth(PipeName pipe,byte len);
    
    public Provider pushConfiguration() throws NrfIOException;
 
    public Provider send(ByteBuffer packet) throws NrfIOException;
    public ByteBuffer read(int len) throws NrfIOException;
    
    public Status readStatus() throws NrfIOException;
    public Provider clearStatus(Status status) throws NrfIOException;
    
    public Provider flushRxFifo() throws NrfIOException;
    public Provider flushTxFifo() throws NrfIOException;
    
    public Provider start() throws NrfIOException;
    public Provider stop() throws NrfIOException;
    
    public Provider setPowerOn() throws NrfIOException;
    public Provider setPowerOff() throws NrfIOException;
    
    public Provider setRxMode() throws NrfIOException;
    public Provider setTxMode() throws NrfIOException;
    
    public Connector getConnector();
   
}
