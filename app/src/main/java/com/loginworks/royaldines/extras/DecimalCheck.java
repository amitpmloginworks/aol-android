package com.loginworks.royaldines.extras;

import java.text.DecimalFormat;

/**
 * Created by ujjwal on 4/26/2017.
 */

public class DecimalCheck {

    public static String decimalFormat(double val) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(val);
    }


}
