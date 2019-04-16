package de.clubber_stuttgart.clubber;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CardClubAdapter extends RecyclerView.Adapter<CardClubAdapter.ExampleViewHolder> {

    private ArrayList<Club> clubList;

    //Contructor to get the Data from ArrayList into the Adapter
    public CardClubAdapter(ArrayList<Club> clubList){
        this.clubList=clubList;
    }

    //ViewHolder for Adapter
    protected static class ExampleViewHolder extends RecyclerView.ViewHolder{
        private TextView clubName;
        private TextView clubAdr;
        private TextView clubTel;
        private TextView clubWeb;

        private ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            // create references to the Views
            clubName=itemView.findViewById(R.id.club_name);
            clubAdr=itemView.findViewById(R.id.club_adrs);
            clubTel=itemView.findViewById(R.id.club_tel);
            clubWeb=itemView.findViewById(R.id.club_web);
        }
    }

    //pass the layout to the Adapter
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_club,viewGroup,false);
        //create Viewholder
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    //Method where you can pass values to the Views
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        Club currentItem = clubList.get(i);
        exampleViewHolder.clubName.setText(currentItem.name);
        exampleViewHolder.clubAdr.setText(currentItem.adrs);
        exampleViewHolder.clubTel.setText(currentItem.tel);
        exampleViewHolder.clubWeb.setText(currentItem.web);

    }

    //defines how many items the list will hold
    @Override
    public int getItemCount() {
        return clubList.size();
    }




}
