package com.marcosdiez.extratocartao.glue;

import android.util.Log;

import com.marcosdiez.extratocartao.ParsingSmsException;
import com.marcosdiez.extratocartao.bank.BancoDoBrasil;
import com.marcosdiez.extratocartao.bank.BaseBank;
import com.marcosdiez.extratocartao.bank.Bradesco;
import com.marcosdiez.extratocartao.bank.Itau;
import com.marcosdiez.extratocartao.bank.ItauPersonnalite;
import com.marcosdiez.extratocartao.bank.ItauPersonnaliteSaque;
import com.marcosdiez.extratocartao.bank.Santander;
import com.marcosdiez.extratocartao.datamodel.Purchase;
import com.marcosdiez.extratocartao.sms.BankSms;
import com.marcosdiez.extratocartao.sms.SMSData;

/**
 * Created by Marcos on 2015-08-02.
 */
public class SmsParser {
    private static final String TAG = "EC-SmsParser";

    private static BaseBank[] bankList = new BaseBank[]{
            new BancoDoBrasil(),
            new Bradesco(),
            new Itau(),
            new ItauPersonnalite(),
            new ItauPersonnaliteSaque(),
            new Santander()
    };

    public static BankSms parseSms(SMSData theSMS) throws ParsingSmsException {
        for (BaseBank thisBank : bankList) {
            Log.d(TAG, "Checking if the SMS is from " + thisBank.getClass().getName());
            if (thisBank.isSmsFromBank(theSMS)) {
                return thisBank.getBankSms();
            }
        }
        return null;
    }

    public static Purchase parseSmsPurchase(SMSData theSms) throws ParsingSmsException {
        BankSms theBankSms = parseSms(theSms);
        if (theBankSms != null) {
            try {
                return new Purchase(theBankSms);
            } catch (Exception e) {
                throw new ParsingSmsException(theSms.getBody(), e);
            }
        }
        return null;
    }

}


