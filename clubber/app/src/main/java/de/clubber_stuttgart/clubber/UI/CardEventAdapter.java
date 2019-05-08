package de.clubber_stuttgart.clubber.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.clubber_stuttgart.clubber.R;

public class CardEventAdapter extends RecyclerView.Adapter<CardEventAdapter.ExampleViewHolder>{


    private ArrayList<Event> mCarditems;
    private Context context;
    final private String LOG = "CardEventAdapter";

    public CardEventAdapter(ArrayList<Event> cardList, Context context){
        mCarditems=cardList;
        this.context = context;
    }

    protected static class ExampleViewHolder extends RecyclerView.ViewHolder{
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private TextView textView5;
        private Button button;


        private ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.eventTitle);
            textView2=itemView.findViewById(R.id.musicDirection);
            textView3=itemView.findViewById(R.id.date);
            textView4=itemView.findViewById(R.id.startTime);
            textView5=itemView.findViewById(R.id.club);
            button = itemView.findViewById(R.id.linkToEvent);

        }

    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_event,viewGroup,false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        final Event currentItem= mCarditems.get(i);

        //exampleViewHolder.imageView.setImageResource(currentItem.getImageResource());
        //Sets values for corresponding nodes of the current Event object
        exampleViewHolder.textView1.setText(currentItem.name);
        exampleViewHolder.textView2.setText(currentItem.genre);
        exampleViewHolder.textView3.setText(currentItem.dte);
        exampleViewHolder.textView4.setText(currentItem.srttime);
        exampleViewHolder.textView5.setText(currentItem.club);
        //OnClickListener for each Button is added to reference URL saved in Event object
        //the context, which is needed to know from which activity the Intent should be started, gets passed when the constructor is called
        exampleViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(currentItem.btn);
                Intent toExternalSite =  new Intent(Intent.ACTION_VIEW, uri);
                //This flag is required to be able to use startActivity() outside of an Activity
                toExternalSite.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toExternalSite);
                Log.i(LOG, "External site " + uri + " has been clicked on");
            }
        });
    }

    @Override
    //defines how many items the ArrayList will hold
    public int getItemCount() {
        return mCarditems.size();
    }


}
