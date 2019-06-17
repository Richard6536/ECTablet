package com.example.richard.ectablet.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.richard.ectablet.Activity.MainActivity;
import com.example.richard.ectablet.R;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mapbox.android.core.location.LocationEngine;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    //PlaceAutocompleteFragment autocompleteFragment;
    int AUTOCOMPLETE_REQUEST_CODE = 1;

    private View view;
    private MapView mapView;
    private LocationEngine locationEngine;
    private Style styleMap;
    private LocationComponent locationComponent;


    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private MapboxMap mapboxMap;
    SymbolManager symbolManager;

    GeoJsonSource geoJsonSource;
    SymbolLayer symbolLayer;

    int PERMISSION_ALL = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;


        mapboxMap.setStyle(new Style.Builder().fromUrl("mapbox://styles/zerods/cjkvmr0q606ks2ro0rt0o8wxc"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                styleMap = style;
                // Initialize Places.
                Places.initialize(getApplicationContext(), "AIzaSyAXRgGC2NP-RPcP0YCcpuw2QMUPEO4Hqsc");
                // Create a new Places client instance.
                PlacesClient placesClient = Places.createClient(getApplicationContext());

                /**
                 * Initialize Places. For simplicity, the API key is hard-coded. In a production
                 * environment we recommend using a secure mechanism to manage API keys.
                 */
                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), "AIzaSyAXRgGC2NP-RPcP0YCcpuw2QMUPEO4Hqsc");
                }

// Initialize the AutocompleteSupportFragment.
                AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                        getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {

                        if (symbolLayer != null) {
                            style.removeLayer("layer-id");
                            style.removeSource("source-id");
                        }

                        // Add the marker image to map
                        style.addImage("marker-icon-id",
                                BitmapFactory.decodeResource(
                                        MapFragment.this.getResources(), R.drawable.mapbox_marker_icon_default));

                        geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                                Point.fromLngLat(place.getLatLng().longitude, place.getLatLng().latitude)));
                        style.addSource(geoJsonSource);

                        symbolLayer = new SymbolLayer("layer-id", "source-id");
                        symbolLayer.withProperties(
                                PropertyFactory.iconImage("marker-icon-id")
                        );
                        style.addLayer(symbolLayer);

                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude)) // Sets the new camera position
                                .zoom(18) // Sets the zoom
                                .bearing(180) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build(); // Creates a CameraPosition from the builder

                        mapboxMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(position), 8000);

                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i(TAG, "An error occurred: " + status);
                    }
                });

                verifyStoragePermissions(getActivity());
            }
        });
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if permissions are enabled and if not request
        int permissionLocation = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ALL);
            }
            else{
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
            }

        }
        else
        {
            //Inicializa Mapas, posición & crea directorios si los permisos se han concedido
            enableLocationComponent(styleMap);
        }


    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(Style style) {

        // Get an instance of the component
        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        // Activate with a built LocationComponentActivationOptions object
        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(getContext(), style).build());

        // Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);

        // Set the component's camera mode
        locationComponent.setCameraMode(CameraMode.TRACKING);

        // Set the component's render mode
        locationComponent.setRenderMode(RenderMode.COMPASS);

        locationComponent.setLocationComponentEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableLocationComponent(styleMap);
        } else {
            getActivity().finish();
        }
    }

    public void putArguments(Bundle args){
        String value = args.getString("val");
    }
}
