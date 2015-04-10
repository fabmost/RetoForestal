package com.clicky.semarnat.data;

/**
 *
 * Created by Clicky on 3/23/15.
 *
 */
public class Folio {

    private String folio;
    private String fecha;

    public Folio(String folio){
        this.folio = folio;
        this.fecha = "12/05/2015";
    }

    public String getFolio(){
        return folio;
    }

    public String getFecha(){
        return fecha;
    }
}
