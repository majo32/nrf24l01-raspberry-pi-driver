/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.m32dn.nrf24pi;

import java.nio.ByteBuffer;

/**
 *
 * @author majo
 */
public interface Nrf24TxPacket {
    public Nrf24Address getTXAddress();
    public ByteBuffer getPayload();
}
