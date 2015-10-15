package com.bluestone.app.core;

/**
 * @author Rahul Agrawal
 *         Date: 1/24/13
 */

/**
 * Just a marker interface for now
 */
public interface ProductionAlert {

    void send(String message);

    void send(String message, Throwable throwable);
}
