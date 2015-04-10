package com.clicky.semarnat.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 *
 * Created by Clicky on 4/2/15.
 *
 */
@ParseClassName("Materia_Prima")
public class MateriaPrima extends ParseObject {

    public String getTipo() {
        return getString("Tipo");
    }

    public Number getCantidad() {
        return getNumber("numer_cantidad");
    }

    public String getDescripcion() {
        return getString("descripcion");
    }

    public String getVolumen() {
        return getString("volumen_peso_amp");
    }

    public String getUnidad() {
        return getString("unidad_medida");
    }

    public Number getSaldoDisponible() {
        return getNumber("saldo_disp");
    }

    public Number getSaldoRestante() {
        return getNumber("saldo_rest");
    }

    public Documentos getDocumento() {
        return (Documentos)getParseObject("Documentos");
    }

    public static ParseQuery<MateriaPrima> getQuery() {
        return ParseQuery.getQuery(MateriaPrima.class);
    }

}
