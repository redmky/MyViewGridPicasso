package com.redmky.myviewgridpicasso;

/**
 * Created by redmky on 10/26/2015.
 */
public class ReviewsAdapter extends android.widget.ArrayAdapter<com.redmky.myviewgridpicasso.MovieByIdInfo> {
    public ReviewsAdapter(android.content.Context context, java.util.ArrayList<com.redmky.myviewgridpicasso.MovieByIdInfo> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        // Get the data item for this position
        MovieByIdInfo review = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = android.view.LayoutInflater.from(getContext()).inflate(com.redmky.myviewgridpicasso.R.layout.review_detail, parent, false);
        }
        // Lookup view for data population
        android.widget.TextView itemReview = (android.widget.TextView) convertView.findViewById(R.id.list_item_review_textview);

        // Populate the data into the template view using the data object
        itemReview.setText(review.key);

        // Return the completed view to render on screen
        return convertView;
    }
}