package com.bcit.project_genuity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class SearchEventsAdapter extends RecyclerView.Adapter<SearchEventsAdapter.ViewHolder> {

    private Event[] events;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewDate;
        private Event currEvent;

        public ViewHolder(View view) {
            super(view);

            textViewName = view.findViewById(R.id.textView_event_name_search); //error here should be expected, this is a template
            textViewDate = view.findViewById(R.id.textView_event_date_search);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), EventPageActivity.class);
                    intent.putExtra("Event", currEvent);
                    view.getContext().startActivity(intent);
                }
            });
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public TextView getTextViewName() {
            return textViewName;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public SearchEventsAdapter(Event[] dataSet) {
        events = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_card, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.currEvent = events[position];
        viewHolder.getTextViewName().setText(events[position].getName());
        viewHolder.getTextViewDate().setText(events[position].getDatetime());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.length;
    }
}