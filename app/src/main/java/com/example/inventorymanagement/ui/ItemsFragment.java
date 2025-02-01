package com.example.inventorymanagement.ui;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.inventorymanagement.R;
import com.example.inventorymanagement.adapters.ItemAdapter;
import com.example.inventorymanagement.callbacks.ItemSwipeCallback;
import com.example.inventorymanagement.callbacks.SwipeToDeleteCallback;
import com.example.inventorymanagement.callbacks.SwipeToEditCallback;
import com.example.inventorymanagement.databinding.FragmentItemsBinding;
import com.example.inventorymanagement.viewModel.InventoryViewModel;
import com.example.inventorymanagementsdk.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsFragment extends Fragment {

    private FragmentItemsBinding binding;
    private Button insertItemButton;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemTouchHelper itemTouchHelperDelete;
    private ItemTouchHelper itemTouchHelperEdit;
    private InventoryViewModel inventoryViewModel = null;
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inventoryViewModel =
                new ViewModelProvider(this).get(InventoryViewModel.class);

        binding = FragmentItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initUI();
        setUpUI();
        return root;
    }

    public void initUI() {
        recyclerView = binding.rvItems;
        swipeRefreshLayout = binding.swipeRefresh;
        insertItemButton = binding.addItemButton;
        searchView = binding.searchView;
        searchView.clearFocus();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter(getContext());
        itemAdapter.setItemCallback(this::showBuyItemDialog);
        recyclerView.setAdapter(itemAdapter);
    }

    public void setUpUI() {
        if (inventoryViewModel.isManager()) {
            insertItemButton.setVisibility(View.VISIBLE);
            insertItemButton.setOnClickListener(v -> showInsertItemDialog());
            itemAdapter.setItemSwipeCallback(new ItemSwipeCallback() {
                @Override
                public void onDelete(Item item) {
                    inventoryViewModel.removeItem(item.getId());
                    Toast.makeText(getContext(), "Deleted " + item.getName(), Toast.LENGTH_SHORT).show();
                    itemAdapter.notifyItemRemoved(itemAdapter.getItems().indexOf(item));
                }

                @Override
                public void onEdit(Item item) {
                    showEditItemDialog(item);
                }
            });
            itemTouchHelperDelete = new ItemTouchHelper(new SwipeToDeleteCallback(itemAdapter, getContext()));
            itemTouchHelperDelete.attachToRecyclerView(recyclerView);

            itemTouchHelperEdit = new ItemTouchHelper(new SwipeToEditCallback(itemAdapter, getContext()));
            itemTouchHelperEdit.attachToRecyclerView(recyclerView);            // Setup ItemTouchHelper for swipe actions
        }
        swipeRefreshLayout.setOnRefreshListener(() -> {
            inventoryViewModel.fetchItems();
            swipeRefreshLayout.setRefreshing(false);
        });
//        inventoryViewModel.getItems().observe(getViewLifecycleOwner(), items -> {
//            itemAdapter.setItems(items);
//        });

        inventoryViewModel.getSearchResults().observe(getViewLifecycleOwner(), items -> {
            itemAdapter.setItems(items);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                inventoryViewModel.setSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inventoryViewModel.setSearchQuery(newText);
                return true;
            }
        });


    }

    private void showEditItemDialog(Item item) {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Item: " + item.getName());

        // Create the EditTexts for name, price, and quantity
        final EditText nameEditText = new EditText(requireContext());
        nameEditText.setHint("Enter item name");
        nameEditText.setText(item.getName());

        final EditText priceEditText = new EditText(requireContext());
        priceEditText.setHint("Enter item price");
        priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceEditText.setText(String.valueOf(item.getPrice()));

        final EditText quantityEditText = new EditText(requireContext());
        quantityEditText.setHint("Enter item quantity");
        quantityEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        quantityEditText.setText(String.valueOf(item.getQuantity()));

        final EditText descriptionEditText = new EditText(requireContext());
        descriptionEditText.setHint("Enter item description");
        descriptionEditText.setText(item.getDescription());


        // Create a layout to hold the EditTexts
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30); // Optional padding for the whole layout

        // Add the EditTexts to the layout
        layout.addView(nameEditText);
        layout.addView(priceEditText);
        layout.addView(quantityEditText);

        // Set the layout as the dialog's view
        builder.setView(layout);

        // Set dialog buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = nameEditText.getText().toString();
            float price = Float.parseFloat(priceEditText.getText().toString());
            int quantity = Integer.parseInt(quantityEditText.getText().toString());
            String description = descriptionEditText.getText().toString();
            Item updatedItem = new Item(name, quantity, price, description);
            inventoryViewModel.updateItem(item.getId(), updatedItem).observe(getViewLifecycleOwner(), item1 -> {
                inventoryViewModel.fetchItems();
                itemAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Updated " + name, Toast.LENGTH_SHORT).show();

            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showInsertItemDialog() {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Insert Item");

        // Create the EditText for the description
        final EditText descriptionEditText = new EditText(requireContext());
        descriptionEditText.setHint("Enter item description");
        descriptionEditText.setInputType(InputType.TYPE_CLASS_TEXT);

        // Create the EditTexts for name, price, and quantity
        final EditText nameEditText = new EditText(requireContext());
        nameEditText.setHint("Enter item name");

        final EditText priceEditText = new EditText(requireContext());
        priceEditText.setHint("Enter item price");
        priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        final EditText quantityEditText = new EditText(requireContext());
        quantityEditText.setHint("Enter item quantity");
        quantityEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Create a layout to hold the description and EditTexts
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30); // Optional padding for the whole layout

        // Add the description and EditTexts to the layout
        layout.addView(descriptionEditText);
        layout.addView(nameEditText);
        layout.addView(priceEditText);
        layout.addView(quantityEditText);

        // Set the layout as the dialog's view
        builder.setView(layout);

        // Set dialog buttons
        builder.setPositiveButton("Insert", (dialog, which) -> {
            String description = descriptionEditText.getText().toString();
            String name = nameEditText.getText().toString();
            float price = Float.parseFloat(priceEditText.getText().toString());
            int quantity = Integer.parseInt(quantityEditText.getText().toString());
            inventoryViewModel.insertItem(name, quantity, price, description).observe(getViewLifecycleOwner(), item -> {
                inventoryViewModel.fetchItems();

                itemAdapter.notifyDataSetChanged();
            });
            Toast.makeText(getContext(), "Inserted " + name, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showBuyItemDialog(Item item) {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Buy Item: " + item.getName());

        // Inflate a custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_buy_item, null);
        builder.setView(dialogView);

        // Find the Spinner in the custom layout
        Spinner quantitySpinner = dialogView.findViewById(R.id.quantity_spinner);

        // Populate the spinner with numbers from 1 to the item's available quantity
        List<Integer> quantities = new ArrayList<>();
        for (int i = 1; i <= item.getQuantity(); i++) {
            quantities.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, quantities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(adapter);

        // Set dialog buttons
        builder.setPositiveButton("Buy", (dialog, which) -> {
            int selectedQuantity = (int) quantitySpinner.getSelectedItem();
            inventoryViewModel.purchaseItem(item, selectedQuantity);
            Toast.makeText(getContext(), "Purchased " + selectedQuantity + " " + item.getName(), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}