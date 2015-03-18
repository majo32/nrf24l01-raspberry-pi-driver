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

import com.m32dn.nrf24pi.event.IrqListener;
import com.m32dn.nrf24pi.nrfbyte.CommandByte;
import com.m32dn.nrf24pi.nrfbyte.MessageByte;
import com.m32dn.nrf24pi.nrfbyte.RegisterByte;
import com.m32dn.nrf24pi.exception.NrfIOException;
import java.nio.ByteBuffer;

/**
 *
 * @author majo
 */
public interface Nfr24Connector {
    /**
     * Write command and than write toWrite bytes to chip in one transaction;
     * @param command
     * @param toWrite
     * @return bytes returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public ByteBuffer writeCommand(byte command, ByteBuffer toWrite) throws NrfIOException;
    /**
     * Write command and than wait for len bytes from chip
     * @param command
     * @param len
     * @return bytes returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public ByteBuffer readCommand(byte command, int len) throws NrfIOException;
    /**
     * Write command. Return bytes provided by chip during transmission.
     * @param command
     * @return bytes provided by chip during transmission
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public byte syncCommand(byte command) throws NrfIOException;
    /**
     * Write command and than write toWrite bytes to chip in one transaction;
     * @param command
     * @param toWrite
     * @return bytes returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public ByteBuffer writeCommand(CommandByte command, ByteBuffer toWrite) throws NrfIOException;
    /**
     * Write command and than wait for len bytes from chip
     * @param command
     * @param len
     * @return bytes returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public ByteBuffer readCommand(CommandByte command, int len) throws NrfIOException;
    /**
     * Write command. Return bytes provided by chip during transmission.
     * @param command
     * @return bytes provided by chip during transmission
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public byte syncCommand(CommandByte command) throws NrfIOException;
    /**
     * Write write-register command and than write toWrite bytes to chip in one transaction;
     * @param reg register without write-register mask
     * @param toWrite
     * @return bytes returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public ByteBuffer writeRegister(RegisterByte reg, ByteBuffer toWrite) throws NrfIOException;
    /**
     * Write read-register command and than wait for len bytes from chip
     * @param reg register without read-register mask
     * @param len
     * @return bytes returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public ByteBuffer readRegister(RegisterByte reg, int len) throws NrfIOException;
    /**
     * Write write-register command and than write toWrite byte to chip in one transaction;
     * @param reg register without write-register mask
     * @param toWrite
     * @return byte returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public byte writeRegister(RegisterByte reg, byte toWrite) throws NrfIOException;
    /**
     * Write write-register command and than write toWrite byte to chip in one transaction;
     * @param reg register without write-register mask
     * @param toWrite
     * @return byte returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public byte writeRegister(RegisterByte reg, MessageByte toWrite) throws NrfIOException;
    /**
     * Write read-register command and than wait for 1 byte from chip
     * @param reg register without read-register mask
     * @return byte returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public byte readRegister(RegisterByte reg) throws NrfIOException;
    /**
     * Write write-register command and than write toWrite bytes to chip in one transaction;
     * @param reg register without write-register mask
     * @param toWrite
     * @return bytes returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public ByteBuffer writeRegister(byte reg, ByteBuffer toWrite) throws NrfIOException;
    /**
     * Write read-register command and than wait for len bytes from chip
     * @param reg register without read-register mask
     * @param len
     * @return bytes returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public ByteBuffer readRegister(byte reg, int len) throws NrfIOException;
    /**
     * Write write-register command and than write toWrite byte to chip in one transaction;
     * @param reg register without write-register mask
     * @param toWrite
     * @return byte returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public byte writeRegister(byte reg, byte toWrite) throws NrfIOException;
    /**
     * Write write-register command and than write toWrite byte to chip in one transaction;
     * @param reg register without write-register mask
     * @param toWrite
     * @return byte returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public byte writeRegister(byte reg, MessageByte toWrite) throws NrfIOException;
    /**
     * Write read-register command and than wait for 1 byte from chip
     * @param reg register without read-register mask
     * @return byte returned via SPI
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public byte readRegister(byte reg) throws NrfIOException;
    /**
     * put CE pin HIGH to start monitoring
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public void startMonitoring() throws NrfIOException;
    /**
     * put CE pin LOW to stop monitoring
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public void stopMonitoring() throws NrfIOException;
    /**
     * low-to-high transition of CE pin for min 10 us
     * @throws com.m32dn.nrf24pi.exception.NrfIOException
     */
    public void fireMessageSending() throws NrfIOException;
    /**
     * register listener that handle IRQ pin signal
     * @param listener
     * @throws NrfIOException 
     */
    public void addIrqListener(IrqListener listener) throws NrfIOException;
    public void removeIrqListener(IrqListener listener) throws NrfIOException;
    /**
     * set CSN pin HIGH
     * @throws NrfIOException 
     */
    public void start() throws NrfIOException;
    /**
     * set pins LOW
     * @throws NrfIOException 
     */
    public void stop() throws NrfIOException;
}
