package com.webileapps.navdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by pablo on 17/07/15.
 */
public class CustomDialogMultas extends DialogFragment {

    EditText editTextValorSugerido;
    Integer numeroDiasMora = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_multa, null);
        editTextValorSugerido = (EditText) view.findViewById(R.id.editTextValorSugerido);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        //Se reciben los parametros enviados desde la pagina invocante
        numeroDiasMora = getArguments().getInt("diasMora");

        editTextValorSugerido.setText("XXX"+numeroDiasMora);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setPositiveButton(R.string.aceptar,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "Yes",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_multa, container);
        editTextValorSugerido = (EditText) view.findViewById(R.id.editTextValorSugerido);
        editTextValorSugerido.setText("XXX");

        return view;
    }*/


}