package com.d.donorsapp;

public interface SmsListener {
    public void messageReceived(String messageText, String messageNumber);
}
