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
public interface Nrf24Provider {
    public Nrf24Provider setChannel(byte channel);
    public Nrf24Provider setRFOutputPower(byte outputPower);
    public Nrf24Provider setSpeedRate(byte speedRate);
    public Nrf24Provider useCRC(boolean useCRC);
    public Nrf24Provider setCRCLen(byte CRCLen);
    public Nrf24Provider setRetransmissionDelay(byte delay);
    public Nrf24Provider setRetransmissionTrials(byte trials);
    public Nrf24Provider setAddressLength(AddressLengthIdentifier al);
    
    public Nrf24Provider setTxAddress(Nrf24Address address);
    
    public Nrf24Provider setRxAddress(PipeName pipe,Nrf24Address address);
    public Nrf24Provider setRxAddressLsByte(PipeName pipe, Byte lsByte);
    public Nrf24Provider setAutoAcknowledgement(PipeName pipe,boolean useAA);
    public Nrf24Provider useRxPipe(PipeName pipe, boolean usePipe);
    public Nrf24Provider setPayloadWidth(PipeName pipe,byte len);
    
    public Nrf24Provider pushConfiguration() throws NrfIOException;
 
    public Nrf24Provider send(ByteBuffer packet) throws NrfIOException;
    public ByteBuffer read(int len) throws NrfIOException;
    
    public Status readStatus() throws NrfIOException;
    public Nrf24Provider clearStatus(Status status) throws NrfIOException;
    
    public Nrf24Provider flushRxFifo() throws NrfIOException;
    public Nrf24Provider flushTxFifo() throws NrfIOException;
    
    public Nrf24Provider start() throws NrfIOException;
    public Nrf24Provider stop() throws NrfIOException;
    
    public Nrf24Provider setPowerOn() throws NrfIOException;
    public Nrf24Provider setPowerOff() throws NrfIOException;
    
    public Nrf24Provider setRxMode() throws NrfIOException;
    public Nrf24Provider setTxMode() throws NrfIOException;
    
    public Nfr24Connector getConnector();
   
}
