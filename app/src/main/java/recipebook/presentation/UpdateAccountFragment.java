package recipebook.presentation;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import comp3350.yumyumclub.R;
import recipebook.business.AccessUser;
import recipebook.business.AccessUserInterface;

public class UpdateAccountFragment extends Fragment
{
    // UI Objects
    TextInputEditText changeNameBox;
    TextInputEditText changePasswordBox;
    TextView viewEmailBox;
    MaterialButton logOutBtn;
    MaterialButton confrimBtn;

    // CurrentUser helper
    AccessUserInterface accessUser = AccessUser.getInstance();

    public UpdateAccountFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_update_account, container, false);

        // Assign UI objects
        changeNameBox = view.findViewById(R.id.changeName_box);
        changePasswordBox = view.findViewById(R.id.changePassword_box);
        viewEmailBox = view.findViewById(R.id.emailView_box);
        logOutBtn = view.findViewById(R.id.logOut_bt);
        confrimBtn = view.findViewById(R.id.confirm_bt);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                LogoutOnClick();
            }
        });

        confrimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateOnClick();
            }
        });

        // Fill in current loged in user name and email
        changeNameBox.setText(accessUser.getCurrentUser().getUserName());
        viewEmailBox.setText(accessUser.getCurrentUser().getEmail());

        return view;
    }

    // Log Out button - log out current user session
    public void LogoutOnClick()
    {
        AccessUser.getInstance().logOut();
        Intent intent= new Intent(requireActivity(), LogInActivity.class);
        requireActivity().finish();
        startActivity(intent);
    }

    // Update button - update user name, password
    public void UpdateOnClick()
    {
        String newPassword = changePasswordBox.getText().toString();

        AccessUser.getInstance().UpdateUser( newPassword);
        UpdateDialog();
    }

    // Notification Pop up - show user information has changed.
    private void UpdateDialog()
    {
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Alright!")
                .setMessage("Your new information has been saved")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        changePasswordBox.setText(null);
    }
}


