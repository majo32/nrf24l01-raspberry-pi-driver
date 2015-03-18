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
import com.m32dn.nrf24pi.enums.AddressLengthIdentifier;
import com.m32dn.nrf24pi.enums.CommandsMap;
import com.m32dn.nrf24pi.enums.PipeName;
import com.m32dn.nrf24pi.response.Status;
import com.m32dn.nrf24pi.Nfr24Connector;
import com.m32dn.nrf24pi.Nrf24Provider;
import com.m32dn.nrf24pi.exception.NrfIOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author majo
 */
public class ProviderImpl implements Nrf24Provider {

    private final Nfr24Connector connector;
    private final Map<String, Byte> configMap;
    private final Map<Byte, ByteBuffer> registerQueue;
    private AddressLengthIdentifier addressLen = AddressLengthIdentifier.L5;
    private boolean ACK = false;

    public ProviderImpl(Nfr24Connector connector) {
        this.configMap = new HashMap();
        this.connector = connector;
        this.registerQueue = new HashMap();
         resetConfig();

    }

    public final Nrf24Provider resetConfig(){
        setConfigAttribute("LNA_gain", (byte) 1);
        setConfigAttribute("PLL_lock", (byte) 0);
        return this;
    }

    private byte getConfigAttribute(String name) {
        byte b = 0;
        if (configMap.containsKey(name)) {
            b = configMap.get(name);
        } else {
            configMap.put(name, (byte) 0);
        }
        return b;
    }

    private void setConfigAttribute(String name, Byte b) {
        configMap.put(name, b);
    }

    private Byte enqueueChannelRegister() {
        return enqueueRegister(CommandsMap.RF_CH, getConfigAttribute("channel"));
        
    }

    private Byte enqueueRfSetupRegister() {
        return enqueueRegister(CommandsMap.RF_SETUP, (byte) ((getConfigAttribute("PLL_lock") << CommandsMap.PLL_LOCK) | (getConfigAttribute("data_rate") << CommandsMap.RF_DR) | (getConfigAttribute("output_power") << CommandsMap.RF_PWR) | (getConfigAttribute("LNA_gain"))));
    }

    private Byte enqueueConfigRegister() {
        return enqueueRegister(CommandsMap.CONFIG, (byte) ((getConfigAttribute("use_crc") << CommandsMap.EN_CRC) | (getConfigAttribute("crc_len") << CommandsMap.CRCO) | (getConfigAttribute("pwr_up") << CommandsMap.PWR_UP) | (getConfigAttribute("type") << CommandsMap.PRIM_RX)));
    }

    private Byte enqueueSetupRetransmissionsRegister() {
        return enqueueRegister(CommandsMap.SETUP_RETR, (byte) ((getConfigAttribute("retransmission_delay") << CommandsMap.ARD) | (getConfigAttribute("retransmission_trials") << CommandsMap.ARC)));
    }

    private Byte enqueueAddressWidthRegister() {
        return enqueueRegister(CommandsMap.SETUP_AW, getConfigAttribute("address_len"));
    }

    private Byte enqueueAutoAcknowledgementRegister() {
        byte b = 0;
        for (PipeName p : PipeName.values()) {
                b |= getConfigAttribute("auto_ack_" + p.toString()) << p.getAAbit();
        }
        return enqueueRegister(CommandsMap.EN_AA, b);
    }
    
    private Byte enqueueEnableRxAddressRegister() {
        byte b = 0;
        for (PipeName p : PipeName.values()) {
               b |= getConfigAttribute("rx_pipe_" + p.toString()) << p.getRXEnabledBit();
        }
        return enqueueRegister(CommandsMap.EN_RXADDR, b);
    }

    private Byte enqueueRegister(byte register, byte data) {
        registerQueue.put(register, ByteBuffer.allocate(1).put(data));
        return register;
    }

    private Byte enqueueRegister(byte register, ByteBuffer data) {
        registerQueue.put(register, data);
        return register;
    }

    @Override
    public Nrf24Provider setChannel(byte channel) {
        setConfigAttribute("channel", (byte) (channel & 0b01111111));
        enqueueChannelRegister();
        return this;
    }

    @Override
    public Nrf24Provider setRFOutputPower(byte outputPower) {
        setConfigAttribute("output_power", outputPower);
        enqueueRfSetupRegister();
        return this;
    }

    @Override
    public Nrf24Provider setSpeedRate(byte speedRate) {
        setConfigAttribute("data_rate", speedRate);
        enqueueRfSetupRegister();
        return this;
    }

    @Override
    public Nrf24Provider useCRC(boolean useCRC) {
        setConfigAttribute("use_crc", (byte) (useCRC ? 1 : 0));
        enqueueConfigRegister();
        return this;
    }

    @Override
    public Nrf24Provider setCRCLen(byte CRCLen) {
        setConfigAttribute("crc_len", CRCLen);
        useCRC(true);
        enqueueConfigRegister();
        return this;
    }

    @Override
    public Nrf24Provider setRetransmissionDelay(byte delay) {
        setConfigAttribute("retransmission_delay", delay);
        enqueueSetupRetransmissionsRegister();
        return this;
    }

    @Override
    public Nrf24Provider setRetransmissionTrials(byte trials) {
        setConfigAttribute("retransmission_trials", trials);
        enqueueSetupRetransmissionsRegister();
        return this;
    }

    @Override
    public Nrf24Provider setAddressLength(AddressLengthIdentifier al) {
        addressLen = al;
        setConfigAttribute("address_len", al.getIdentifier());
        enqueueAddressWidthRegister();
        return this;

    }

    @Override
    public Nrf24Provider setAutoAcknowledgement(PipeName pipe, boolean useAA) {
        setConfigAttribute("auto_ack_" + pipe.toString(), (byte) (useAA ? 1 : 0));
        ACK = false;
        for (PipeName p : PipeName.values()) {
            if (getConfigAttribute("auto_ack_" + p.toString()) != 0) {
                ACK = true;
            }
        }
        if (ACK) {
            setConfigAttribute("auto_ack_" + PipeName.PAA.toString(), (byte) 1);
            useCRC(true);
        } else {
            setConfigAttribute("auto_ack_" + PipeName.PAA.toString(), (byte) 0);
        }
        enqueueAutoAcknowledgementRegister();
        return this;
    }

    @Override
    public Nrf24Provider useRxPipe(PipeName pipe, boolean usePipe) {
        setConfigAttribute("rx_pipe_" + pipe.toString(), (byte) (usePipe ? 1 : 0));
        enqueueEnableRxAddressRegister();
        return this;
    }

    @Override
    public Nrf24Provider setPayloadWidth(PipeName pipe, byte len) {
        setConfigAttribute("payload_width_" + pipe.toString(), len);
        enqueueRegister(pipe.getRXPayloadWidthRegister(), len);
        return this;
    }

    @Override
    public Nrf24Provider pushConfiguration() throws NrfIOException {
        for (Entry<Byte, ByteBuffer> entry : registerQueue.entrySet()) {
            connector.writeRegister(entry.getKey(), entry.getValue());
        }
        registerQueue.clear();
        return this;
    }

    public Nrf24Provider pushRegister(Byte reg, Byte value) throws NrfIOException {
        connector.writeRegister(reg, value);
        return this;
    }
    public Nrf24Provider pushRegister(Byte reg, ByteBuffer value) throws NrfIOException {
        connector.writeRegister(reg, value);
        return this;
    }
    public Nrf24Provider pushRegister(Byte reg) throws NrfIOException {
        if(registerQueue.get(reg) == null){
            return this;
        }
        connector.writeRegister(reg, registerQueue.get(reg));
        registerQueue.remove(reg);
        return this;
    }

    @Override
    public Nrf24Provider setTxAddress(Nrf24Address address) {
        if (ACK) {
            enqueueRegister(CommandsMap.RX_ADDR_P0, address.renderByteArray(addressLen));
        }
        enqueueRegister(CommandsMap.TX_ADDR, address.renderByteArray(addressLen));
        return this;
    }

    @Override
    public Nrf24Provider setRxAddress(PipeName pipe, Nrf24Address address) {
        enqueueRegister(pipe.getRXAddressRegister(), address.renderByteArray(addressLen));
        return this;
    }
    
    @Override
    public Nrf24Provider setRxAddressLsByte(PipeName pipe, Byte lsByte) {
        enqueueRegister(pipe.getRXAddressRegister(), lsByte);
        return this;
    }

    @Override
    public Nrf24Provider send(ByteBuffer packet) throws NrfIOException {
        connector.writeCommand(CommandsMap.W_TX_PAYLOAD, packet);
        connector.fireMessageSending();
        return this;
    }

    @Override
    public ByteBuffer read(int len) throws NrfIOException {
        return connector.readCommand(CommandsMap.R_RX_PAYLOAD, len);
    }

    @Override
    public Status readStatus() throws NrfIOException {
        Byte b = connector.syncCommand(CommandsMap.NOP);
        Status s = new Status(b);
        return s;
    }

    @Override
    public Nrf24Provider clearStatus(Status status) throws NrfIOException {
        pushRegister(CommandsMap.STATUS, status.renderClearByte());
        return this;
    }
    
    protected Nrf24Provider resetStatus() throws NrfIOException {
        pushRegister(CommandsMap.STATUS, Status.renderResetByte());
        return this;
    }

    @Override
    public Nrf24Provider flushRxFifo() throws NrfIOException {
        connector.syncCommand(CommandsMap.FLUSH_RX);
        return this;
    }

    @Override
    public Nrf24Provider flushTxFifo() throws NrfIOException {
        connector.syncCommand(CommandsMap.FLUSH_TX);
        return this;
    }

    @Override
    public Nrf24Provider start() throws NrfIOException {
        connector.start();
        resetStatus();
        flushRxFifo();
        flushTxFifo();
        pushConfiguration();
        setPowerOn();
        return this;
    }

    @Override
    public Nrf24Provider stop() throws NrfIOException {
        setPowerOff();
        connector.stop();
        return this;
    }

    @Override
    public Nrf24Provider setPowerOn() throws NrfIOException{
        setConfigAttribute("pwr_up", (byte) 1);
        pushRegister(enqueueConfigRegister());
        return this;
    }

    @Override
    public Nrf24Provider setPowerOff() throws NrfIOException{
        setConfigAttribute("pwr_up", (byte) 0);
        pushRegister(enqueueConfigRegister());
        return this;
    }

    @Override
    public Nrf24Provider setRxMode() throws NrfIOException{
        setConfigAttribute("type", (byte) 1);
        connector.startMonitoring();
        pushRegister(enqueueConfigRegister());
        return this;
    }

    @Override
    public Nrf24Provider setTxMode() throws NrfIOException{
        setConfigAttribute("type", (byte) 0);
        connector.stopMonitoring();
        pushRegister(enqueueConfigRegister());
        return this;
    }

    @Override
    public Nfr24Connector getConnector() {
        return connector;
    }

}
