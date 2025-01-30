package recipebook.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import comp3350.yumyumclub.R;

import recipebook.application.Services;
import recipebook.business.AccessFolderInterface;

public class AddRecipeFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ModalBottomSheet";

    private AccessFolderInterface accessFolder;

    private List<String> folderNamess;

    private int recipeID;

    private ImageButton closeBtn;

    private Button completeBtn;

    private AddRecipeAdapter addRecipeAdapter;

    private RecyclerView folderCheckboxRecyclerView;

    public AddRecipeFragment(int recipeID) {
        this.recipeID = recipeID;
        accessFolder = Services.getAccessFolder();
        folderNamess = new ArrayList<>(accessFolder.getAllFolders().keySet());
    }


    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_recipe_option, container, false);

        folderCheckboxRecyclerView = view.findViewById(R.id.folder_checkbox_recycler_view);
        closeBtn = view.findViewById(R.id.close_bottom_sheet_bt);
        completeBtn = view.findViewById(R.id.complete_btn);

        closeBtn.setOnClickListener(v -> {
            onClickCloseBtn();
        });

        completeBtn.setOnClickListener(v -> {
            onClickCompleteBtn();
        });

        addRecipeAdapter = new AddRecipeAdapter(folderNamess, recipeID);
        folderCheckboxRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        folderCheckboxRecyclerView.setAdapter(addRecipeAdapter);

        return  view;
    }

    private void onClickCloseBtn(){
        dismiss();
    }



    private void onClickCompleteBtn(){
        addRecipeToFolders();
        dismiss();
    }

    private void addRecipeToFolders(){
        List<String> checkedFolders = addRecipeAdapter.getCheckedFolder();

        for(String folderName:checkedFolders){
            accessFolder.addRecipeToFolder(folderName, recipeID);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Set the bottom sheet to expanded state
        BottomSheetBehavior<?> bottomSheetBehavior = BottomSheetBehavior.from((View) getView().getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // Disable dragging
        bottomSheetBehavior.setDraggable(false);
    }

}
