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
public class CommandsMap {

    public static final byte CONFIG = 0x00;
    public static final byte EN_AA = 0x01;
    public static final byte EN_RXADDR = 0x02;
    public static final byte SETUP_AW = 0x03;
    public static final byte SETUP_RETR = 0x04;
    public static final byte RF_CH = 0x05;
    public static final byte RF_SETUP = 0x06;
    public static final byte STATUS = 0x07;
    public static final byte OBSERVE_TX = 0x08;
    public static final byte CD = 0x09;
    public static final byte RX_ADDR_P0 = 0x0A;
    public static final byte RX_ADDR_P1 = 0x0B;
    public static final byte RX_ADDR_P2 = 0x0C;
    public static final byte RX_ADDR_P3 = 0x0D;
    public static final byte RX_ADDR_P4 = 0x0E;
    public static final byte RX_ADDR_P5 = 0x0F;
    public static final byte TX_ADDR = 0x10;
    public static final byte RX_PW_P0 = 0x11;
    public static final byte RX_PW_P1 = 0x12;
    public static final byte RX_PW_P2 = 0x13;
    public static final byte RX_PW_P3 = 0x14;
    public static final byte RX_PW_P4 = 0x15;
    public static final byte RX_PW_P5 = 0x16;
    public static final byte FIFO_STATUS = 0x17;
    public static final byte DYNPD = 0x1C;

    /* CONFIG register bits*/
    public static final byte MASK_RX_DR = 6;
    public static final byte MASK_TX_DS = 5;
    public static final byte MASK_MAX_RT = 4;
    public static final byte EN_CRC = 3;
    public static final byte CRCO = 2;
    public static final byte PWR_UP = 1;
    public static final byte PRIM_RX = 0;

    /* EN_AA register bits*/
    public static final byte ENAA_P5 = 5;
    public static final byte ENAA_P4 = 4;
    public static final byte ENAA_P3 = 3;
    public static final byte ENAA_P2 = 2;
    public static final byte ENAA_P1 = 1;
    public static final byte ENAA_P0 = 0;

    /* EN_RXADDR register bits */
    public static final byte ERX_P5 = 5;
    public static final byte ERX_P4 = 4;
    public static final byte ERX_P3 = 3;
    public static final byte ERX_P2 = 2;
    public static final byte ERX_P1 = 1;
    public static final byte ERX_P0 = 0;

    /* SETUP_AW register bits */
    public static final byte AW = 0; /* 2 bits */

    /* SETUP_RETR register bits */
    public static final byte ARD = 4; /* 4 bits */

    public static final byte ARC = 0; /* 4 bits */

    /* RF_SETUP register bits*/
    public static final byte PLL_LOCK = 4;
    public static final byte RF_DR = 3;
    public static final byte RF_PWR = 1;/* 2 bits */

    /* STATUS register bits */
    public static final byte RX_DR = 6;
    public static final byte TX_DS = 5;
    public static final byte MAX_RT = 4;
    public static final byte RX_P_NO = 1;/* 3 bits */
    public static final byte TX_FULL = 0;

    /* OBSERVE_TX register bits */
    public static final byte PLOS_CNT = 4;/* 4 bits */
    public static final byte ARC_CNT = 0; /* 4 bits */
    

    /* FIFO_STATUS register bits */
    public static final byte TX_REUSE = 6;
    public static final byte FIFO_FULL = 5;
    public static final byte TX_EMPTY = 4;
    public static final byte RX_FULL = 1;
    public static final byte RX_EMPTY = 0;

    /* dynamic length */
    public static final byte DPL_P0 = 0;
    public static final byte DPL_P1 = 1;
    public static final byte DPL_P2 = 2;
    public static final byte DPL_P3 = 3;
    public static final byte DPL_P4 = 4;
    public static final byte DPL_P5 = 5;

    /* Instruction Mnemonics */
    public static final byte R_REGISTER = (byte) 0x00; /* last 4 bits will indicate reg. address */
    public static final byte W_REGISTER = (byte) 0x20;/* last 4 bits will indicate reg. address */
    public static final byte REGISTER_MASK = (byte) 0x1F;
    public static final byte R_RX_PAYLOAD = (byte) 0x61;
    public static final byte W_TX_PAYLOAD = (byte) 0xA0;
    public static final byte FLUSH_TX = (byte) 0xE1;
    public static final byte FLUSH_RX = (byte) 0xE2;
    public static final byte REUSE_TX_PL = (byte) 0xE3;
    public static final byte ACTIVATE = (byte) 0x50;
    public static final byte R_RX_PL_WID = (byte) 0x60;
    public static final byte NOP = (byte) 0xFF;

}
