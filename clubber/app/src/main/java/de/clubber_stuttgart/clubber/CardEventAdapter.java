package de.clubber_stuttgart.clubber;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CardEventAdapter extends RecyclerView.Adapter<CardEventAdapter.ExampleViewHolder>{

    private ArrayList<Event> mCarditems;

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_event,viewGroup,false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        Event currentItem= mCarditems.get(i);

        // exampleViewHolder.imageView.setImageResource(currentItem.getImageResource());
        exampleViewHolder.textView1.setText(currentItem.name);
        exampleViewHolder.textView2.setText(currentItem.genre);
        exampleViewHolder.textView3.setText(currentItem.dte);
        exampleViewHolder.textView4.setText(currentItem.srttime);
        exampleViewHolder.textView5.setText(currentItem.club);
    }

    @Override
    //definiert wieviele Items eine Liste haben wird später
    public int getItemCount() {
        return mCarditems.size();
    }

    public CardEventAdapter(ArrayList<Event> cardList){
        mCarditems=cardList;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.eventTitle);
            textView2=itemView.findViewById(R.id.musicDirection);
            textView3=itemView.findViewById(R.id.date);
            textView4=itemView.findViewById(R.id.startTime);
            textView5=itemView.findViewById(R.id.club);
        }
    }


}
