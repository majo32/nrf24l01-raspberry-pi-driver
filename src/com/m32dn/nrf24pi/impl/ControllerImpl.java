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
import com.m32dn.nrf24pi.Nrf24Address;
import com.m32dn.nrf24pi.Nrf24Controller;
import com.m32dn.nrf24pi.Nrf24Pipe;
import com.m32dn.nrf24pi.Nrf24Provider;
import com.m32dn.nrf24pi.Nrf24RxPacket;
import com.m32dn.nrf24pi.Nrf24TxPacket;
import com.m32dn.nrf24pi.enums.AckState;
import com.m32dn.nrf24pi.enums.AddressLengthIdentifier;
import com.m32dn.nrf24pi.enums.PipeName;
import com.m32dn.nrf24pi.event.AckListener;
import com.m32dn.nrf24pi.event.PacketListener;
import com.m32dn.nrf24pi.exception.NrfIOException;
import com.m32dn.nrf24pi.exception.NrfMessageTimeOutException;
import com.m32dn.nrf24pi.exception.NrfOpenPipeException;
import com.m32dn.nrf24pi.exception.NrfPipeNotOpenException;
import com.m32dn.nrf24pi.exception.NrfUndeliveredMessageException;
import java.nio.ByteBuffer;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ControllerImpl implements Nrf24Controller {

    private final RxPacketHandler nextRxPacketProcess;
    private final AckStateHandler confirmAckProcess;

    private FutureTask<Nrf24RxPacket> nextRxPacketFuture;
    private FutureTask<AckState> confirmAckFuture;

    private final ExecutorService executor;
    private final Nrf24Provider provider;

    private final List<Entry<PipeName, PacketListener>> packetListeners;
    private Nrf24RxPacket lastPacket;
    private final List<AckListener> ackListeners;
    private AckState lastAckState;

    private Nrf24Address currentTxAddress;
    private AddressLengthIdentifier addressLength;
    private final List<Nrf24Pipe> openPipes;
    private boolean autoAck = false;
    private boolean isRxMode = false;

    public ControllerImpl(Nrf24Provider provider) {
        this.provider = provider;
        nextRxPacketProcess = new RxPacketHandler(this);
        confirmAckProcess = new AckStateHandler(this);

        packetListeners = new ArrayList();
        ackListeners = new ArrayList();
        openPipes = new ArrayList();
        executor = Executors.newCachedThreadPool();

        startPacketWaiting();
    }

    protected synchronized void executePacketTask(Status status) {
        if (nextRxPacketFuture != null) {
            nextRxPacketProcess.setStatus(status);
            executor.execute(nextRxPacketFuture);
        }
    }

    protected synchronized void executeAckStateTask(Status status) {
        
        if (confirmAckFuture != null) {
            confirmAckProcess.setStatus(status);
            executor.execute(confirmAckFuture);
        }
    }

    protected final synchronized void startPacketWaiting() {
        nextRxPacketFuture = new FutureTask(nextRxPacketProcess);
    }

    protected synchronized void startAckStateWaiting() {
        confirmAckFuture = new FutureTask(confirmAckProcess);
    }

    protected void addPacketListener(PipeName pipe, PacketListener listener) {
        packetListeners.add(new SimpleEntry(pipe, listener));
    }

    protected void removePacketListener(PipeName pipe, PacketListener listener) {
        for (Entry e : packetListeners) {
            if (e.getKey() == pipe && e.getValue() == listener) {
                packetListeners.remove(e);
            }
        }
    }

    protected void addAckListener(AckListener listener) {
        ackListeners.add(listener);
    }

    protected void removeAckListener(AckListener listener) {
        ackListeners.remove(listener);
    }

    protected List<Entry<PipeName, PacketListener>> getPacketListeners() {
        return packetListeners;
    }

    protected List<AckListener> getAckListeners() {
        return ackListeners;
    }

    protected void handlePacketFromPipe(Nrf24Pipe pipe, Nrf24RxPacket packet) {
        for (Entry<PipeName, PacketListener> entry : getPacketListeners()) {
            if (entry.getKey() == pipe.getPipeName()) {
                entry.getValue().handlePacket(packet);
            }
        }
    }

    protected void handleAckState(AckState state) {
        for (AckListener entry : getAckListeners()) {
            entry.handleAck(state);
        }
    }

    protected void setLastPacket(Nrf24RxPacket packet) {
        lastPacket = packet;
    }

    protected void setLastAckState(AckState lastAckState) {
        this.lastAckState = lastAckState;
    }

    protected void reportException(NrfIOException ex) {
        System.out.println(ex);
    }

    protected ExecutorService getExecutor() {
        return executor;
    }

    public Nrf24RxPacket getLastPacket() {
        return lastPacket;
    }

    protected Nrf24Provider getProvider() {
        return provider;
    }

    public void setAutoAck(boolean autoAck) {
        this.autoAck = autoAck;
    }
    

    @Override
    public Nrf24Controller start() throws NrfIOException {
        provider.getConnector().addIrqListener(new IrqListenerImpl(this));
        provider.start();

        this.addAckListener(new AckListener() {
            @Override
            public void handleAck(AckState ack) {
                /*try {
                    setRxMode();
                } catch (NrfIOException ex) {
                    reportException(ex);
                }*/
            }
        });
        return this;
    }

    @Override
    public AckState sendSynchronized(Nrf24TxPacket packet) throws NrfIOException, NrfUndeliveredMessageException, NrfMessageTimeOutException {
        Future<AckState> f = send(packet);
        AckState ack = null;
        try {
            ack = f.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            throw new NrfMessageTimeOutException(ex);
        } catch (InterruptedException | ExecutionException ex) {
            throw new NrfIOException(ex);
        }
        if (ack == AckState.UNDELIVERED) {
            throw new NrfUndeliveredMessageException();
        }
        return ack;
    }

    @Override
    public AckState sendSynchronized(Nrf24Address address, ByteBuffer payload) throws NrfIOException, NrfUndeliveredMessageException, NrfMessageTimeOutException {
        return sendSynchronized(new TxPacketImpl(address, payload));
    }

    @Override
    public Future<AckState> send(Nrf24TxPacket packet) throws NrfIOException {
        if (currentTxAddress == null || !packet.getTXAddress().isEqual(currentTxAddress, addressLength)) {
            provider.setTxAddress(packet.getTXAddress());
            currentTxAddress = packet.getTXAddress();
            provider.pushConfiguration();
        }
        setTxMode();
        startAckStateWaiting();
        provider.send(packet.getPayload());
        
        return confirmAckFuture;
    }

    @Override
    public Future<AckState> send(Nrf24Address address, ByteBuffer payload) throws NrfIOException {
        return send(new TxPacketImpl(address, payload));
    }

    @Override
    public Nrf24Pipe openPipe(PipeName name, Nrf24Address address, int payload_width) throws NrfIOException, NrfOpenPipeException {
        if (autoAck) {
            if (name == PipeName.PAA) {
                throw new NrfOpenPipeException("Can not open \'Pipe 0\' while auto acknowlegenemt is on!");
            }
        }
        provider.setAutoAcknowledgement(name, autoAck);
        provider.setPayloadWidth(name, (byte) payload_width);
        provider.useRxPipe(name, true);
        if (name.getRXAddressWidth() != 5) {
            try {
                Nrf24Pipe p1 = getPipe(PipeName.P1);
                if (!address.isDerivated(p1.getAddress(), addressLength)) {
                    throw new NrfOpenPipeException("Address of \'Pipe 2 .. 5\' must have first n-1 bytes the same with \'Pipe 1\'!");
                }

            } catch (NrfPipeNotOpenException ex) {
                throw new NrfOpenPipeException("Can not open \'Pipe 2 .. 5\' while \'Pipe 1\' is not open!",ex);
            }
            provider.setRxAddressLsByte(name, address.getLastByte(addressLength));
        } else {
            provider.setRxAddress(name, address);
        }
        provider.pushConfiguration();
        Nrf24Pipe p = new PipeImpl(this, name, address, payload_width);
        openPipes.add(p);
        return p;
    }

    @Override
    public Nrf24Pipe getPipe(PipeName name) throws NrfPipeNotOpenException {
        for (Nrf24Pipe p : openPipes) {
            if (p.getPipeName() == name) {
                return p;
            }
        }
        throw new NrfPipeNotOpenException();
    }

    @Override
    public Nrf24Controller closePipe(PipeName name) throws NrfIOException, NrfPipeNotOpenException {
        provider.useRxPipe(name, true);
        Nrf24Pipe p = getPipe(name);
        openPipes.remove(p);
        provider.pushConfiguration();
        return this;
    }

    @Override
    public Nrf24Controller stop() throws NrfIOException {
        provider.stop();
        return this;
    }

    @Override
    public Nrf24Controller setRxMode() throws NrfIOException {
        if (!isRxMode) {
            provider.setRxMode();
            isRxMode = true;
        }
        return this;
    }

    @Override
    public Nrf24Controller setTxMode() throws NrfIOException {
        if (isRxMode) {
            provider.setTxMode();
            isRxMode = false;
        }
        return this;
    }

    @Override
    public boolean isRxMode() {
        return isRxMode;
    }

}
