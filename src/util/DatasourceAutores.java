package util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import modelo.Autor;

/**
 * Created by pablo on 23/09/15.
 * Clase encargada de contener la estructura de paginacion para el listado de autores
 */
public class DatasourceAutores
{
    //Singleton pattern
    private static DatasourceAutores datasourceAutores = null;
    private List<Autor> data = null;
    private int SIZE = 0;

    TareasGenerales tareasGenerales = new TareasGenerales();
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    public static DatasourceAutores getInstance()
    {
        if (datasourceAutores == null)
        {
            datasourceAutores = new DatasourceAutores();
        }
        return datasourceAutores;
    }

    private DatasourceAutores(){
        data = new ArrayList<Autor>();
    }

    public List<Autor> getData() {
        return data;
    }

    public void setData(List<Autor> data) {
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
    public List<Autor> getData(int offset, int limit)
    {
        List<Autor> newList = new ArrayList<Autor>(limit);

        try {
            newList = tareasGenerales.listarAutoresPaginados(variablesGlobales.getAutorBuscar(), offset, limit);
            Log.i("DSAutores", ">>>>>>>>>>> Tamaño lista autores DataSoruce: " + newList.size());
        }catch (Exception e){
            Log.e("DSAutores ", "xxx Error getData(): " + e.getMessage());
        }

        return newList;
    }

}
