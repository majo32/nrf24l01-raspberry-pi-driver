/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.m32dn.nrf24pi.nrfbyte;

import com.m32dn.nrf24pi.enums.CommandsMap;

/**
 *
 * @author majo
 */
public class ByteBuilder implements MessageByte, RegisterByte, CommandByte{
    private byte content;

    private ByteBuilder(byte content) {
        this.content = content;
    }
          
    public static ByteBuilder create(byte init){
        return new ByteBuilder(init);
    }
    public static ByteBuilder create(){
        return new ByteBuilder((byte)0);
    }
    public static ByteBuilder compose(byte ... activebits){
        ByteBuilder retval =  new ByteBuilder((byte)0);
        for(byte b : activebits){
            retval.setBit(b);
        }
        return retval;
    }
    
    public ByteBuilder setBits(byte start, byte data){
        this.content |= (byte) (data << start);
        return this;
    }
    public ByteBuilder setBit(byte bitNum){
        return setBits(bitNum,(byte)1);
    }
    
    @Override
    public byte getByte() {
        return this.content;
    }

    @Override
    public byte getRegisterReadCommand() {
        return (byte) ((this.content & CommandsMap.REGISTER_MASK) | CommandsMap.R_REGISTER);
    }

    @Override
    public byte getRegisterWriteCommand() {
        return (byte) ((this.content & CommandsMap.REGISTER_MASK) | CommandsMap.W_REGISTER);
    }

    @Override
    public byte getMessage() {
        return this.content;
    }

    @Override
    public byte getCommand() {
       return this.content;
    }
    
}
