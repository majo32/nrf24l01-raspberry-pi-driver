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
import com.m32dn.nrf24pi.Nrf24Controller;
import com.m32dn.nrf24pi.event.PacketListener;
import com.m32dn.nrf24pi.Nrf24Pipe;
import com.m32dn.nrf24pi.enums.PipeName;
import com.m32dn.nrf24pi.Nrf24RxPacket;
import com.m32dn.nrf24pi.exception.NrfIOException;
import com.m32dn.nrf24pi.exception.NrfPipeNotOpenException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 *
 * @author majo
 */
public class PipeImpl implements Nrf24Pipe {

    private final PipeName name;
    private final int payloadWidth;
    private final ControllerImpl controller;

    private final PipeRxPacketHandler packetHandler;
    private FutureTask<Nrf24RxPacket> packetTask;
    private final Nrf24Address address;

    public PipeImpl(ControllerImpl controller, PipeName name, Nrf24Address address, int payloadWidth) {
        this.payloadWidth = payloadWidth;
        this.controller = controller;
        this.name = name;
        this.address = address;
        packetHandler = new PipeRxPacketHandler(this);

        this.controller.addPacketListener(this.name, new PipePacketListenerImpl(this));
    }

    protected void startPacketTaskWaiting() {
        packetTask = new FutureTask(packetHandler);
    }

    protected void executePacketTask(Nrf24RxPacket packet) {
        if (packetTask != null) {
            packetHandler.setPacket(packet);
            controller.getExecutor().execute(packetTask);
        }
    }

    @Override
    public int getPayloadWidth() {
        return payloadWidth;
    }

    @Override
    public Future<Nrf24RxPacket> getWaitingForPacketFuture() {
        return packetTask;
    }

    @Override
    public Nrf24Pipe addPacketListener(PacketListener listener) {
        controller.addPacketListener(name, listener);
        return this;
    }

    @Override
    public Nrf24Pipe close() throws NrfIOException, NrfPipeNotOpenException {
        controller.closePipe(name);
        return this;
    }

    @Override
    public Nrf24Controller getController() {
        return controller;
    }

    @Override
    public PipeName getPipeName() {
        return name;
    }

    @Override
    public Nrf24Address getAddress() {
        return address;
    }

}
