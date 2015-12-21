package mj.project.chat_ex.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;


public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("아이디와 비밀번호를 입력해주세요 !").setTitle("Warning").setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
