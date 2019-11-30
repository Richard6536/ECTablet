package com.example.richard.ectablet.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.richard.ectablet.R;

import java.util.List;

public class CardviewAutosAdapter  extends RecyclerView.Adapter<CardviewAutosAdapter.CardViewHolder> {
    private List<SpinnerAdapter> listaAutos;
    SpinnerAdapter auto;

    private OnItemClickListener onItemClickListener; // Global scope
    public interface OnItemClickListener {
        void onItemClicked(int position, int itemPosition, SpinnerAdapter auto);
    }

    private Context mCtx;

    //getting the context and product list with constructor
    public CardviewAutosAdapter(Context mCtx, List<SpinnerAdapter> lista, CardviewAutosAdapter.OnItemClickListener onItemClickListener) {
        this.mCtx = mCtx;
        this.listaAutos = lista;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout._cardview_lista_autos, null);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        auto = listaAutos.get(position);
        holder.txtNombreDelVehiculo.setText(auto.getNombre());
        holder.txtPatenteDelVehiculo.setText(auto.getPatente());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SpinnerAdapter autoSeleccionado = listaAutos.get(position);
                onItemClickListener.onItemClicked(position, 0, autoSeleccionado);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAutos.size();
    }
    class CardViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombreDelVehiculo, txtPatenteDelVehiculo;

        public CardViewHolder(View itemView) {
            super(itemView);

            txtNombreDelVehiculo = itemView.findViewById(R.id.txtNombreDelVehiculo);
            txtPatenteDelVehiculo = itemView.findViewById(R.id.txtPatenteDelVehiculo);

        }
    }
}
