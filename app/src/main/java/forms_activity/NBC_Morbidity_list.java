package forms_activity;

import java.util.ArrayList;
 import java.util.List;
 import android.app.*;
import android.content.Intent;
 import android.location.Location;
 import android.view.KeyEvent;
 import android.os.Bundle;
 import android.view.View;
 import android.view.MotionEvent;
 import android.view.ViewGroup;
 import android.view.LayoutInflater;
 import android.widget.LinearLayout;
 import android.widget.TextView;
 import android.widget.Button;
 import android.widget.ImageButton;
 import Common.*;
 import forms_datamodel.NBC_Morbidity_DataModel;

import android.widget.EditText;
 import android.view.WindowManager;
 import Utility.*;
 import java.util.Calendar;
 import android.widget.DatePicker;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.recyclerview.widget.DefaultItemAnimator;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;
 import androidx.constraintlayout.widget.ConstraintLayout;

import org.icddrb.champs_lc_dss_search_member.R;

//This activity is not used any where
public class NBC_Morbidity_list extends AppCompatActivity {
    boolean networkAvailable=false;
    Location currentLocation; 
    double currentLatitude,currentLongitude; 
    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override 
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) 
             { return false; }
        else { return true;  }
    }
    String VariableID;
    private int mDay;
    private int mMonth;
    private int mYear;
     final int DATE_DIALOG = 1;
     final int TIME_DIALOG = 2;

    Connection C;
    Global g;
    private List<NBC_Morbidity_DataModel> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter mAdapter;
     String TableName;

    TextView lblHeading;
    TextView lblTotal;
    Button btnAdd;
    ImageButton btnSearch;
    EditText txtSearch;
    EditText dtpFDate;
    EditText dtpTDate;

     String STARTTIME = "";
     String DEVICEID  = "";

     String ENTRYUSER = "";

    ConstraintLayout ll_no_data;
     String MORBIID = "";
     String PGN = "";
     String CHSL = "";

 public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
   try
     {
         setContentView(R.layout.nbc_morbidity_list);
         getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
         C = new Connection(this);
         g = Global.getInstance();
         STARTTIME = g.CurrentTime24();

         DEVICEID = MySharedPreferences.getValue(this, "deviceid");
         ENTRYUSER = MySharedPreferences.getValue(this, "userid");
         TableName = "NBC_Morbidity";
         lblHeading = (TextView)findViewById(R.id.lblHeading);
         lblTotal = (TextView)findViewById(R.id.lblTotal);

         ImageButton cmdBack = (ImageButton) findViewById(R.id.cmdBack);
         cmdBack.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 finish();
             }});

         btnAdd = (Button) findViewById(R.id.btnAdd);
         btnAdd.setOnClickListener(new View.OnClickListener() {

             public void onClick(View view) {
                         Bundle IDbundle = new Bundle();
                         IDbundle.putString("MorbiID", "");
                         IDbundle.putString("PGN", "");
                         IDbundle.putString("ChSl", "");
                         Intent intent = new Intent(getApplicationContext(), NBC_Morbidity.class);
                         intent.putExtras(IDbundle);
                         startActivityForResult(intent, 1);

             }});
         txtSearch = (EditText)findViewById(R.id.txtSearch);

         btnSearch = (ImageButton) findViewById(R.id.btnSearch);
         btnSearch.setOnClickListener(new View.OnClickListener() {

             public void onClick(View view) {
                         DataSearch(txtSearch.getText().toString());

             }});


        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        ll_no_data = findViewById(R.id.ll_no_data);
        mAdapter = new DataAdapter(dataList);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        dtpFDate = (EditText) findViewById(R.id.dtpFDate);
        dtpTDate = (EditText) findViewById(R.id.dtpTDate);
        dtpFDate.setText(Global.DateNowDMY());
        dtpTDate.setText(Global.DateNowDMY());
        dtpFDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (dtpFDate.getRight() - dtpFDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        VariableID = "dtpFDate";
                        showDialog(DATE_DIALOG);
                        return true;
                    }
                }
                return false;
            }
        });
        
        dtpTDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (dtpTDate.getRight() - dtpTDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        VariableID = "dtpTDate";
                        showDialog(DATE_DIALOG);
                        return true;
                    }
                }
                return false;
            }
        });


        DataSearch(txtSearch.getText().toString());


     }
     catch(Exception  e)
     {
         Connection.MessageBox(NBC_Morbidity_list.this, e.getMessage());
         return;
     }
 }
 
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if (resultCode == Activity.RESULT_CANCELED) {
         //Write your code if there's no result
     } else {
         DataSearch(txtSearch.getText().toString());
     }
 }

 private void DataSearch(String SearchText)
     {
       try
        {
     
           NBC_Morbidity_DataModel d = new NBC_Morbidity_DataModel();
             String SQL = "Select * from "+ TableName +"  Where MorbiID like('"+ SearchText +"%') or PGN like('"+ SearchText +"%') or ChSl like('"+ SearchText +"%') and isdelete=2";
             List<NBC_Morbidity_DataModel> data = d.SelectAll(this, SQL);
             dataList.clear();

             dataList.addAll(data);
             if (dataList != null && !dataList.isEmpty())
             {
                 recyclerView.setVisibility(View.VISIBLE);
                 ll_no_data.setVisibility(View.GONE);
             }
             else
             {
                 recyclerView.setVisibility(View.GONE);
                 ll_no_data.setVisibility(View.VISIBLE);
             }
             try {
                 mAdapter.notifyDataSetChanged();
                 lblTotal.setText(" (Total: "+ dataList.size() +")");
             }catch ( Exception ex){
                 Connection.MessageBox(NBC_Morbidity_list.this,ex.getMessage());
             }
        }
        catch(Exception  e)
        {
            Connection.MessageBox(NBC_Morbidity_list.this, e.getMessage());
            return;
        }
     }



     public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {
         private List<NBC_Morbidity_DataModel> dataList;
         public class MyViewHolder extends RecyclerView.ViewHolder {
         LinearLayout secListRow;
         TextView MorbiID;
         TextView MemID;
         TextView HHID;
         TextView PGN;
         TextView ChSl;
         TextView ChDiarrhea;
         TextView DiarrheaTreat;
         TextView DiarrheaTreatPlace;
         TextView DiarrheaTreatPlaceOth;
         TextView Cough2W;
         TextView FastBrth;
         TextView FastBrthCause;
         TextView SeekHCare;
         TextView SeekHCarePlace;
         TextView SeekHCarePlaceOth;
         TextView Fever2W;
         TextView FeverTreat;
         TextView FeverTreatPlace;
         TextView FeverTreatPlaceOth;
         TextView WeightLost;
         TextView PoorWeight;
         TextView FeedLP2Week;
         TextView UnderweightAge;
         TextView OverweightAge;
         TextView ComMEBaby;
         public MyViewHolder(View convertView) {
             super(convertView);
             secListRow = (LinearLayout)convertView.findViewById(R.id.secListRow);
             MorbiID = (TextView)convertView.findViewById(R.id.MorbiID);
             MemID = (TextView)convertView.findViewById(R.id.MemID);
             HHID = (TextView)convertView.findViewById(R.id.HHID);
             PGN = (TextView)convertView.findViewById(R.id.PGN);
             ChSl = (TextView)convertView.findViewById(R.id.ChSl);
             ChDiarrhea = (TextView)convertView.findViewById(R.id.ChDiarrhea);
             DiarrheaTreat = (TextView)convertView.findViewById(R.id.DiarrheaTreat);
             DiarrheaTreatPlace = (TextView)convertView.findViewById(R.id.DiarrheaTreatPlace);
             DiarrheaTreatPlaceOth = (TextView)convertView.findViewById(R.id.DiarrheaTreatPlaceOth);
             Cough2W = (TextView)convertView.findViewById(R.id.Cough2W);
             FastBrth = (TextView)convertView.findViewById(R.id.FastBrth);
             FastBrthCause = (TextView)convertView.findViewById(R.id.FastBrthCause);
             SeekHCare = (TextView)convertView.findViewById(R.id.SeekHCare);
             SeekHCarePlace = (TextView)convertView.findViewById(R.id.SeekHCarePlace);
             SeekHCarePlaceOth = (TextView)convertView.findViewById(R.id.SeekHCarePlaceOth);
             Fever2W = (TextView)convertView.findViewById(R.id.Fever2W);
             FeverTreat = (TextView)convertView.findViewById(R.id.FeverTreat);
             FeverTreatPlace = (TextView)convertView.findViewById(R.id.FeverTreatPlace);
             FeverTreatPlaceOth = (TextView)convertView.findViewById(R.id.FeverTreatPlaceOth);
             WeightLost = (TextView)convertView.findViewById(R.id.WeightLost);
             PoorWeight = (TextView)convertView.findViewById(R.id.PoorWeight);
             FeedLP2Week = (TextView)convertView.findViewById(R.id.FeedLP2Week);
             UnderweightAge = (TextView)convertView.findViewById(R.id.UnderweightAge);
             OverweightAge = (TextView)convertView.findViewById(R.id.OverweightAge);
             ComMEBaby = (TextView)convertView.findViewById(R.id.ComMEBaby);
             }
         }
         public DataAdapter(List<NBC_Morbidity_DataModel> datalist) {
             this.dataList = datalist;
         }
         @Override
         public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
             View itemView = LayoutInflater.from(parent.getContext())
                     .inflate(R.layout.nbc_morbidity_row, parent, false);
             return new MyViewHolder(itemView);
         }
         @Override
         public void onBindViewHolder(MyViewHolder holder, int position) {
             final NBC_Morbidity_DataModel data = dataList.get(position);
             holder.MorbiID.setText(data.getMorbiID());
             holder.MemID.setText(data.getMemID());
             holder.HHID.setText(data.getHHID());
             holder.PGN.setText(data.getPGN());
             holder.ChSl.setText(data.getChSl());
             holder.ChDiarrhea.setText(String.valueOf(data.getChDiarrhea()));
             holder.DiarrheaTreat.setText(String.valueOf(data.getDiarrheaTreat()));
             holder.DiarrheaTreatPlace.setText(String.valueOf(data.getDiarrheaTreatPlace()));
             holder.DiarrheaTreatPlaceOth.setText(data.getDiarrheaTreatPlaceOth());
             holder.Cough2W.setText(String.valueOf(data.getCough2W()));
             holder.FastBrth.setText(String.valueOf(data.getFastBrth()));
             holder.FastBrthCause.setText(String.valueOf(data.getFastBrthCause()));
             holder.SeekHCare.setText(String.valueOf(data.getSeekHCare()));
             holder.SeekHCarePlace.setText(String.valueOf(data.getSeekHCarePlace()));
             holder.SeekHCarePlaceOth.setText(data.getSeekHCarePlaceOth());
             holder.Fever2W.setText(String.valueOf(data.getFever2W()));
             holder.FeverTreat.setText(String.valueOf(data.getFeverTreat()));
             holder.FeverTreatPlace.setText(String.valueOf(data.getFeverTreatPlace()));
             holder.FeverTreatPlaceOth.setText(data.getFeverTreatPlaceOth());
             holder.WeightLost.setText(String.valueOf(data.getWeightLost()));
             holder.PoorWeight.setText(String.valueOf(data.getPoorWeight()));
             holder.FeedLP2Week.setText(String.valueOf(data.getFeedLP2Week()));
             holder.UnderweightAge.setText(String.valueOf(data.getUnderweightAge()));
             holder.OverweightAge.setText(String.valueOf(data.getOverweightAge()));
             holder.ComMEBaby.setText(String.valueOf(data.getComMEBaby()));


             holder.secListRow.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View v) {
                     final ProgressDialog progDailog = ProgressDialog.show(NBC_Morbidity_list.this, "", "Please Wait . . .", true);
                     new Thread() {
                         public void run() {
                             try {
                                 Bundle IDbundle = new Bundle();
                                 IDbundle.putString("MorbiID", data.getMorbiID());
                                 IDbundle.putString("PGN", data.getPGN());
                                 IDbundle.putString("ChSl", data.getChSl());
                                 Intent f1 = new Intent(getApplicationContext(), NBC_Morbidity.class);
                                 f1.putExtras(IDbundle);
                                 startActivityForResult(f1,1);
                             } catch (Exception e) {
                             }
                             progDailog.dismiss();
                         }
                     }.start();
                 }
             });
         }
         @Override
         public int getItemCount() {
             return dataList.size();
         }
     }


     protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener,g.mYear,g.mMonth-1,g.mDay);
        }
        return null;
     }


     private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year; mMonth = monthOfYear+1; mDay = dayOfMonth;
            EditText dtpDate;
            dtpDate = (EditText)findViewById(R.id.dtpFDate);
            if (VariableID.equals("dtpFDate"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpFDate);
            }
            else if (VariableID.equals("dtpTDate"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpTDate);
            }
            dtpDate.setText(new StringBuilder()
                .append(Global.Right("00"+mDay,2)).append("/")
                .append(Global.Right("00"+mMonth,2)).append("/")
                .append(mYear));
        }
     };


}