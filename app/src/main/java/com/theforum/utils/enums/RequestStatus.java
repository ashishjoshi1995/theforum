package com.theforum.utils.enums;

/**
 * @author DEEPANKAR
 * @since 20-01-2016.
 */

public enum RequestStatus {
    /**
     * when the assigned task has been completed. Now the data is available and can
     * be used to update the UI. Data received must be utilized before the next request
     * or will be lost.
     */
    COMPLETED,

    /**
     * the request is currently executing and any request coming at this time will
     * be simply discarded.
     */
    EXECUTING,

    /**
     *  data received by request is consumed and system is ready for another
     *  request.
     */
    IDLE

}
