/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.m32dn.nrf24pi;

import com.m32dn.nrf24pi.enums.AddressLengthIdentifier;
import java.nio.ByteBuffer;

/**
 *
 * @author majo
 */
public interface Nrf24Address {
    public ByteBuffer renderByteArray(AddressLengthIdentifier type);
    public boolean isEqual(Nrf24Address b,AddressLengthIdentifier type);
    public boolean isDerivated(Nrf24Address b,AddressLengthIdentifier type);
    public byte getLastByte(AddressLengthIdentifier type);
    public Nrf24Address renderDerivatedAddress(AddressLengthIdentifier type,byte lastByte);
}
