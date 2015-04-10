package com.clicky.semarnat.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 *
 * Created by Clicky on 4/2/15.
 *
 */
@ParseClassName("Documentos")
public class Documentos extends ParseObject {

    public String getTipo() {
        return getString("Tipo");
    }

    public Number getFolioProg() {
        return getNumber("Folio_prog");
    }

    public String getFolioAuto() {
        return getString("Folio_auto");
    }

    public Date getFechaExp() {
        return getDate("Fecha_exp");
    }

    public Date getFechaVen() {
        return getDate("Fecha_ven");
    }

    public ParseObject getTitular() {
        return getParseObject("Titular");
    }

    public ParseObject getTransportista() {
        return getParseObject("Transportista");
    }

    public ParseObject getCAT() {
        return getParseObject("CAT");
    }

    public String getObservaciones() {
        return getString("Observaciones");
    }

    public String getRemitenteTipo() {
        return getString("Remitente_tipo");
    }

    public String getRemitenteCat() {
        return getString("remit_cat");
    }

    public String getRemitenteTitular() {
        return getString("remit_titular");
    }

    public static ParseQuery<Documentos> getQuery() {
        return ParseQuery.getQuery(Documentos.class);
    }

}
