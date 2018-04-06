package com.example.rizk.firebaseexamplelogin;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.TripViewHolder> {

    Intent go;
    FirebaseDatabase mDatabase;

    public static class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        CardView cv;
        TextView from, date;
        TextView name, to, time, note;
        Button del;


        TripViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            del = itemView.findViewById(R.id.deleteBtn);
            from = (TextView) itemView.findViewById(R.id.pFrom);
            to = (TextView) itemView.findViewById(R.id.pTo);
            time = (TextView) itemView.findViewById(R.id.pTime);
            date = (TextView) itemView.findViewById(R.id.pdate);
            note = itemView.findViewById(R.id.editTextMulti);
            name = itemView.findViewById(R.id.nameview);

        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    List<SaveData> trip;

    RvAdapter(List<SaveData> trip) {
        this.trip = trip;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv, viewGroup, false);
        TripViewHolder pvh = new TripViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final TripViewHolder tripViewHolder, final int i) {

        
            tripViewHolder.from.setText(trip.get(i).getFrom());
            tripViewHolder.to.setText(trip.get(i).getTo());
            tripViewHolder.date.setText(trip.get(i).getDate());
            tripViewHolder.time.setText(trip.get(i).getTime());
            tripViewHolder.name.setText(trip.get(i).getName());
            tripViewHolder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    mDatabase = FirebaseDatabase.getInstance();
                    mDatabase.getReference("Trips/" + user.getUid()).child(trip.get(i).getKey()).removeValue();
                    trip.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, trip.size());
                    notifyDataSetChanged();
                }
            });
            tripViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    go = new Intent(v.getContext(), EditDeleteAct.class);
                    go.putExtra("from", trip.get(i).getFrom());
                    go.putExtra("to", trip.get(i).getTo());
                    go.putExtra("date", trip.get(i).getDate());
                    go.putExtra("name", trip.get(i).getName());
                    go.putExtra("time", trip.get(i).getTime());
                    go.putExtra("note", trip.get(i).getNotes());
                    go.putExtra("id", trip.get(i).getKey());
                    go.putExtra("status",trip.get(i).getStatus());
                    v.getContext().startActivity(go);

                }
            });

    }

    @Override
    public int getItemCount() {
        return trip.size();
    }

}
