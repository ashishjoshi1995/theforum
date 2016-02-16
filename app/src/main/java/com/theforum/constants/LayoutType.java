package com.theforum.constants;

/**
 * @author DEEPANKAR
 * @since 31-12-2015.
 */
public class LayoutType {

    /**
     * fragments constants identifiers (values) to be opened by containerActivity.
     */
    public static final int  NEW_OPINION_FRAGMENT = 100;
    public static final int NEW_TOPIC_FRAGMENT = 101;
    public static final int OPINIONS_FRAGMENT = 102;
    public static final int SETTINGS_FRAGMENT = 103;
    public static final int SORT_FRAGMENT = 104;
    public static final int STATS_FRAGMENT = 105;


    /**
     * other key values for passing of the variables (as bundles) between activities.
     */

    /*
        used when data is passed from topics item to opinion fragment
     */
    public static final String TOPIC_MODEL = "topic_model";
    public static final String OPINION_MODEL = "opinion_model";
}
