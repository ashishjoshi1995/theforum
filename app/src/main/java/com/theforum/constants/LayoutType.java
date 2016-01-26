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
    public static final int NOTIFICATION_FRAGMENT = 104;
    public static final int SORT_FRAGMENT = 105;

    /**
     * constants identifiers (key values) for sorting the topics which are shown
     * to the user.
     */

    public static final int SORT_BASIS_CREATED_BY_ME = 200;
    public static final int SORT_BASIS_LATEST = 201;
    public static final int SORT_BASIS_MOST_RENEWAL = 202;
    public static final int SORT_BASIS_LEAST_RENEWAL = 203;
    public static final int SORT_BASIS_MOST_POPULAR = 204;

    /**
     * joshi please update its use.
     */
    public static final int ADD_MY_TOPIC = 300;
    public static final int ADD_TOPICS_ALL = 301;



    /**
     * other key values for passing of the variables (as bundles) between activities.
     */

    /*
        used when data is passed from topics item to opinion fragment
     */
    public static final String TOPIC_MODEL = "topic_model";
    public static final String OPINION_MODEL = "opinion_model";
}