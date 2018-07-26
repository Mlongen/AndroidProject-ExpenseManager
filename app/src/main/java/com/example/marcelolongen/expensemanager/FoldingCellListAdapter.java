package com.example.marcelolongen.expensemanager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import static android.graphics.Color.BLACK;

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
        viewHolder.price.setText("$" +(item.getValue() * Overview.getCurrentRate()) + "0");
        viewHolder.price2.setText("$" + (item.getValue() * Overview.getCurrentRate()) + "0");
        viewHolder.time.setText(item.getCategory());
        viewHolder.date.setText(item.getMonth()+"/"+item.getDay());
        viewHolder.date2.setText(item.getMonth()+"/"+item.getDay());
        viewHolder.description.setText(item.getDescription());
        viewHolder.category.setText(item.getCategory());
        viewHolder.category2.setText(item.getCategory());
        viewHolder.description2.setText(item.getDescription());

        if (item.getCategory().equals("Food")) {
            viewHolder.layout.setBackgroundColor(0xFFFFA500);
            viewHolder.layout2.setBackgroundColor(0xFFFFA500);
            viewHolder.headImage.setImageResource(R.drawable.food);
        } else if (item.getCategory().equals("Bills")) {
            viewHolder.layout.setBackgroundColor(Color.RED);
            viewHolder.layout2.setBackgroundColor(Color.RED);
            viewHolder.headImage.setImageResource(R.drawable.bills);
        } else if (item.getCategory().equals("Housing")) {
            viewHolder.layout.setBackgroundColor(Color.GRAY);
            viewHolder.layout2.setBackgroundColor(Color.GRAY);
            viewHolder.headImage.setImageResource(R.drawable.housing);

        } else if (item.getCategory().equals("Health")) {
            viewHolder.layout.setBackgroundColor(0xFF008000);
            viewHolder.layout2.setBackgroundColor(0xFF008000);
            viewHolder.headImage.setImageResource(R.drawable.health);
        } else if (item.getCategory().equals("Beauty")) {
            viewHolder.layout.setBackgroundColor(0xFF800080);
            viewHolder.layout2.setBackgroundColor(0xFF800080);
            viewHolder.headImage.setImageResource(R.drawable.beauty);
        } else if (item.getCategory().equals("Social Life")) {
            viewHolder.headImage.setImageResource(R.drawable.party);
            viewHolder.layout.setBackgroundColor(0xFFFF80FF);
            viewHolder.layout2.setBackgroundColor(0xFFFF80FF);
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
