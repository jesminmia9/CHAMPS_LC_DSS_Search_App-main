package forms_datamodel;

import android.content.Context;
 import android.database.Cursor;
 import Common.Connection;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Date;
 import Common.Global;
 import android.content.ContentValues;
 public class GPSLandmark_DataModel{

        private String _VillID = "";
        public String getVillID(){
              return _VillID;
         }
        public void setVillID(String newValue){
              _VillID = newValue;
         }
        private String _LMNo = "";
        public String getLMNo(){
              return _LMNo;
         }
        public void setLMNo(String newValue){
              _LMNo = newValue;
         }
        private String _LocalityName = "";
        public String getLocalityName(){
              return _LocalityName;
         }
        public void setLocalityName(String newValue){
              _LocalityName = newValue;
         }
        private String _LMName = "";
        public String getLMName(){
              return _LMName;
         }
        public void setLMName(String newValue){
              _LMName = newValue;
         }
        private String _Latitude = "";
        public String getLatitude(){
              return _Latitude;
         }
        public void setLatitude(String newValue){
              _Latitude = newValue;
         }
        private String _Longitude = "";
        public String getLongitude(){
              return _Longitude;
         }
        public void setLongitude(String newValue){
              _Longitude = newValue;
         }
        private String _StartTime = "";
        public void setStartTime(String newValue){
              _StartTime = newValue;
         }
        private String _EndTime = "";
        public void setEndTime(String newValue){
              _EndTime = newValue;
         }
        private String _DeviceID = "";
        public void setDeviceID(String newValue){
              _DeviceID = newValue;
         }
        private String _EntryUser = "";
        public void setEntryUser(String newValue){
              _EntryUser = newValue;
         }
        private String _Lat = "";
        public void setLat(String newValue){
              _Lat = newValue;
         }
        private String _Lon = "";
        public void setLon(String newValue){
              _Lon = newValue;
         }
        private String _EnDt = Global.DateTimeNowYMDHMS();
        private int _Upload = 2;
        private String _modifyDate = Global.DateTimeNowYMDHMS();

        String TableName = "GPSLandmark";

        public String SaveUpdateData(Context context)
        {
            String response = "";
            C = new Connection(context);
            String SQL = "";
            try
            {
                 if(C.Existence("Select * from "+ TableName +"  Where VillID='"+ _VillID +"' and LMNo='"+ _LMNo +"' "))
                    response = UpdateData(context);
                 else
                    response = SaveData(context);
            }
            catch(Exception  e)
            {
                 response = e.getMessage();
            }
           return response;
        }
        Connection C;

        private String SaveData(Context context)
        {
            String response = "";
            C = new Connection(context);
            try
              {
                 ContentValues contentValues = new ContentValues();
                 contentValues.put("VillID", _VillID);
                 contentValues.put("LMNo", _LMNo);
                 contentValues.put("LocalityName", _LocalityName);
                 contentValues.put("LMName", _LMName);
                 contentValues.put("Latitude", _Latitude);
                 contentValues.put("Longitude", _Longitude);
                 contentValues.put("StartTime", _StartTime);
                 contentValues.put("EndTime", _EndTime);
                 contentValues.put("DeviceID", _DeviceID);
                 contentValues.put("EntryUser", _EntryUser);
                 contentValues.put("Lat", _Lat);
                 contentValues.put("Lon", _Lon);
                 contentValues.put("EnDt", _EnDt);
                 contentValues.put("Upload", _Upload);
                 contentValues.put("modifyDate", _modifyDate);
                 C.InsertData(TableName, contentValues);
              }
              catch(Exception  e)
              {
                 response = e.getMessage();
              }
           return response;
        }

        private String UpdateData(Context context)
        {
            String response = "";
            C = new Connection(context);
            try
              {
                 ContentValues contentValues = new ContentValues();
                 contentValues.put("VillID", _VillID);
                 contentValues.put("LMNo", _LMNo);
                 contentValues.put("LocalityName", _LocalityName);
                 contentValues.put("LMName", _LMName);
                 contentValues.put("Latitude", _Latitude);
                 contentValues.put("Longitude", _Longitude);
                 contentValues.put("Upload", _Upload);
                 contentValues.put("modifyDate", _modifyDate);
                 C.UpdateData(TableName, "VillID,LMNo", (""+ _VillID +", "+ _LMNo +""), contentValues);
              }
              catch(Exception  e)
              {
                 response = e.getMessage();
              }
           return response;
        }

          int Count = 0;          private int _Count = 0;          public int getCount(){ return _Count; }
        public List<GPSLandmark_DataModel> SelectAll(Context context, String SQL)
        {
            Connection C = new Connection(context);
            List<GPSLandmark_DataModel> data = new ArrayList<GPSLandmark_DataModel>();
            GPSLandmark_DataModel d = new GPSLandmark_DataModel();
            Cursor cur = C.ReadData(SQL);

            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                Count += 1;
                d = new GPSLandmark_DataModel();
                d._Count = Count;
                d._VillID = cur.getString(cur.getColumnIndex("VillID"));
                d._LMNo = cur.getString(cur.getColumnIndex("LMNo"));
                d._LocalityName = cur.getString(cur.getColumnIndex("LocalityName"));
                d._LMName = cur.getString(cur.getColumnIndex("LMName"));
                d._Latitude = cur.getString(cur.getColumnIndex("Latitude"));
                d._Longitude = cur.getString(cur.getColumnIndex("Longitude"));
                data.add(d);

                cur.moveToNext();
            }
            cur.close();
          return data;
        }
 }