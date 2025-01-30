package recipebook.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.objects.Recipe;

public class SavedFolderAdapter extends RecyclerView.Adapter<SavedFolderAdapter.ViewHolder> {

    private HashMap<String, List<Recipe>> folders;
    private List<String> folderNames;
    private static SavedFolderFragment fragment;


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final CardView thumbnailCard; // transition start view
        private final ImageView savedThumbnail;
        private final TextView savedFolderTitle;
        private final TextView savedFolderRecipesCount;

        public ViewHolder(View view)
        {
            super(view);

            thumbnailCard = view.findViewById(R.id.saved_folder_card);
            savedThumbnail = view.findViewById(R.id.saved_thumbnail);
            savedFolderTitle = view.findViewById(R.id.saved_foldder_title);
            savedFolderRecipesCount = view.findViewById(R.id.saved_folder_recipes_count);
        }

        public CardView getThumbnailCard() {return thumbnailCard; }
        public ImageView getSavedThumbnail() { return savedThumbnail; }
        public TextView getSavedFolderTitle() {
            return savedFolderTitle;
        }
        public TextView getSavedFolderItemsCount() { return savedFolderRecipesCount; }
    }

    public SavedFolderAdapter(HashMap<String, List<Recipe>> folders, SavedFolderFragment fragment) {
        this.folders = folders;
        this.fragment = fragment;
        folderNames = new ArrayList<>(folders.keySet());
    }


    @Override
    public SavedFolderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_folder_item, viewGroup, false);

        return new SavedFolderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedFolderAdapter.ViewHolder viewHolder, final int position) {
        String folderName = folderNames.get(position);
        List<Recipe> recipeAt = folders.get(folderName);

        Context context = viewHolder.getSavedThumbnail().getContext();
        int resId;

        if (recipeAt.size() > 0) {
            resId = viewHolder.getSavedThumbnail().getResources().getIdentifier(recipeAt.get(0).getImage_URL(), "drawable", context.getPackageName());
        } else {
            resId = viewHolder.getSavedThumbnail().getResources().getIdentifier("cooking_pot", "drawable", context.getPackageName());
        }

        viewHolder.getSavedThumbnail().setImageResource(resId);
        viewHolder.getSavedFolderTitle().setText(folderName);
        viewHolder.getSavedFolderItemsCount().setText(recipeAt.size() + " items");

        //When item is clicked
        viewHolder.getThumbnailCard().setOnClickListener(view -> {
                    fragment.goInsideSaved(folderName);
                }
        );
    }

    @Override
    public int getItemCount() {
        return folders.keySet().size();
    }

}
