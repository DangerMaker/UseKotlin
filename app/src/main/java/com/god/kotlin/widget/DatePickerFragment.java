package com.god.kotlin.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.god.kotlin.R;

public class DatePickerFragment extends DialogFragment {

    int year;
    int month;
    int day;

    public static DatePickerFragment newInstance(int year, int month, int day) {
        Bundle args = new Bundle();
        DatePickerFragment fragment = new DatePickerFragment();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        year = getArguments().getInt("year");
        month = getArguments().getInt("month");
        day = getArguments().getInt("day");

        View view = inflater.inflate(R.layout.trade_fragment_datepicker, null);
        DatePicker datePicker = view.findViewById(R.id.date_picker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                callback.callback(year, monthOfYear, dayOfMonth);
            }
        });

        Button ok = view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }


    DatePickerCallback callback;
    public void setCallback(DatePickerCallback callback){
        this.callback = callback;
    }

    public interface DatePickerCallback {

        void callback(int year,int month,int day);
    }

}
