package util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import modelo.Libro;

/**
 * Created by pablo on 23/09/15.
 * Clase encargada de contener la estructura de paginacion para el listado de libros
 */
public class DatasourceLibros
{
    //Singleton pattern
    private static DatasourceLibros datasourceLibros = null;
    private List<Libro> data = null;
    private int SIZE = 0;

    TareasGenerales tareasGenerales = new TareasGenerales();
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    public static DatasourceLibros getInstance()
    {
        if (datasourceLibros == null)
        {
            datasourceLibros = new DatasourceLibros();
        }
        return datasourceLibros;
    }

    private DatasourceLibros(){
        data = new ArrayList<Libro>();
    }

    public List<Libro> getData() {
        return data;
    }

    public void setData(List<Libro> data) {
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
    public List<Libro> getData(int offset, int limit)
    {
        List<Libro> newList = new ArrayList<Libro>(limit);

        try {
            newList = tareasGenerales.buscarLibrosPaginados(variablesGlobales.getLibroBuscar(), offset, limit);
            Log.i("DSLibros", ">>>>>>>>>>> Tamaño lista libros DataSoruce: " + newList.size());
        }catch (Exception e){
            Log.e("DSLibros ", "xxx Error getData(): " + e.getMessage());
        }

        return newList;
    }

}
