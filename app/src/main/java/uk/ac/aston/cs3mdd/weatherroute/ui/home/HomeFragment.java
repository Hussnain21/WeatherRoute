package uk.ac.aston.cs3mdd.weatherroute.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.aston.cs3mdd.weatherroute.database.RoutesAdapter;
import uk.ac.aston.cs3mdd.weatherroute.database.MyDatabase;
import uk.ac.aston.cs3mdd.weatherroute.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private RoutesAdapter ra;
    private MyDatabase my_DB;
    private ArrayList<String> routes_id, route_name, start_point, end_point;
    private RecyclerView rcView;
    private SearchView searchBarView;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rcView = binding.recycleview;
        searchBarView = binding.searchView;

        my_DB = new MyDatabase(requireActivity());
        routes_id = new ArrayList<>();
        route_name = new ArrayList<>();
        start_point = new ArrayList<>();
        end_point = new ArrayList<>();

        displayData();

        ra = new RoutesAdapter(requireActivity(), routes_id, route_name, start_point, end_point);
        rcView.setAdapter(ra);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcView.setLayoutManager(layoutManager);

        searchBarView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterRoutes(newText);
                return true;
            }
        });

        return root;
    }

    //method to filter through the routes when a search is being made on the search bar and it only works viw the route name
    private void filterRoutes(String query) {
        ArrayList<String> filteredRoutesId = new ArrayList<>();
        ArrayList<String> filteredRouteName = new ArrayList<>();
        ArrayList<String> filteredStartPoint = new ArrayList<>();
        ArrayList<String> filteredEndPoint = new ArrayList<>();

        for (int i = 0; i < route_name.size(); i++) {

            if (route_name.get(i).toLowerCase().contains(query.toLowerCase())) {
                filteredRoutesId.add(routes_id.get(i));
                filteredRouteName.add(route_name.get(i));
                filteredStartPoint.add(start_point.get(i));
                filteredEndPoint.add(end_point.get(i));
            }
        }

        ra = new RoutesAdapter(requireActivity(), filteredRoutesId, filteredRouteName, filteredStartPoint, filteredEndPoint);
        rcView.setAdapter(ra);
    }

    //method to display the data from the database on this fragment
    public void displayData() {
        Cursor c = my_DB.readDate();
        if (c.getCount() == 0) {
            Toast.makeText(requireActivity(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            while (c.moveToNext()) {
                routes_id.add(c.getString(0));
                route_name.add(c.getString(1));
                start_point.add(c.getString(2));
                end_point.add(c.getString(3));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
