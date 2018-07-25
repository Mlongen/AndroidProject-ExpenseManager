package com.example.marcelolongen.expensemanager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAlign;
import com.anychart.anychart.LegendLayout;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.chart.common.Event;
import com.anychart.anychart.chart.common.ListenersInterface;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {
    private Database db;
    private BoomMenuButton bmb;
    private Double[] sum = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    private DatabaseReference root;
    private DatabaseReference user;
    private static final String ARG_USER_NAME = "userName";
    private String userName;
    private AnyChartView anyChartView;
    private ViewHolder add;
    private TextView highestSpending;
    private TextView highestName;
    private TextView totalSum;
    private View thisView;

    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance(String userName) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_NAME, userName);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public View getThisView() {
        return thisView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Database.getInstance();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_overview, container, false);
//        db.readContentsFromFile(userName);


        final ArrayList<Class> classes = new ArrayList<>();
        String[] names = {"Detailed list", "Settings"};


        bmb = (BoomMenuButton) thisView.findViewById(R.id.bmb);
        assert bmb != null;



        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.listing_option);
        images.add(R.drawable.settings);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
        bmb.setButtonTopMargin(1000);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(images.get(i))
                    .normalText(names[i])
                    .shadowEffect(true)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
//
//                            Fragment newDetail = DetailsFragment.newInstance(userName);
//                            getFragmentManager().beginTransaction().replace(R.id.fill_horizontal, newDetail).commit();
//                            Fragment newInstance = DetailsFragment.newInstance(userName);
//                            FragmentManager fragmentManager = getFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.add(R.id.fragmentContainer, newInstance)
//                                    .addToBackStack(null)
//                                    .commit(); // just do it

//
//                                Intent intent = new Intent(getApplicationContext(), classes.get(index));
//                                if (index == 0) {
//                                    intent.putExtra("user", userName);
//                                }
//                                startActivity(intent);
                        }
                    })
                    ;

            bmb.addBuilder(builder);


        }

        return thisView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData(thisView);
    }

    public void updateData(View view) {
        String[] categories = {"Food", "Bills", "Housing", "Health", "Social Life", "Apparel", "Beauty", "Education", "Other"};

        @SuppressLint("SimpleDateFormat") String thisMonth = new SimpleDateFormat("M").format(Calendar.getInstance().getTime());

        //clearing data
        for (int i = 0; i < sum.length; i++) {
            sum[i] = 0.0;
        }
        for (int i = 0; i< db.getItemObjects().size(); i++ ) {
            for (int j = 0; j < categories.length;j++) {
                if (db.getItemObjects().get(i).getCategory().equals(categories[j])&& db.getItemObjects().get(i).getMonth().toString().equals(thisMonth) ) {
                    sum[j] += db.getItemObjects().get(i).getValue();
                }
            }
        }


        TextView highestSpending = view.findViewById(R.id.highestCategory);
        TextView highestName = view.findViewById(R.id.highestName);

        TextView totalSum = view.findViewById(R.id.totalSum);
        double sumTotal = sum[0] + sum[1] + sum[2] + sum[3] + sum[4] + sum[5] + sum[6] + sum[7] + sum[8];
        totalSum.setText("CAD: " + String.valueOf(sumTotal) + "0");
        double highestValue = sum[0];
        int highestIndex = 0;
        for (int i = 0; i < sum.length; i++) {
            if (sum[i] > highestValue) {
                highestValue = sum[i];
                highestIndex = i;
            }
        }
        highestSpending.setText("CAD: " + String.valueOf(highestValue) + "0");
        highestName.setText(categories[highestIndex]);
    }



    private void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}
