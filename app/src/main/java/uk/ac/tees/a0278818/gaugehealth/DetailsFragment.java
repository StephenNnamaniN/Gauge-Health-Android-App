package uk.ac.tees.a0278818.gaugehealth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import uk.ac.tees.a0278818.gaugehealth.conditionsModels.ItemViewModel;


public class DetailsFragment extends Fragment {
    private ItemViewModel itemViewModel;


    public DetailsFragment() {
        // Required empty public constructor
    }


    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemViewModel = new ViewModelProvider(requireActivity())
                .get(ItemViewModel.class);
        TextView itemName = view.findViewById(R.id.condition_name);
        TextView desDetails = view.findViewById(R.id.details_description);
        TextView url = view.findViewById(R.id.details_url);
        TextView articleStatus = view.findViewById(R.id.articleDes);



        itemViewModel.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            itemName.setText(item.getName());
            desDetails.setText(item.getDescription());
            url.setText(item.getUrl());
            articleStatus.setText(item.getArticleStatus());

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        return view;
    }
}