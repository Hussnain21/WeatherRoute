package uk.ac.aston.cs3mdd.weatherroute.ui.add_routes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import uk.ac.aston.cs3mdd.weatherroute.database.MyDatabase;
import uk.ac.aston.cs3mdd.weatherroute.databinding.FragmentAddRoutesBinding;


public class AddRoutesFragment extends Fragment {

    private FragmentAddRoutesBinding binding;
    private EditText route_name, start_point, end_point;
    private Button add_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddRoutesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        route_name = binding.routeName;
        start_point = binding.startPoint;
        end_point = binding.endPoint;
        add_button = binding.addButton;
        add_button.setOnClickListener(v -> {
            addRouteIfFieldsFilled();
            hideKeyboard();
        });
        end_point.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addRouteIfFieldsFilled();
                return true;
            }
            return false;
        });
        return root;
    }

    //method to add routes to the database through text fields
    private void addRouteIfFieldsFilled() {
        String routeName = route_name.getText().toString().trim();
        String startPoint = start_point.getText().toString().trim();
        String endPoint = end_point.getText().toString().trim();

        if (routeName.isEmpty() || startPoint.isEmpty() || endPoint.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
            MyDatabase my_DB = new MyDatabase(requireActivity());
            my_DB.addRoute(routeName, startPoint, endPoint);
            route_name.setText("");
            start_point.setText("");
            end_point.setText("");

            hideKeyboard();
        }
    }

    //method to hide keyboard after the user presses enter or clicks the buttons
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }
}