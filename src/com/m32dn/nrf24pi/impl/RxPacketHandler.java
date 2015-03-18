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

import com.m32dn.nrf24pi.response.Status;
import com.m32dn.nrf24pi.Nrf24Pipe;
import com.m32dn.nrf24pi.enums.PipeName;
import com.m32dn.nrf24pi.Nrf24RxPacket;
import java.util.concurrent.Callable;

/**
 *
 * @author majo
 */
class RxPacketHandler implements Callable<Nrf24RxPacket> {

    private Status status;
    private final ControllerImpl controller;

    public RxPacketHandler(ControllerImpl controller) {
        this.controller = controller;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Nrf24RxPacket call() throws Exception {
        PipeName p = PipeName.getByName(status.getPipeNumber());
        Nrf24Pipe pipe = controller.getPipe(p);
        Nrf24RxPacket packet = new RxPacketImpl(pipe,controller.getProvider().read(pipe.getPayloadWidth()));
        controller.setLastPacket(packet);
        controller.handlePacketFromPipe(pipe, packet);
        controller.startPacketWaiting();
        return packet;
    }

}
