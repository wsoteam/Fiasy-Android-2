package com.wsoteam.diet.MainScreen.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.util.Calendar;

public class SublimePickerDialogFragment extends DialogFragment {

    OnDateChoosedListener dateChoosedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SublimeListenerAdapter listener = new SublimeListenerAdapter() {
            @Override
            public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
                Calendar calendar = selectedDate.getFirstDate();
                dateChoosedListener.dateChoosed(calendar, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                dismiss();
            }

            @Override
            public void onCancelled() {
                dismiss();
            }
        };

        SublimePicker sublimePicker = new SublimePicker(getActivity());
        SublimeOptions sublimeOptions = new SublimeOptions();
        sublimeOptions.setCanPickDateRange(false);
        sublimeOptions.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        sublimeOptions.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER);
        sublimePicker.initializePicker(sublimeOptions, listener);
        return sublimePicker;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dateChoosedListener = (OnDateChoosedListener) getTargetFragment();
    }

    public interface OnDateChoosedListener {
        void dateChoosed(Calendar calendar, int dayOfMonth, int month, int year);
    }
}
