package com.example.garbagetruck;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private static Context mContext;
    private ArrayList<String> mGroupName;
    private ArrayList<ArrayList<notifyPosition>> mItem;
    int spinnerDistrictIndex,spinnerCountryIndex;


    @Override
    public int getGroupCount() {
        return mGroupName.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mItem.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return mGroupName.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mItem.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String[] setting_name = mContext.getResources().getStringArray(R.array.setting_list);
        List<String> groupName = Arrays.asList(setting_name);
        List<String> settingGroup = new ArrayList<String>(groupName);
        mGroupName = (ArrayList<String>) settingGroup;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_group, null);
        }
        TextView textView = view.findViewById(R.id.listTitle);
        textView.setText(mGroupName.get(i));


        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        }
        Spinner spinnerCountry,spinnerDistrict,spinnerRoute;
        spinnerCountry = view.findViewById(R.id.spinner7);
        spinnerDistrict = view.findViewById(R.id.spinner8);
        spinnerRoute = view.findViewById(R.id.spinner9);
        notifyPosition notifyPosition = mItem.get(i).get(i1);
        spinnerCountry.setSelection(notifyPosition.getCountry());
        spinnerCountry.setClickable(false);
        spinnerCountry.setEnabled(false);
        spinnerDistrict.setSelection(notifyPosition.getDistric());
        spinnerDistrict.setClickable(false);
        spinnerDistrict.setEnabled(false);
        spinnerRoute.setSelection(notifyPosition.getRoute());
        spinnerRoute.setClickable(false);
        spinnerRoute.setEnabled(false);
        EditText address = view.findViewById(R.id.editTextTextPersonName);
        address.setText(notifyPosition.getAddress());
        address.setFocusable(false);
        address.setFocusableInTouchMode(false);
        EditText nickName = view.findViewById(R.id.editTextTextPersonName2);
        nickName.setText(notifyPosition.getNickName());
        nickName.setFocusable(false);
        nickName.setFocusableInTouchMode(false);
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(mContext,R.array.country, android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter1);
        spinnerCountry.setSelection(notifyPosition.getCountry());
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<CharSequence> adapter2;
                spinnerCountryIndex = i;
                switch (i){
                    case 0:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.keelungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 1:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.newTaipeiDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 2:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.taipeiDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 3:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.taoyuanDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 4:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.hsinchuDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 5:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.miaoliDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 6:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.taichungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 7:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.changhuaDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 8:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.nantouDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 9:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.yunlinDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 10:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.chiayiDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 11:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.tainanDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 12:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.kaohsiungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 13:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.pingtungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 14:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.taitungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 15:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.hualienDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 16:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.yilanDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 17:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.penghuDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 18:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.kinmenDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 19:
                        adapter2 = ArrayAdapter.createFromResource(mContext,R.array.lienchiangDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
                spinnerDistrict.setAdapter(adapter2);
                spinnerDistrict.setSelection(notifyPosition.getDistric());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerCountryIndex = 12;
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(mContext,R.array.kaohsiungDistrict, android.R.layout.simple_spinner_dropdown_item);
                spinnerDistrict.setAdapter(adapter2);
                spinnerDistrict.setSelection(notifyPosition.getDistric());
            }
        });
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerDistrictIndex = i;
                ArrayAdapter<CharSequence> adapter3;
                if(spinnerCountryIndex == 12 && i == 0){
                    adapter3 = ArrayAdapter.createFromResource(mContext,R.array.route_kaohsiung_1, android.R.layout.simple_spinner_dropdown_item);
                }
                else if(spinnerCountryIndex == 12 && i == 1){
                    adapter3 = ArrayAdapter.createFromResource(mContext,R.array.route_kaohsiung_2, android.R.layout.simple_spinner_dropdown_item);

                }
                else {
                    adapter3 = ArrayAdapter.createFromResource(mContext,R.array.route1, android.R.layout.simple_spinner_dropdown_item);

                }
//                if(spinnerCountryIndex == 12){
//                    districtName = (String) spinnerDistrict.getSelectedItem();
//                }
                spinnerRoute.setAdapter(adapter3);
                spinnerRoute.setSelection(notifyPosition.getRoute());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerDistrictIndex = 0;
                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(mContext,R.array.route_kaohsiung_1, android.R.layout.simple_spinner_dropdown_item);
                spinnerRoute.setAdapter(adapter3);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkBox.setBackgroundResource(R.drawable.save);
                    spinnerCountry.setClickable(true);
                    spinnerCountry.setEnabled(true);
                    spinnerDistrict.setClickable(true);
                    spinnerDistrict.setEnabled(true);
                    spinnerRoute.setClickable(true);
                    spinnerRoute.setEnabled(true);
                    address.setFocusable(true);
                    address.setFocusableInTouchMode(true);
                    address.requestFocus();
                    nickName.setFocusable(true);
                    nickName.setFocusableInTouchMode(true);
                    nickName.requestFocus();
                }
                else {
                    checkBox.setBackgroundResource(R.drawable.pencil);
                    spinnerCountry.setClickable(false);
                    spinnerCountry.setEnabled(false);
                    spinnerDistrict.setClickable(false);
                    spinnerDistrict.setEnabled(false);
                    spinnerRoute.setClickable(false);
                    spinnerRoute.setEnabled(false);
                    address.setFocusable(false);
                    address.setFocusableInTouchMode(false);
                    nickName.setFocusable(false);
                    nickName.setFocusableInTouchMode(false);
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("notify",Context.MODE_PRIVATE);
                    sharedPreferences.edit().putInt("country",spinnerCountry.getSelectedItemPosition()).
                            putInt("district",spinnerDistrict.getSelectedItemPosition()).
                            putInt("route",spinnerRoute.getSelectedItemPosition()).
                            putString("address",spinnerCountry.getSelectedItem().toString()+spinnerDistrict.getSelectedItem().toString()+address.getText().toString()).
                            putString("nickName",nickName.getText().toString()).
                            putString("city",spinnerDistrict.getSelectedItem().toString()).
                            putString("districName",spinnerDistrict.getSelectedItem().toString()).
                            commit();
                }
            }
        });


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public ExpandableListViewAdapter(Context context, ArrayList<String> groupName,ArrayList<ArrayList<notifyPosition>> item){
        this.mContext = context;
        this.mGroupName = groupName;
        this.mItem = item;
    }
}
