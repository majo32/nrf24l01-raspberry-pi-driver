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

import com.m32dn.nrf24pi.Nfr24Connector;
import com.m32dn.nrf24pi.exception.ControllerInstanceExistsException;
import com.m32dn.nrf24pi.Nrf24Controller;
import com.m32dn.nrf24pi.Nrf24Provider;
import com.m32dn.nrf24pi.enums.AddressLengthIdentifier;
import com.m32dn.nrf24pi.enums.PipeName;
import com.m32dn.nrf24pi.exception.NrfIOException;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author majo
 */
abstract public class BaseFactory {

    private final static Map<String, Object> config = new HashMap();

    static {
        config.put("csn_pin", RaspiPin.GPIO_22);
        config.put("ce_pin", RaspiPin.GPIO_21);
        config.put("irq_pin", RaspiPin.GPIO_23);
        config.put("auto_acknowlegement", true);
        config.put("address_length", AddressLengthIdentifier.L5);
        config.put("crc_length", (byte) 0);
        config.put("channel", (byte) 2);
        config.put("retransmission_delay", (byte) 4);
        config.put("retransmission_trials", (byte) 0x0F);
        config.put("antena_gain", (byte) 3);
        config.put("speed_rate", (byte) 0);
    }
    private static Nrf24Controller controller = null;

    static public Nrf24Controller getInstance() throws NrfIOException, ControllerInstanceExistsException {
        return init(null);
    }
    
    static public Nrf24Controller getInstance(Map<String, Object> customConfig) throws NrfIOException, ControllerInstanceExistsException {
        return init(customConfig);
    }

    static private Nrf24Controller init(Map<String, Object> customConfig) throws ControllerInstanceExistsException, NrfIOException {
        if (controller != null) {
            throw new ControllerInstanceExistsException();
        }
        Nfr24Connector c = new ConnectorImpl((Pin) getConfigAttr("csn_pin", customConfig), (Pin) getConfigAttr("ce_pin", customConfig), (Pin) getConfigAttr("irq_pin", customConfig));
        Nrf24Provider p = new ProviderImpl(c);
        if ((boolean) getConfigAttr("auto_acknowlegement", customConfig)) {
            p.setAutoAcknowledgement(PipeName.PAA, true);
            p.useRxPipe(PipeName.PAA, true);
        }
        p.setAddressLength((AddressLengthIdentifier) getConfigAttr("address_length", customConfig));
        p.setCRCLen((byte) getConfigAttr("crc_length", customConfig));
        p.setChannel((byte) getConfigAttr("channel", customConfig));
        p.setRetransmissionDelay((byte) getConfigAttr("retransmission_delay", customConfig));
        p.setRetransmissionTrials((byte) getConfigAttr("retransmission_trials", customConfig));
        p.setRFOutputPower((byte) getConfigAttr("antena_gain", customConfig));
        p.setSpeedRate((byte) getConfigAttr("speed_rate", customConfig));
        
        ControllerImpl con = new ControllerImpl(p);
        con.setAutoAck((boolean) getConfigAttr("auto_acknowlegement", customConfig));
        controller = con;
        return controller;
    }

    static public Object getConfigAttr(String name, Map<String, Object> customConfig) {
        if (customConfig == null) {
            return config.get(name);
        } else {
            if (customConfig.containsKey(name)) {
                return customConfig.get(name);
            } else {
                return config.get(name);
            }
        }
    }
}
