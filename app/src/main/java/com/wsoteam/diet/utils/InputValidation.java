package com.wsoteam.diet.utils;

import android.text.TextUtils;
import android.widget.EditText;

public interface InputValidation {
  /**
   * @param input EditText to test
   * @return Error message if fails to validate
   */
  CharSequence validate(EditText input);

  abstract class AbstractInputValidator implements InputValidation {
    private final int errorMessageId;

    public AbstractInputValidator(int errorMessageId) {
      this.errorMessageId = errorMessageId;
    }

    @Override public CharSequence validate(EditText input) {
      return checkValid(input) ? null : input.getContext().getString(errorMessageId);
    }

    protected abstract boolean checkValid(EditText input);
  }

  class MinLengthValidation extends AbstractInputValidator {

    private int allowedMinimum = 0;

    public MinLengthValidation(int errorMessageId) {
      super(errorMessageId);
    }

    public MinLengthValidation(int errorMessageId, int allowedMinimum) {
      super(errorMessageId);
      this.allowedMinimum = allowedMinimum;
    }

    public void setAllowedMinimum(int allowedMinimum) {
      this.allowedMinimum = allowedMinimum;
    }

    @Override protected boolean checkValid(EditText input) {
      return input.length() >= allowedMinimum;
    }
  }

  class EmailValidation extends AbstractInputValidator {

    public EmailValidation(int errorMessageId) {
      super(errorMessageId);
    }

    @Override protected boolean checkValid(EditText input) {
      return !TextUtils.isEmpty(input.getText()) && android.util.Patterns.EMAIL_ADDRESS.matcher(input.getText().toString().trim()).matches();
    }
  }

  class ValueRangeValidator implements InputValidation {
    private final int errorMessageid;
    private final int allowedMinValue;
    private final int allowedMaxValue;

    public ValueRangeValidator(int allowedMinValue, int allowedMaxValue, int errorMessageid) {
      this.errorMessageid = errorMessageid;
      this.allowedMinValue = allowedMinValue;
      this.allowedMaxValue = allowedMaxValue;
    }

    @Override public CharSequence validate(EditText input) {
      if (inBetween(getIntValue(input, -1), allowedMinValue, allowedMaxValue)) {
        return null;
      } else {
        return input.getContext().getString(errorMessageid);
      }
    }

    static int getIntValue(EditText input, int defaultValue) {
      try {
        return Integer.parseInt(input.getText().toString());
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    }

    static boolean inBetween(int value, int min, int max) {
      return value >= min && max >= value;
    }
  }
}