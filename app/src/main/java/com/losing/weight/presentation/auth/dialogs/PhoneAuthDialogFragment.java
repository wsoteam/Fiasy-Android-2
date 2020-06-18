package com.losing.weight.presentation.auth.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.losing.weight.Authenticate.Valid;
import com.losing.weight.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PhoneAuthDialogFragment extends DialogFragment {

    @BindView(R.id.auth_phone_tv) TextView infoTextView;
    @BindView(R.id.auth_phone_et_number) EditText phoneNumberEditText;
    @BindView(R.id.auth_phone_et_code) EditText codeEditText;
    @BindView(R.id.auth_phone_btn_ok) Button okButton;
    private OnPhoneAuthClickListener mListener;
    private Unbinder unbinder;
    private boolean isSendCode;

    public static PhoneAuthDialogFragment newInstance() {
        return new PhoneAuthDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog_phone_auth, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.auth_phone_btn_ok, R.id.auth_phone_btn_cancel})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.auth_phone_btn_ok:
                if (!isSendCode) {
                    String phone = phoneNumberEditText.getText().toString();
                    if (Valid.isValidPhone(phone)) {
                        mListener.onPhoneNumberVerification(phone);
                        codeEditText.setEnabled(true);
                        phoneNumberEditText.setEnabled(false);
                        infoTextView.setText(R.string.auth_main_phone_text_set_code);
                        okButton.setText(R.string.signIn);
                        isSendCode = true;
                    } else {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.auth_phone_wrong_number), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String code = codeEditText.getText().toString();
                    if (Valid.isValidCode(code)) {
                        mListener.onVerifyPhoneNumberCode(code);
                    } else {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.auth_phone_wrong_code), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.auth_phone_btn_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnPhoneAuthClickListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnPhoneAuthClickListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public interface OnPhoneAuthClickListener {
        void onPhoneNumberVerification(String phoneNumber);

        void onVerifyPhoneNumberCode(String code);
    }
}
