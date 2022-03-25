package com.bcit.project_genuity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * A simple pager adapter that represents 5 ExampleFragment objects, in
 * sequence.
 */
class ViewPagerEventsHome extends FragmentStateAdapter {

    /**
     * The number of pages to show
     */
    Event[] events;

    public ViewPagerEventsHome(FragmentActivity fa, Event[] events) {
        super(fa);
        this.events = events;
    }

    @Override
    public Fragment createFragment(int position) {
        return HomeEventsFragment.newInstance(events[position]); //you will get an error here this is just a template
    }

    @Override
    public int getItemCount() {
        return events.length;
    }
}