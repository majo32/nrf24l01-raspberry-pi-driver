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

package com.m32dn.nrf24pi.response;

import com.m32dn.nrf24pi.enums.CommandsMap;


/**
 *
 * @author majo
 */
public class Status {

    private boolean rxFifoReady;
    private boolean txFifoReady;
    private boolean maxRetriesExceeded;
    private byte pipeNumber;
    private boolean txFifoFull;

    public static byte renderResetByte() {
        byte retval = 0;
        retval |= (1 << CommandsMap.RX_DR);
        retval |= (1 << CommandsMap.TX_DS);
        retval |= (1 << CommandsMap.MAX_RT);

        return retval;
    }
    public static Status renderResetStatus(){
        return new Status(Status.renderResetByte());
    }
    public Status() {
        
    }
    
    public Status(Byte b) {
        setData(b);
    }
    
    public byte renderClearByte() {
        byte retval = 0;
        retval |= rxFifoReady ? (1 << CommandsMap.RX_DR) : 0;
        retval |= txFifoReady ? (1 << CommandsMap.TX_DS) : 0;
        retval |= maxRetriesExceeded ? (1 << CommandsMap.MAX_RT) : 0;
        
        return retval;
    }

    public final void setData(byte data) {
        this.rxFifoReady = ((data & (1 << CommandsMap.RX_DR)) != 0);
        this.txFifoReady = ((data & (1 << CommandsMap.TX_DS)) != 0);
        this.maxRetriesExceeded = ((data & (1 << CommandsMap.MAX_RT)) != 0);
        this.txFifoFull = ((data & (1 << CommandsMap.TX_FULL)) != 0);
        this.pipeNumber = (byte) ((data & 0b00001110) >> CommandsMap.RX_P_NO);
    }

    public boolean isRxFifoReady() {
        return rxFifoReady;
    }

    public boolean isTxFifoReady() {
        return txFifoReady;
    }

    public boolean isMaxRetriesExceeded() {
        return maxRetriesExceeded;
    }

    public boolean isTxFifoFull() {
        return txFifoFull;
    }

    public byte getPipeNumber() {
        return pipeNumber;
    }
    
    public boolean empty(){
        return !this.maxRetriesExceeded && !this.rxFifoReady && !this.txFifoReady;
    }

}
