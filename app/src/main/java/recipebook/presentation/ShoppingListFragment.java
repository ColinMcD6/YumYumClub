package recipebook.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;


import comp3350.yumyumclub.R;

public class ShoppingListFragment extends Fragment {

    RecyclerView shoppingListRecyclerView;

    Button editBtn;

    Button clearBtn;

    public ShoppingListFragment(){

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);


        shoppingListRecyclerView = view.findViewById(R.id.shopping_list_recycler);
        editBtn = view.findViewById(R.id.edit_btn);
        clearBtn= view.findViewById(R.id.clear_btn);

        ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter();
        shoppingListRecyclerView.setAdapter(shoppingListAdapter);
        shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        editBtn.setOnClickListener(v -> {
            if(editBtn.getText().equals("Edit")){
                editBtn.setText("Save");
                TransitionManager.beginDelayedTransition(shoppingListRecyclerView, new Fade());
                shoppingListAdapter.setDeleteBtnVisible(true);
            } else if (editBtn.getText().equals("Save")) {
                editBtn.setText("Edit");
                TransitionManager.beginDelayedTransition(shoppingListRecyclerView, new Fade());
                shoppingListAdapter.setDeleteBtnVisible(false);
            }
        });

        clearBtn.setOnClickListener(v -> {
            shoppingListAdapter.clearList();
        });


        return view;
    }
}
