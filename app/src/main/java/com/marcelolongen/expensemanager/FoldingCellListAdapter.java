package com.marcelolongen.expensemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marcelolongen.expensemanager.R;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<Item> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public FoldingCellListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from((getContext()));
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.price = cell.findViewById(R.id.title_price);
            viewHolder.price2 = cell.findViewById(R.id.head_image_right_text);
            viewHolder.time = cell.findViewById(R.id.title_time_label);
            viewHolder.date = cell.findViewById(R.id.title_date_label);
            viewHolder.date2 = cell.findViewById(R.id.dateTopLeft);
            viewHolder.description = cell.findViewById(R.id.description);
            viewHolder.description2 = cell.findViewById(R.id.description2);
            viewHolder.category = cell.findViewById(R.id.title_pledge);
            viewHolder.category2 = cell.findViewById(R.id.head_image_center_text);
            viewHolder.contentRequestBtn = cell.findViewById(R.id.deleteEntryButton);
            viewHolder.layout = cell.findViewById(R.id.leftTitle);
            viewHolder.layout2 = cell.findViewById(R.id.insideTitle);
            viewHolder.headImage = cell.findViewById(R.id.head_image);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.price.setText("$" + String.format("%.2f",(item.getValue() * Overview.getCurrentRate())));
        viewHolder.price2.setText("$" + String.format("%.2f",(item.getValue() * Overview.getCurrentRate())));
        viewHolder.time.setText(item.getCategory());
        viewHolder.date.setText(item.getMonth()+"/"+item.getDay());
        viewHolder.date2.setText(item.getMonth()+"/"+item.getDay());
        viewHolder.description.setText(item.getDescription());
        viewHolder.category.setText(item.getCategory());
        viewHolder.category2.setText(item.getCategory());
        viewHolder.description2.setText(item.getDescription());

        String[] categories = {"Food", "Bills", "Housing", "Health", "Social Life", "Apparel", "Beauty", "Education", "Other"};
        if (item.getCategory().equals("Food")) {
            viewHolder.layout.setBackgroundColor(rgb("#f1c40f"));
            viewHolder.layout2.setBackgroundColor(rgb("#f1c40f"));
            viewHolder.headImage.setImageResource(R.drawable.food2);
        } else if (item.getCategory().equals("Bills")) {
            viewHolder.layout.setBackgroundColor(rgb("#e74c3c"));
            viewHolder.layout2.setBackgroundColor(rgb("#e74c3c"));
            viewHolder.headImage.setImageResource(R.drawable.bills2);
        } else if (item.getCategory().equals("Housing")) {
            viewHolder.layout.setBackgroundColor(rgb("#9E9E9E"));
            viewHolder.layout2.setBackgroundColor(rgb("#9E9E9E"));
            viewHolder.headImage.setImageResource(R.drawable.housing);

        } else if (item.getCategory().equals("Health")) {
            viewHolder.layout.setBackgroundColor(rgb("#2ecc71"));
            viewHolder.layout2.setBackgroundColor(rgb("#2ecc71"));
            viewHolder.headImage.setImageResource(R.drawable.health2);
        } else if (item.getCategory().equals("Beauty")) {
            viewHolder.layout.setBackgroundColor(rgb("#E91E63"));
            viewHolder.layout2.setBackgroundColor(rgb("#E91E63"));
            viewHolder.headImage.setImageResource(R.drawable.beauty2);
        } else if (item.getCategory().equals("Social Life")) {
            viewHolder.headImage.setImageResource(R.drawable.party2);
            viewHolder.layout.setBackgroundColor(rgb("#9C27B0"));
            viewHolder.layout2.setBackgroundColor(rgb("#9C27B0"));
        } else if (item.getCategory().equals("Apparel")) {
            viewHolder.headImage.setImageResource(R.drawable.housing2);
            viewHolder.layout.setBackgroundColor(rgb("#009688"));
            viewHolder.layout2.setBackgroundColor(rgb("#009688"));
        }


        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
        }

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView price;
        TextView price2;
        TextView contentRequestBtn;
        TextView category;
        TextView category2;
        TextView description;
        TextView description2;
        TextView date;
        TextView date2;
        TextView time;
        RelativeLayout layout;
        RelativeLayout layout2;
        ImageView headImage;
    }
}
