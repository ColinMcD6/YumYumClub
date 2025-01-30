package recipebook.presentation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.HashMap;
import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.application.Services;
import recipebook.objects.Recipe;
import recipebook.business.AccessFolderInterface;

public class SavedFolderFragment extends Fragment {


    private FloatingActionButton addFolderFAB;
    private RecyclerView savedFoldrRecyclerView;
    private ScrollView scrollView;
    private CircularProgressIndicator loadingIndicator;

    private HashMap<String, List<Recipe>> folders;

    private AccessFolderInterface accessFolder;

    private int dynamicColumns;
    final int FADE_IN_DURATION = 400;


    // Constructor
    public SavedFolderFragment() {
        folders = new HashMap<>();
        accessFolder = Services.getAccessFolder();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);


        savedFoldrRecyclerView = view.findViewById(R.id.saved_folder_recycler);
        addFolderFAB = view.findViewById(R.id.add_folder_FAB);
        scrollView = view.findViewById(R.id.saved_scroll_view);
        loadingIndicator = view.findViewById(R.id.saved_loading_indicator);

        loadingIndicator.setVisibility(View.VISIBLE);
        savedFoldrRecyclerView.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);

        calculateRowNumber();

        addFolderFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFolderDialog();
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                folders = accessFolder.getAllFolders();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI elements
                        displaySaved();
                        scrollView.setVisibility(View.VISIBLE);
                        loadingIndicator.setVisibility(View.INVISIBLE);
                        fadeIn(savedFoldrRecyclerView);

                    }
                });
            }
        }).start();
    }


    private void displaySaved(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), dynamicColumns);
        SavedFolderAdapter savedFolderAdapter = new SavedFolderAdapter(folders, this);

        savedFolderAdapter.notifyDataSetChanged();
        savedFoldrRecyclerView.setAdapter(savedFolderAdapter);
        savedFoldrRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void calculateRowNumber(){
        int displayWidth = getDisplayWidthDp();
        dynamicColumns = displayWidth < 200 ? 1 : displayWidth/200;
    }

    private int getDisplayWidthDp(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int widthPixels = displayMetrics.widthPixels;
        return Math.round(widthPixels / density);
    }

    public void goInsideSaved(String folderName){
        NavController navController = Navigation.findNavController(requireView());
        SavedFolderFragmentDirections.InsideFolder action =
                SavedFolderFragmentDirections.insideFolder("");
        action.setFolderName(folderName);
        navController.navigate(action);
    }

    private void createFolderDialog(){

        // Create the dialog, Inflate the dialog layout
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_folder, null);

        builder.setTitle("Create New Folder");
        builder.setView(dialogView);

        // Set up the EditText and Button inside the dialog
        EditText editTextFolderName = dialogView.findViewById(R.id.edit_folder_name);
        Button buttonSubmit = dialogView.findViewById(R.id.create_folder_btn);
        Button cancel = dialogView.findViewById(R.id.cacnel_btn);

        // Add TextWatcher to the EditText
        editTextFolderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable/disable button based on text input
                String folderName = s.toString().trim();
                if (accessFolder.isFolderNameUnique(folderName) && (folderName.length() > 0)) {
                    buttonSubmit.setEnabled(true);
                } else {
                    buttonSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        AlertDialog dialog = builder.create();
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderName = editTextFolderName.getText().toString().trim();
                if (!folderName.isEmpty()) {
                    createFolder(folderName);
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


        dialog.show();
    }


    private void createFolder(String folderName) {
        //sent data to DB by logic
        accessFolder.addFolder(folderName);
        folders = accessFolder.getAllFolders();

        displaySaved();
    }

    private void fadeIn(View view){
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(FADE_IN_DURATION)
                .setListener(null);
    }



}
