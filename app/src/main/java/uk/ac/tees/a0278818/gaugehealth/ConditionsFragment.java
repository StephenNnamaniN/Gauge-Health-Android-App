package uk.ac.tees.a0278818.gaugehealth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.a0278818.gaugehealth.Survey.SurveyMenu;
import uk.ac.tees.a0278818.gaugehealth.adapters.ItemsRecyclerViewAdapter;
import uk.ac.tees.a0278818.gaugehealth.adapters.OnItemClickListener;
import uk.ac.tees.a0278818.gaugehealth.conditionsModels.Item;
import uk.ac.tees.a0278818.gaugehealth.conditionsModels.ItemViewModel;
import uk.ac.tees.a0278818.gaugehealth.data.AsyncResponse;
import uk.ac.tees.a0278818.gaugehealth.data.Repository;
import uk.ac.tees.a0278818.gaugehealth.databinding.FragmentConditionsBinding;

public class ConditionsFragment extends Fragment  implements OnItemClickListener{
    private RecyclerView recyclerView;
    private ItemsRecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;

    private ItemViewModel itemViewModel;
    FragmentConditionsBinding binding;
    private TextView aTV, bTV, cTV, dTV, eTV, fTV, gTV, hTV, iTV, jTV, kTV, lTV, mTV, nTV, oTV,
                    pTV, qTV, rTV, sTV, tTV, uTV, vTV, wTV, xTV, yTV, zTV;

    public ConditionsFragment() {
    }

    public static ConditionsFragment newInstance(){
        ConditionsFragment fragment = new ConditionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = new ArrayList<>();
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        Repository.getItems(new AsyncResponse() {
            @Override
            public void processItem(List<Item> items) {
                itemList = items;
                itemViewModel.setSelectedItems(itemList);
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemViewModel = new ViewModelProvider(requireActivity())
                .get(ItemViewModel.class);
        if(itemViewModel.getItems().getValue() != null) {
            itemList = itemViewModel.getItems().getValue();
            recyclerViewAdapter = new ItemsRecyclerViewAdapter(itemList, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conditions, container, false);

        recyclerView = view.findViewById(R.id.conditions_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        itemList = new ArrayList<>();


//
//
//        aTV = binding.tvA;
//        bTV = binding.tvB;
//        cTV = binding.tvC;
//        dTV = binding.tvD;
//        eTV = binding.tvE;
//        fTV = binding.tvF;
//        gTV = binding.tvG;
//        hTV = binding.tvH;
//        iTV = binding.tvI;
//        jTV = binding.tvJ;
//        kTV = binding.tvK;
//        lTV = binding.tvL;
//        mTV = binding.tvM;
//        nTV = binding.tvN;
//        oTV = binding.tvO;
//        pTV = binding.tvP;
//        qTV = binding.tvQ;
//        rTV = binding.tvR;
//        sTV = binding.tvS;
//        tTV = binding.tvT;
//        uTV = binding.tvU;
//        vTV = binding.tvV;
//        wTV = binding.tvW;
//        xTV = binding.tvX;
//        yTV = binding.tvY;
//        zTV = binding.tvZ;

        return view;
    }

    @Override
    public void onItemClicked(Item item) {
        itemViewModel.setSelectedItem(item);
        DetailsFragment detailsFragment = new DetailsFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailsFragment, "null")
                .addToBackStack(null)
                .commit();
    }
}