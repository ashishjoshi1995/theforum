package com.theforum.utils.enums;

import java.io.Serializable;

/**
 * @author DEEPANKAR
 * @since 21-01-2016.
 */

public enum VoteStatus implements Serializable{

    /**
     * if an opinion is upvoted
     */
    UPVOTED,

    /**
     * if an opinion is downvoted
     */
    DOWNVOTED,

    /**
     * none of the actions is performed
     */
    NONE
}
