package com.multimarca.tae.voceadorestae.Objects;


public class Traspaso {

    private long _id;
    private String _Fecha;
    private String _Cliente;
    private String _Valor;
    private String _Banco;
    private String _Referencia;
    private String _Auth;
    private String _Desc;
    private String _Supe;


    public Traspaso(long id, String fecha, String cliente, String valor, String banco, String referencia, String auth, String desc, String supe) {
        _id = id;
        _Fecha = fecha;
        _Cliente = cliente;
        _Valor = valor;
        _Banco = banco;
        _Referencia = referencia;
        _Auth = auth;
        _Desc = desc;
        _Supe = supe;
    }

    public String get_Fecha() {
        return _Fecha;
    }

    public String get_Valor() {
        return _Valor;
    }

    public String get_Banco() {
        return _Banco;
    }

    public String get_Referencia() {
        return _Referencia;
    }

    public String get_Auth() {
        return _Auth;
    }

    public String get_Desc() {
        return _Desc;
    }

    public String get_Supe() {
        return _Supe;
    }

    public String get_Cliente() {
        return _Cliente;
    }
}
