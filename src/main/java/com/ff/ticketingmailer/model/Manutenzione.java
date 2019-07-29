package com.ff.ticketingmailer.model;

import java.lang.reflect.Member;

public enum Manutenzione {
    ANNUALE("annuale", 12),
    TRIMESTRALE("trimestrale", 3),
    MENSILE("mensile", 1);

    private int mesi;
    private String descrizione;

    Manutenzione(String descrizione, int mesi){
        this.descrizione = descrizione;
        this.mesi = mesi;
    }

    public String getDescrizione(){
        return descrizione;
    }
    public int getMesi(){
        return mesi;
    }

    public static Manutenzione fromDescrione(String descrizione){
        for(Manutenzione manutenzione:Manutenzione.values()){
            if(manutenzione.getDescrizione().equals(descrizione)){
                return manutenzione;
            }
        }
        return null;
    }
}
