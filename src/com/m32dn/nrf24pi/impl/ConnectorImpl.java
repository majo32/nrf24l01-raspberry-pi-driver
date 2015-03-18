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

import com.m32dn.nrf24pi.nrfbyte.ByteBuilder;
import com.m32dn.nrf24pi.nrfbyte.CommandByte;
import com.m32dn.nrf24pi.nrfbyte.MessageByte;
import com.m32dn.nrf24pi.nrfbyte.RegisterByte;
import com.m32dn.nrf24pi.Nfr24Connector;
import com.m32dn.nrf24pi.event.IrqListener;
import com.m32dn.nrf24pi.exception.NrfIOException;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ConnectorImpl implements Nfr24Connector {

    private final GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput CEpin;
    private GpioPinDigitalOutput CSNpin;
    private GpioPinDigitalInput IRQpin;
    private SpiDevice spi;
    private final List<GpioPinListenerDigitalAdaptor> listeners = new ArrayList();

    public ConnectorImpl(GpioPinDigitalOutput CSNpin, GpioPinDigitalOutput CEpin, GpioPinDigitalInput IRQpin, SpiDevice spi) {
        init(CSNpin, CEpin, IRQpin, spi);
    }

    public ConnectorImpl(Pin csn, Pin ce, Pin irq, SpiChannel channell) throws NrfIOException {
        try {
            init(gpio.provisionDigitalOutputPin(csn), gpio.provisionDigitalOutputPin(ce), gpio.provisionDigitalInputPin(irq), SpiFactory.getInstance(channell));
        } catch (IOException ex) {
            throw new NrfIOException(ex);
        }
    }

    public ConnectorImpl(Pin csn, Pin ce, Pin irq) throws NrfIOException {
        try {
            init(gpio.provisionDigitalOutputPin(csn), gpio.provisionDigitalOutputPin(ce), gpio.provisionDigitalInputPin(irq), SpiFactory.getInstance(SpiChannel.CS0));
        } catch (IOException ex) {
            throw new NrfIOException(ex);
        }
    }

    public ConnectorImpl() throws NrfIOException {
        try {
            init(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22), gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21), gpio.provisionDigitalInputPin(RaspiPin.GPIO_23), SpiFactory.getInstance(SpiChannel.CS0));
        } catch (IOException ex) {
            throw new NrfIOException(ex);
        }
    }

    private void init(GpioPinDigitalOutput CSNpin, GpioPinDigitalOutput CEpin, GpioPinDigitalInput IRQpin, SpiDevice spi) {
        this.CSNpin = CSNpin;
        this.CEpin = CEpin;
        this.IRQpin = IRQpin;
        this.spi = spi;
    }

    private synchronized ByteBuffer transmit(byte command, ByteBuffer toWrite) throws NrfIOException {
        ByteBuffer b = null;
        CSNpin.setState(false);
        try {
            byte r = spi.write(command)[0];
            System.out.printf("R: %02X \n",r);
            b = spi.write(toWrite);
        } catch (IOException ex) {
            throw new NrfIOException(ex);
        } finally {
            CSNpin.setState(true);
        }
        return b;
    }

    private synchronized byte transmitOne(byte command) throws NrfIOException {
        byte b[] = null;
        CSNpin.setState(false);
        try {
            b = spi.write(command);
            System.out.printf("R: %02X \n",b[0]);
        } catch (IOException ex) {
            throw new NrfIOException(ex);
        } finally {
            CSNpin.setState(true);
        }
        return b[0];
    }

    @Override
    public ByteBuffer writeCommand(byte command, ByteBuffer toWrite) throws NrfIOException {
        return transmit(ByteBuilder.create(command).getCommand(), toWrite);

    }

    @Override
    public ByteBuffer readCommand(byte command, int len) throws NrfIOException {
        return transmit(ByteBuilder.create(command).getCommand(), ByteBuffer.allocate(len));
    }

    @Override
    public byte syncCommand(byte command) throws NrfIOException {
        return transmitOne(ByteBuilder.create(command).getCommand());
    }

    @Override
    public ByteBuffer writeCommand(CommandByte command, ByteBuffer toWrite) throws NrfIOException {
        return transmit(command.getCommand(), toWrite);
    }

    @Override
    public ByteBuffer readCommand(CommandByte command, int len) throws NrfIOException {
        return transmit(command.getCommand(), ByteBuffer.allocate(len));
    }

    @Override
    public byte syncCommand(CommandByte command) throws NrfIOException {
        return transmitOne(command.getCommand());
    }

    @Override
    public ByteBuffer writeRegister(RegisterByte reg, ByteBuffer toWrite) throws NrfIOException {
        return transmit(reg.getRegisterWriteCommand(), toWrite);
    }

    @Override
    public ByteBuffer readRegister(RegisterByte reg, int len) throws NrfIOException {
        return transmit(reg.getRegisterReadCommand(), ByteBuffer.allocate(len));
    }

    @Override
    public byte writeRegister(RegisterByte reg, byte toWrite) throws NrfIOException {
        return transmit(reg.getRegisterWriteCommand(), ByteBuffer.allocate(1).put(ByteBuilder.create(toWrite).getMessage())).get(0);
    }

    @Override
    public byte writeRegister(RegisterByte reg, MessageByte toWrite) throws NrfIOException {
        return transmit(reg.getRegisterWriteCommand(), ByteBuffer.allocate(1).put(toWrite.getMessage())).get(0);
    }

    @Override
    public byte readRegister(RegisterByte reg) throws NrfIOException {
        return transmit(reg.getRegisterReadCommand(), ByteBuffer.allocate(1)).get(0);
    }

    @Override
    public ByteBuffer writeRegister(byte reg, ByteBuffer toWrite) throws NrfIOException {
        return transmit(ByteBuilder.create(reg).getRegisterWriteCommand(), toWrite);
    }

    @Override
    public ByteBuffer readRegister(byte reg, int len) throws NrfIOException {
        return transmit(ByteBuilder.create(reg).getRegisterReadCommand(), ByteBuffer.allocate(len));
    }

    @Override
    public byte writeRegister(byte reg, byte toWrite) throws NrfIOException {
        return transmit(ByteBuilder.create(reg).getRegisterWriteCommand(), ByteBuffer.allocate(1).put(ByteBuilder.create(toWrite).getMessage())).get(0);
    }

    @Override
    public byte writeRegister(byte reg, MessageByte toWrite) throws NrfIOException {
        return transmit(ByteBuilder.create(reg).getRegisterWriteCommand(), ByteBuffer.allocate(1).put(toWrite.getMessage())).get(0);
    }

    @Override
    public byte readRegister(byte reg) throws NrfIOException {
        return transmit(ByteBuilder.create(reg).getRegisterReadCommand(), ByteBuffer.allocate(1)).get(0);
    }

    @Override
    public void startMonitoring() throws NrfIOException {
        CEpin.setState(true);
    }

    @Override
    public void stopMonitoring() throws NrfIOException {
        CEpin.setState(false);
    }

    @Override
    public void fireMessageSending() throws NrfIOException {
        CEpin.setState(true);
        try {
            Thread.sleep(0, 10000);
        } catch (InterruptedException ex) {
            throw new NrfIOException(ex);
        }
        CEpin.setState(false);
    }

    @Override
    public void addIrqListener(IrqListener listener) throws NrfIOException {
        GpioPinListenerDigitalAdaptor l = new GpioPinListenerDigitalAdaptor(listener);
        listeners.add(l);
        IRQpin.addListener(l);
    }

    @Override
    public void removeIrqListener(IrqListener listener) throws NrfIOException {
        GpioPinListenerDigitalAdaptor l = null;
        for (GpioPinListenerDigitalAdaptor a : listeners) {
            if (a.getListener() == listener) {
                l = a;
                break;
            }
        }
        if (l != null) {
            IRQpin.removeListener(l);
        }

    }

    @Override
    public void start() throws NrfIOException {
        this.CSNpin.setState(true);
        this.CEpin.setState(false);
    }

    @Override
    public void stop() throws NrfIOException {
        this.CSNpin.setState(false);
        this.CEpin.setState(false);
    }

    private class GpioPinListenerDigitalAdaptor implements GpioPinListenerDigital {

        private final IrqListener listener;

        public GpioPinListenerDigitalAdaptor(IrqListener listener) {
            this.listener = listener;
        }

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            //System.out.println("toggle");
            if (event.getState().isLow()) {
                listener.signal();
            }
        }

        public IrqListener getListener() {
            return listener;
        }

    }

}
