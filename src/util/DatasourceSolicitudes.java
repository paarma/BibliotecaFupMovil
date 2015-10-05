package util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import modelo.Solicitud;

/**
 * Created by pablo on 23/09/15.
 * Clase encargada de contener la estructura de paginacion para el listado de solicitudes
 * @author paarma80@gmail.com
 */
public class DatasourceSolicitudes
{
    //Singleton pattern
    private static DatasourceSolicitudes datasourceSolicitudes = null;
    private List<Solicitud> data = null;
    private int SIZE = 0;

    TareasGenerales tareasGenerales = new TareasGenerales();
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    public static DatasourceSolicitudes getInstance()
    {
        if (datasourceSolicitudes == null)
        {
            datasourceSolicitudes = new DatasourceSolicitudes();
        }
        return datasourceSolicitudes;
    }

    private DatasourceSolicitudes(){
        data = new ArrayList<Solicitud>();
    }

    public List<Solicitud> getData() {
        return data;
    }

    public void setData(List<Solicitud> data) {
        this.data = data;
    }

    public int getSize()
    {
        return SIZE;
    }

    public void setSIZE(int SIZE) {
        this.SIZE = SIZE;
    }

    /**
     * Returns the elements in a <b>NEW</b> list.
     */
    public List<Solicitud> getData(int offset, int limit)
    {
        List<Solicitud> newList = new ArrayList<Solicitud>(limit);

        try {
            newList = tareasGenerales.buscarSolicitudesPaginadas(variablesGlobales.getSolicitudBuscar(), offset, limit);
            Log.i("DSSolicitud", ">>>>>>>>>>> Tamaño lista solicitud DataSoruce: " + newList.size());
        }catch (Exception e){
            Log.e("DSSolicitud ", "xxx Error getData(): " + e.getMessage());
        }

        return newList;
    }

}
