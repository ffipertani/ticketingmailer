package com.ff.ticketingmailer.model;

import java.util.Date;

public class MaintenanceRemainder {
    private String id;
    private String marca;
    private String modello;
    private String manutenzione;
    private Date dataInstallazione;
    private Date dataEmail;
    private Date dataManutenzione;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public String getManutenzione() {
        return manutenzione;
    }

    public void setManutenzione(String manutenzione) {
        this.manutenzione = manutenzione;
    }

    public Date getDataInstallazione() {
        return dataInstallazione;
    }

    public void setDataInstallazione(Date dataInstallazione) {
        this.dataInstallazione = dataInstallazione;
    }
    public Date getDataEmail() {
        return dataEmail;
    }

    public void setDataEmail(Date dataEmail) {
        this.dataEmail = dataEmail;
    }

    public Date getDataManutenzione() {
        return dataManutenzione;
    }

    public void setDataManutenzione(Date dataManutenzione) {
        this.dataManutenzione = dataManutenzione;
    }
}
