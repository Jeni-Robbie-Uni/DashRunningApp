package com.example.dashrunningapp.ui.event;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dashrunningapp.R;
import com.example.dashrunningapp.models.EventDTO;

import java.util.List;

//fills the recyler views accordingly
//followed tutorial Reference:https://medium.com/@droidbyme/android-cardview-with-recyclerview-90cfeda6a4d4https://medium.com/@droidbyme/android-cardview-with-recyclerview-90cfeda6a4d4
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private List<EventDTO> mDataset;
    private Context Context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        // event properties to be displayed in recylcer item view
        public TextView textName, textCity, textDate;

        //Make reference to item view elements
        public MyViewHolder(View v) {
            super(v);
            textName = v.findViewById(R.id.txtName);
            textCity = v.findViewById(R.id.txtCity);
            textDate = v.findViewById(R.id.txtDate);
        }
        //set text views
        void setDetails(EventDTO event) {
            textName.setText(event.getName());
            textCity.setText(event.getCity());
            textDate.setText(event.getDate());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(Context context, List<EventDTO> myDataset) {
        mDataset = myDataset;
        Context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View view = LayoutInflater.from(Context).inflate(R.layout.recycler_view_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // get element from your dataset at this position
        // replace the contents of the view with that element
        final EventDTO event = mDataset.get(position);

        //set recyler item text views to event details
        holder.setDetails(event);

        //when view is clicked open event url
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl()));
                Context.startActivity(browserIntent);
            }
        });
    }

    // Return the size of your dataset called by layout manager to generate x amount of cards
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}