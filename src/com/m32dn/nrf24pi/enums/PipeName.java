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

package com.m32dn.nrf24pi.enums;

import com.m32dn.nrf24pi.exception.NrfPipeNotExistsException;

/**
 *
 * @author majo
 */
public enum PipeName {

    PAA(CommandsMap.ENAA_P0, CommandsMap.RX_PW_P0, CommandsMap.ERX_P0, CommandsMap.RX_ADDR_P0, 5, true,0),
    P1(CommandsMap.ENAA_P1, CommandsMap.RX_PW_P1, CommandsMap.ERX_P1, CommandsMap.RX_ADDR_P1, 5, false,1),
    P2(CommandsMap.ENAA_P2, CommandsMap.RX_PW_P2, CommandsMap.ERX_P2, CommandsMap.RX_ADDR_P2, 1, false,2),
    P3(CommandsMap.ENAA_P3, CommandsMap.RX_PW_P3, CommandsMap.ERX_P3, CommandsMap.RX_ADDR_P3, 1, false,3),
    P4(CommandsMap.ENAA_P4, CommandsMap.RX_PW_P4, CommandsMap.ERX_P4, CommandsMap.RX_ADDR_P4, 1, false,4),
    P5(CommandsMap.ENAA_P5, CommandsMap.RX_PW_P5, CommandsMap.ERX_P5, CommandsMap.RX_ADDR_P5, 1, false,5);
    private final byte AAbit;
    private final byte RXPayloadWidthRegister;
    private final byte RXEnabledBit;
    private final byte RXAddressRegister;
    private final int RXAddressWidth;
    private final boolean isAAPipe;
    private final int name;

    private PipeName(byte AAbit, byte RXPayloadWidthRegister, byte RXEnabledBit, byte RXAddressRegister, int RXAddressWidth, boolean isAAPipe, int name) {
        this.AAbit = AAbit;
        this.RXPayloadWidthRegister = RXPayloadWidthRegister;
        this.RXEnabledBit = RXEnabledBit;
        this.RXAddressRegister = RXAddressRegister;
        this.RXAddressWidth = RXAddressWidth;
        this.isAAPipe = isAAPipe;
        this.name = name;
    }

    public byte getAAbit() {
        return AAbit;
    }

    public byte getRXPayloadWidthRegister() {
        return RXPayloadWidthRegister;
    }

    public byte getRXEnabledBit() {
        return RXEnabledBit;
    }

    public byte getRXAddressRegister() {
        return RXAddressRegister;
    }

    public int getRXAddressWidth() {
        return RXAddressWidth;
    }

    public boolean isAAPipe() {
        return this.isAAPipe;
    }

    public int getName() {
        return name;
    }
    public static PipeName getByName(int name) throws NrfPipeNotExistsException{
        for(PipeName p : PipeName.values()){
            if(p.getName() == name){
                return p;
            }
        }
        throw new NrfPipeNotExistsException();
    }
    

}
