package com.example.garbagetruck;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SupportMapFragment mapFragment;
    FrameLayout frameLayout;
    GoogleMap mMap;
    boolean locationPermissionGranted = false;
    CameraPosition cameraPosition;
    PlacesClient placesClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location lastKnownLocation;

    int M_MAX_ENTRIES = 5;
    String[] likelyPlaceNames;
    String[] likelyPlaceAddresses;
    List[] likelyPlaceAttributions;
    LatLng[] likelyPlaceLatLngs;

    String TAG = home.class.getSimpleName();
    LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);

    String KEY_CAMERA_POSITION = "camera_position";
    String KEY_LOCATION = "location";

    FloatingActionButton floatingActionButton;

    Spinner spinnerCountry,spinnerDistrict;
    Button buttonRoute;


    int spinnerCountryIndex,spinnerDistrictIndex;

    String[] route,truck;
    boolean[] routeShow;
    String truckURL = "https://api.kcg.gov.tw/api/service/get/aaf4ce4b-4ca8-43de-bfaf-6dc97e89cac0";

    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static home newInstance(String param1, String param2) {
        home fragment = new home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        mapFrament = getFragmentManager().findFragmentById(R.id.map);
//        mapFrament.getActivity();
//        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync( this::onMapReady);


        mapFragment = SupportMapFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map,mapFragment).commit();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
//                mMap = googleMap;
//
//                LatLng sydney = new LatLng(-34,151);
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap = googleMap;


                Places.initialize(getContext(),getString(R.string.google_API));
                placesClient = Places.createClient(getContext());
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
//                Prompt the user for permission.
                getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
                updateLocationUI();

        // Get the current location of the device and set the position of the map.
                getDeviceLocation();
//                LatLng kushan = new LatLng(22.65319353788493,120.29219744600017);
//                mMap.addMarker(new MarkerOptions().position(kushan).title("Marker in Kushan"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(kushan));

//


            }
        });

//        Places.initialize(getContext(), String.valueOf(R.string.google_API));
//        placesClient = Places.createClient(getContext());
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
//
////         Build the map.
//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this::onMapReady);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = getView().findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                new DownloadTask().execute(truckURL);
//                Toast.makeText(getContext(),"點擊定位按鈕",Toast.LENGTH_SHORT).show();
                Places.initialize(getContext(),getString(R.string.google_API));
                placesClient = Places.createClient(getContext());
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

                // Prompt the user for permission.
                getLocationPermission();

                // Turn on the My Location layer and the related control on the map.
                updateLocationUI();

                // Get the current location of the device and set the position of the map.
                getDeviceLocation();

            }
        });
        spinnerCountry = getView().findViewById(R.id.spinner);
        spinnerCountry.bringToFront();
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.country, android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter1);
        spinnerCountry.setSelection(12);
        spinnerDistrict = getView().findViewById(R.id.spinner2);
        spinnerDistrict.bringToFront();
        buttonRoute = getView().findViewById(R.id.button);
        buttonRoute.bringToFront();
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<CharSequence> adapter2;
                spinnerCountryIndex = i;
                switch (i){
                    case 0:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.keelungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 1:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.newTaipeiDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 2:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.taipeiDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 3:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.taoyuanDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 4:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.hsinchuDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 5:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.miaoliDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 6:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.taichungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 7:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.changhuaDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 8:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.nantouDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 9:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.yunlinDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 10:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.chiayiDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 11:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.tainanDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 12:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.kaohsiungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 13:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.pingtungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 14:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.taitungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 15:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.hualienDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 16:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.yilanDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 17:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.penghuDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 18:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.kinmenDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 19:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.lienchiangDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
                spinnerDistrict.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerCountryIndex = 12;
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.kaohsiungDistrict, android.R.layout.simple_spinner_dropdown_item);
                spinnerDistrict.setAdapter(adapter2);
            }
        });
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerDistrictIndex = i;
                if(spinnerCountryIndex == 12 && i == 0){
                    route = getResources().getStringArray(R.array.route_kaohsiung_1);
                    truck = getResources().getStringArray(R.array.garbageTruck_kaohsiung_1);
                    routeShow = new boolean[route.length];
                }
                else if(spinnerCountryIndex == 12 && i == 1){
                    route = getResources().getStringArray(R.array.route_kaohsiung_2);
                    truck = getResources().getStringArray(R.array.garbageTruck_kaohsiung_2);
                    routeShow = new boolean[route.length];
                }
                else {
                    route = getResources().getStringArray(R.array.route1);
                    truck = getResources().getStringArray(R.array.garbageTruck_kaohsiung_1);
                    routeShow = new boolean[route.length];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerDistrictIndex = 0;
                route = getResources().getStringArray(R.array.route_kaohsiung_1);
                truck = getResources().getStringArray(R.array.garbageTruck_kaohsiung_1);
                routeShow = new boolean[route.length];
            }
        });
        buttonRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[] tmp = new boolean[route.length];
                System.arraycopy(routeShow,0,tmp,0,route.length);
                new AlertDialog.Builder(getContext()).setTitle("選擇要顯示的路線").setMultiChoiceItems(route, tmp, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                    }
                }).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.arraycopy(tmp,0,routeShow,0,route.length);
                        mMap.clear();
                        // Prompt the user for permission.
                        getLocationPermission();

                        // Turn on the My Location layer and the related control on the map.
                        updateLocationUI();

                        // Get the current location of the device and set the position of the map.
                        getDeviceLocation();
                        new DownloadTask().execute(truckURL);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

            }
        });

    }

    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        Places.initialize(getContext(),getString(R.string.google_API));
        placesClient = Places.createClient(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

//        this.mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//            @Override
//            // Return null here, so that getInfoContents() is called next.
//            public View getInfoWindow(Marker arg0) {
//                return null;
//            }

//            @Override
//            public View getInfoContents(Marker marker) {
//                // Inflate the layouts for the info window, title and snippet.
//                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
//                        (FrameLayout) findViewById(R.id.map), false);
//
//                TextView title = infoWindow.findViewById(R.id.title);
//                title.setText(marker.getTitle());
//
//                TextView snippet = infoWindow.findViewById(R.id.snippet);
//                snippet.setText(marker.getSnippet());
//
//                return infoWindow;
//            }
//        });

        // Prompt the user for permission.
//        getLocationPermission();
//
//        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI();
//
//        // Get the current location of the device and set the position of the map.
//        getDeviceLocation();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude())).title("Marker in Location"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 18));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, 18));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        home.this.openPlacesDialog();
                    }
                    else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title("Sydney")
                    .position(defaultLocation)
                    .snippet("In Sydney"));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        15));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("請選擇一個地點")
                .setItems(likelyPlaceNames, listener)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Places.initialize(getContext(),getString(R.string.google_API));
        placesClient = Places.createClient(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if(mMap != null){
            mMap.clear();
            // Prompt the user for permission.
            getLocationPermission();

            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
            getDeviceLocation();
            new DownloadTask().execute(truckURL);
        }

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return downloadUrl(truckURL);
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.garbagetruck);
                Bitmap b = bitmapDrawable.getBitmap();
                Bitmap smallIcon = Bitmap.createScaledBitmap(b,100,100,false);
//                Toast.makeText(getContext(),"getJson",Toast.LENGTH_SHORT).show();
//                LatLng truckPosition2 = new LatLng(22.642873,120.278835);
//                mMap.addMarker(new MarkerOptions().position(truckPosition2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("第一責任區"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(truckPosition2));
                JSONArray data = new JSONObject(s).getJSONArray("data");
//                Toast.makeText(getContext(),"After JSON",Toast.LENGTH_SHORT);
                Log.d("test","After JSON");
                Log.d("test", String.valueOf(mMap==null));
                for(int i=0;i<data.length();i++){
                    JSONObject siteObj = data.getJSONObject(i);
                    for (int j=0;j<routeShow.length;j++){
                        if(routeShow[j]){
                            if(siteObj.getString("car").equals(truck[j])){
//                                Toast.makeText(getContext(),"Get Car",Toast.LENGTH_SHORT);
                                LatLng truckPosition = new LatLng(Double.parseDouble(siteObj.getString("y")),Double.parseDouble(siteObj.getString("x")));
                                mMap.addMarker(new MarkerOptions().position(truckPosition).icon(BitmapDescriptorFactory.fromBitmap(smallIcon)).title(route[j]));
                            }
                        }
                    }



                }
            }
            catch (JSONException e) {
//                Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_LONG);
                e.printStackTrace();
            }
        }
    }

    private String downloadUrl(String urlString)
    {
        String strHTML = "";
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); // milliseconds
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();

            if(stream != null)
            {
                int leng = 0;
                byte[] Data = new byte[100];
                byte[] totalData = new byte[0];
                int totalLeg = 0;
                do
                {
                    leng = stream.read(Data);
                    if(leng > 0)
                    {
                        totalLeg += leng;
                        byte[] temp = new byte[totalLeg];
                        System.arraycopy(totalData, 0, temp, 0, totalData.length);
                        System.arraycopy(Data, 0, temp, totalData.length, leng);
                        totalData = temp;
                    }
                }
                while(leng > 0);
                // strReturn = new String(totalData,"UTF-8");
                strHTML = new String(totalData, "UTF-8");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
//            Log.e(TAG, e.toString());
        }
        return strHTML;
    }

    private void updateConnectedFlags()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null)
        {
            NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
            if(activeNetwork != null && activeNetwork.isConnected())
            {
                wifiConnected = activeNetwork.isConnected();
                mobileConnected = activeNetwork.isConnected();
            }
            else
            {
                wifiConnected = false;
                mobileConnected = false;
            }
        }
        else
        {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    Object getSystemService(String name) {
        throw new RuntimeException("Stub!");
    }
}

