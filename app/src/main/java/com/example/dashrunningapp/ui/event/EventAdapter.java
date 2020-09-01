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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private List<EventDTO> mDataset;
    private Context Context;
    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textName, textCity, textDate;

        public MyViewHolder(View v) {
            super(v);
            textName = v.findViewById(R.id.txtName);
            textCity = v.findViewById(R.id.txtCity);
            textDate = v.findViewById(R.id.txtDate);
        }

        void setDetails(EventDTO event) {
            textName.setText(event.name);
            textCity.setText(event.city);
            textDate.setText(event.date);
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
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final EventDTO event = mDataset.get(position);
        holder.setDetails(event);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.url));
                Context.startActivity(browserIntent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}