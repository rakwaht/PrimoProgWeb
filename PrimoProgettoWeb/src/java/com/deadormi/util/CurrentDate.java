/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.deadormi.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;


/**
 *
 * @author francesco
 */
public class CurrentDate {
   
     static Logger  log = Logger.getLogger(CurrentDate.class);
    
    public static String getCurrentDate () {
        Date date = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd 'alle' HH:mm:ss");
        String res = formato.format(date);
        return res;
    }
    
}
