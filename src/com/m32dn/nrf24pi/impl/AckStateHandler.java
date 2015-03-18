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
import com.m32dn.nrf24pi.enums.AckState;
import java.util.concurrent.Callable;

/**
 *
 * @author majo
 */
class AckStateHandler implements Callable<AckState> {

    private Status status;
    private final ControllerImpl controller;

    public AckStateHandler(ControllerImpl controller) {
        this.controller = controller;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public AckState call() throws Exception {
        AckState state;
        if(!status.isMaxRetriesExceeded() && status.isTxFifoReady()){
            state =  AckState.OK;
        }else{
            state = AckState.UNDELIVERED;
        }
        controller.handleAckState(state);
        return state;
    }

}
