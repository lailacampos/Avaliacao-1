package com.laila.unifacs.avaliacao1.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.laila.unifacs.avaliacao1.R;
import com.laila.unifacs.avaliacao1.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String permission = Manifest.permission.ACCESS_FINE_LOCATION;
    private Boolean[] isPermissionGranted = {false};
    private LocationManager locationManager;
    private LocationListener locationListener;
    private CameraPosition cameraPosition;

    private TextView latitudeTextView, longitudeTextView, velocidadeTextView;

    private SharedPreferences sharedPreferences;

    private int orientacaoMapaRadioButtonSelected;
    private int tipoMapaRadioButtonSelected;
    private int infoTrafegoRadioButtonSelected;
    private int coordGeograficasRadionButtonSelected;
    private int unidadeVelocidadeRadioButtonSelected;

    public static double latitude, longitude;
    public static float velocidade;

    private String latitudeString, longitudeString, velocidadeString;

    private final String PREFERENCE_NAME = "myPref";
    private final String ORIENTACAO_MAPA_KEY = "orientacaoMapaKey";
    private final String TIPO_MAPA_KEY = "tipoMapaKey";
    private final String INFO_TRAFEGO_KEY = "infoTrafegoKey";
    private final String VELOCIDADE_KEY = "velocidadeKey";
    private final String LATITUDE_KEY = "latitudeKey";
    private final String LONGITUDE_KEY = "longitudeKey";
    private final String COORD_GEO_KEY = "coordGeoKey";
    private final String UNIDADE_VELOCIDADE_KEY = "unidadeVelocidadeKey";

    private Circle accuracyCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        latitudeTextView = findViewById(R.id.latitude_textView);
        longitudeTextView = findViewById(R.id.longitude_textView);
        velocidadeTextView = findViewById(R.id.velocidade_textView);

        // Recupera os dados no sharedPreferences
        getPreferences();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        setContentView(R.layout.activity_maps);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setConfiguration();

        // Objeto responsável por gerenciar a localização do usuário
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        isPermissionGranted = Permission.checkPermission(this, this.permission);

        if (isPermissionGranted[0]) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            // Objeto responsável por escutar o evento de mudança de localização
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    mMap.clear();
                    // Pega a latidude e longitude do usuário
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Float accuracy = location.getAccuracy();
                    velocidade = location.getSpeed();

                    convertLatLong();

                    savePreferences();

                    // Objeto LatLng que representa a posição do marcador
                    LatLng myMarker = new LatLng(latitude, longitude);

                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.top_view_car_40);
                    MarkerOptions markerOptions = new MarkerOptions().position(myMarker).title("Marker").icon(icon).flat(true);

                    // Objeto do tipo Marker que representa o marcador em si
                    Marker marker = mMap.addMarker(markerOptions);

                    // Desenha o circulo ao redor do usuário
                    drawAccuracyCircle(location, accuracy);

                    marker.setRotation(location.getBearing());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myMarker));
                    rotateMap(location);

                }
            };

            // Sets the location provider and a listener for the user position
            // Seta o provedor de localização e um escutador para a posição do usuário
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    0.1f,
                    locationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Permission.checkPermission(this, this.permission);
    }

    public void getPreferences() {

        // Recupera o objeto sharedPreferences que contém a tabela com preferências salvas através do nome da preferência
        this.sharedPreferences = getSharedPreferences(this.PREFERENCE_NAME, 0);

        // Recupera o id do radioButton selecionado de cada RadioGroup
        this.orientacaoMapaRadioButtonSelected = this.sharedPreferences.getInt(this.ORIENTACAO_MAPA_KEY, 0);
        this.tipoMapaRadioButtonSelected = this.sharedPreferences.getInt(this.TIPO_MAPA_KEY, 0);
        this.infoTrafegoRadioButtonSelected = this.sharedPreferences.getInt(this.INFO_TRAFEGO_KEY, 0);
        this.coordGeograficasRadionButtonSelected = this.sharedPreferences.getInt(this.COORD_GEO_KEY, 0);
        this.unidadeVelocidadeRadioButtonSelected = this.sharedPreferences.getInt(this.UNIDADE_VELOCIDADE_KEY, 0);

    }

    private void savePreferences() {

        SharedPreferences.Editor editor = this.sharedPreferences.edit();

        editor.putString(LATITUDE_KEY, latitudeString);
        editor.putString(LONGITUDE_KEY, longitudeString);
        editor.putString(VELOCIDADE_KEY, velocidadeString);
        InfoFragment.setValues(latitudeString, longitudeString, velocidadeString);
        editor.apply();

    }

    // Desenha um circulo transparente ao redor da posição do usuário, cujo raio é o grau de precisão de localização
    private void drawAccuracyCircle(Location location, Float accuracy) {

        int accuracyStrokeColor = Color.argb(50, 130, 182, 228);
        int accuracyFillColor = Color.argb(100, 130, 182, 228);

        if (accuracyCircle != null) { accuracyCircle.remove();}

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(accuracy)
                .fillColor(accuracyFillColor)
                .strokeColor(accuracyStrokeColor)
                .strokeWidth(2.0f);

        accuracyCircle = mMap.addCircle(circleOptions);

    }

    // Changes map type
    private void changeMapType() {

        // Checks which radioButton is selected
        // Checa qual radioButton está selecionado
        if (tipoMapaRadioButtonSelected == R.id.vetorial_RadioButton) {

            // Vector map type is selected
            // Mapa do tipo Vector está selecionado
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else if (tipoMapaRadioButtonSelected == R.id.imagem_satelite_RadioButton) {

            // Satellite map type selected
            // Mapa do tipo Satélite está selecionado
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }

    private void enableTrafficInfo() {

        // Checks if traffic info is enabled
        // Checa se o informação de tráfico está ligada
        if (infoTrafegoRadioButtonSelected == R.id.trafego_ligado_RadioButton) {

            // Traffic info is enabled
            // Informação de tráfico está ligada
            mMap.setTrafficEnabled(true);
        }
        else if (infoTrafegoRadioButtonSelected == R.id.trafego_desligado_RadioButton) {

            // Traffic info is disabled
            // Informação de tráfico está desligada
            mMap.setTrafficEnabled(false);
        }

    }

    private void rotateMap(Location location) {

        if (orientacaoMapaRadioButtonSelected == R.id.orientacao_course_up_RadioButton) {

            if (location.hasBearing()) {

                CameraPosition camPos = CameraPosition
                        .builder(
                                mMap.getCameraPosition() // current Camera
                        )
                        .bearing(location.getBearing())
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
                mMap.getUiSettings().setRotateGesturesEnabled(false);
            }
        }
        else if (orientacaoMapaRadioButtonSelected == R.id.orientacao_north_up_RadioButton) {

            CameraPosition camPos = CameraPosition
                    .builder(
                            mMap.getCameraPosition() // current Camera
                    )
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
            mMap.getUiSettings().setRotateGesturesEnabled(false);
        }

        else if (orientacaoMapaRadioButtonSelected == R.id.orientacao_nenhuma_RadioButton) {

            CameraPosition camPos = CameraPosition
                    .builder(
                            mMap.getCameraPosition() // current Camera
                    ).build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));

        }
    }

    private void convertLatLong() {

        if (coordGeograficasRadionButtonSelected == R.id.grau_decimal_RadioButton) {
            latitudeString = Location.convert(latitude, Location.FORMAT_DEGREES);
            longitudeString = Location.convert(longitude, Location.FORMAT_DEGREES);
        }
        else if (coordGeograficasRadionButtonSelected == R.id.grau_minuto_RadioButton) {
            latitudeString = Location.convert(latitude, Location.FORMAT_MINUTES);
            longitudeString = Location.convert(longitude, Location.FORMAT_MINUTES);
        }
        else if (coordGeograficasRadionButtonSelected == R.id.grau_minuto_segundo_RadioButton) {
            latitudeString = Location.convert(latitude, Location.FORMAT_SECONDS);
            longitudeString = Location.convert(longitude, Location.FORMAT_SECONDS);
        }
    }

    private void convertVelocity() {

        if (unidadeVelocidadeRadioButtonSelected == R.id.milha_hora_RadioButton) {
            velocidadeString = Float.toString(velocidade / 1.609344f);
        } else {velocidadeString = Float.toString(velocidade);}
    }

    private void setConfiguration() {

        changeMapType();
        enableTrafficInfo();
    }
}