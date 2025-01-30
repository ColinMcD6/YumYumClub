package recipebook.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import comp3350.yumyumclub.R;

import recipebook.application.Services;
import recipebook.business.AccessFolderInterface;

public class AddRecipeAdapter extends RecyclerView.Adapter<AddRecipeAdapter.ViewHolder>{

    List<String> folderNames;

    List<String> checkedFolders;

    int recipeID;

    AccessFolderInterface accessFolder;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final CheckBox checkBox;

        public ViewHolder(View view)
        {
            super(view);

            checkBox = view.findViewById(R.id.folder_checkbox);

        }

        public CheckBox getCheckBox() { return checkBox; }
    }

    public AddRecipeAdapter(List<String> folderNames, int recipeID) {
        this.folderNames = folderNames;
        this.recipeID = recipeID;
        checkedFolders = new ArrayList<>();
        accessFolder = Services.getAccessFolder();
    }

    @Override
    public AddRecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.folder_checkbox_list_item, viewGroup, false);
        return new AddRecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddRecipeAdapter.ViewHolder viewHolder, final int position)
    {


        viewHolder.getCheckBox().setText(folderNames.get(position));

        if(accessFolder.checkFolderHasRecipe(folderNames.get(position), recipeID)){
            viewHolder.getCheckBox().setChecked(true);
            viewHolder.getCheckBox().setClickable(false);
        }

        viewHolder.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    checkedFolders.add(viewHolder.getCheckBox().getText().toString());
                }
                else
                {
                    checkedFolders.remove(viewHolder.getCheckBox().getText().toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return folderNames.size();
    }

    public List<String> getCheckedFolder(){
        return checkedFolders;
    }
}
