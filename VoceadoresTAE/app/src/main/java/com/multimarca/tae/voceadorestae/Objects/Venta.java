package com.multimarca.tae.voceadorestae.Objects;


public class Venta {

    private String numero;
    private String monto;
    private String fecha;
    private String carrier;
    private String folio;

    public Venta(String numero, String monto, String fecha, String folio, String carrier) {
        this.numero = numero;
        this.monto = monto;
        this.fecha = fecha;
        this.folio = folio;
        this.carrier = carrier;
    }

    public String getNumero() {
        return numero;
    }

    public String getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getFolio() {
        return folio;
    }
}
