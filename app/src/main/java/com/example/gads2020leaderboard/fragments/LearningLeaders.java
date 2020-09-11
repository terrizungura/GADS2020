package com.example.gads2020leaderboard.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.gads2020leaderboard.R;
import com.example.gads2020leaderboard.adapters.RecyclerViewAdapter;
import com.example.gads2020leaderboard.model.Stats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LearningLeaders extends Fragment {

    private final String JSON_URL = "https://gadsapi.herokuapp.com/api/hours";
    private JsonArrayRequest request ;
    private RequestQueue requestQueue ;
    private List<Stats> gadsList;
    private RecyclerView recyclerView ;


    public LearningLeaders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.learning_leaders, container, false);

        gadsList = new ArrayList<>() ;
        recyclerView = view.findViewById(R.id.learn_hours_list);
        jsonrequest();

        return view;

    }


    private void jsonrequest() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Top learners  data....");
        progressDialog.show();

        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                JSONObject jsonObject  = null ;


                for (int i = 0 ; i < response.length(); i++ ) {


                    try {
                        jsonObject = response.getJSONObject(i) ;
                        Stats gadsLn_hours = new Stats() ;
                        gadsLn_hours.setName(jsonObject.getString("name"));
                        gadsLn_hours.setLn_hours(String.valueOf(jsonObject.getInt("hours")));
                        gadsLn_hours.setCountry(jsonObject.getString("country"));



                        gadsLn_hours.setImage_url(jsonObject.getString("badgeUrl"));
                        gadsList.add(gadsLn_hours);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                setuprecyclerview(gadsList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });


        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(request) ;
    }

    private void setuprecyclerview(List<Stats> listGads) {

        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);

        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(getActivity(),listGads) ;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myadapter);

    }
}