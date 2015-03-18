/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.m32dn.nrf24pi.enums;

/**
 *
 * @author majo
 */
public enum AddressLengthIdentifier {
    L3((byte)3,(byte)0b00000001),
    L4((byte)4,(byte)0b00000010),
    L5((byte)5,(byte)0b00000011);
    private final byte length;
    private final byte identifier;

    private AddressLengthIdentifier(byte length, byte identifier) {
        this.length = length;
        this.identifier = identifier;
    }

    public byte getLength() {
        return length;
    }

    public byte getIdentifier() {
        return identifier;
    } 
    
    
    
}
