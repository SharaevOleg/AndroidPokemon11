package com.example.androidpokemon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidpokemon.models.Pokemon;
import com.example.androidpokemon.models.PokemonResponse;
import com.example.androidpokemon.pokeapi.AboutActivity;
import com.example.androidpokemon.pokeapi.PokeapiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    //-----------------------------------------------------------------------------------
    EditText editText;
    Button button;
    Button button2;
    //-----------------------------------------------------------------------------------
    private static final String TAG = "POKEDEX";
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListPokemonAdapter listPokemonAdapter;
    private int offset;
    private boolean enough;

    ArrayList<Pokemon> listPokemon;
    Pokemon favourites = new Pokemon();
    ArrayList<String> favouritesPokemon = new ArrayList<>();
    String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//-----------------------------------------------------------------------------------
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = editText.getText().toString();
                favourites = findUsingEnhancedForLoop(str, listPokemon);
                if (favourites != null) {
//                    editText.setText(favourites.getName() + "YRA!!!");
                    onClick2(v);
                } else {
                    editText.setText("НЕТ ПОКЕМОНА!");
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);

                intent.putExtra("keyOne", favouritesPokemon);
                startActivity(intent);
            }
        });


//-----------------------------------------------------------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listPokemonAdapter = new ListPokemonAdapter(this);
        recyclerView.setAdapter(listPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (enough) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, " END");

                            enough = false;
                            offset += 20;
                            getData(offset);
                        }
                    }
                }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        enough = true;
        offset = 0;
        getData(offset);
    }

    private void getData(int offset) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonResponse> pokemonReactionCall = service.obtenerListPokemon(20, 40);

        pokemonReactionCall.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                enough = true;
                if (response.isSuccessful()) {
                    PokemonResponse pokemonResponse = response.body();
                    listPokemon = pokemonResponse.getResults();
                    listPokemonAdapter.insertListPokemon(listPokemon);

                } else {
                    Log.e(TAG, " onResponse: " + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                enough = true;
                Log.e(TAG, " onFailure: " + t.getMessage());
            }
        });
    }


    //-----------------------------------------------------------------------------------

    public Pokemon findUsingEnhancedForLoop(String name, ArrayList<Pokemon> customers) {

        for (Pokemon customer : customers) {
            if (customer.getName().equals(name)) {
                return customer;
            }
        }
        return null;
    }


    public void onClick2(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("УРА - ЕСТЬ ТАКОЙ ПОКЕМОН!").setMessage(str.toUpperCase());
        builder.setPositiveButton("FAVOURITES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                favouritesPokemon.add(str);
//                dialog.cancel();

            }
        });
        builder.setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());

//        favouritesPokemon.add(str);
        AlertDialog alert = builder.create();
        alert.show();
    }
//-----------------------------------------------------------------------------------
}



